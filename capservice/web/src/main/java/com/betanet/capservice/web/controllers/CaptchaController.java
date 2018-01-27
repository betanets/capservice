package com.betanet.capservice.web.controllers;

import com.betanet.capservice.domain.CaptchaEntity;
import com.betanet.capservice.service.CaptchaService;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Alexander Shkirkov
 */
@Slf4j
@Controller
public class CaptchaController {
    
    @Autowired
    private CaptchaService captchaService;
    
    @RequestMapping(value = "/getcaptcha", method = RequestMethod.GET)
    public void getCaptcha(Model model, HttpServletResponse response) {
        String captchaString = captchaService.generateCaptchaString();
        String md5CaptchaString = DigestUtils.md5DigestAsHex(captchaString.getBytes());
        
        response.addHeader("request_id", md5CaptchaString);
        response.addHeader("captcha_string", captchaString);
        
        response.setContentType("image/jpg");
        try {
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(captchaService.generateImage(captchaString), "jpeg", out);
            out.close();
        } catch (IOException ex) {
            log.error("Error while sending captcha");
        }
        captchaService.getCaptchaEntities().add(new CaptchaEntity(md5CaptchaString, captchaString, LocalDateTime.now().plusSeconds(100)));
        System.out.println("ww: " + captchaService.getCaptchaEntities().size());
    }
    
    @ResponseBody
    @RequestMapping(value = "/postcaptcha", method = RequestMethod.POST)
    public String postCaptcha(Model model, 
            @RequestParam(name = "request_id", required = true) String requestId,
            @RequestParam(name = "captcha_string", required = true) String captchaString){
        CaptchaEntity resultEntity = captchaService.findCaptchaEntity(requestId, captchaString);
        if(resultEntity != null){
            return "success";
        } else {
            return "error";
        }
    }
}