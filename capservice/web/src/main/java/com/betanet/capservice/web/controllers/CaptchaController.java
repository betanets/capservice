package com.betanet.capservice.web.controllers;

import com.betanet.capservice.domain.CaptchaEntity;
import com.betanet.capservice.service.CaptchaService;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Alexander Shkirkov
 */
@Controller
public class CaptchaController {
    
    @Getter
    private final int CAPTCHA_TTL = 10;
    
    @Autowired
    private CaptchaService captchaService;
    
    @RequestMapping(value = "/getcaptcha", method = RequestMethod.GET)
    public void getCaptcha(HttpServletResponse response) {
        String captchaString = captchaService.generateCaptchaString();
        String md5CaptchaString = DigestUtils.md5DigestAsHex(captchaString.getBytes());
        
        response.addHeader("request_id", md5CaptchaString);
        response.addHeader("captcha_string", captchaString);
        
        response.setContentType("image/png");
        try {
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(captchaService.generateImage(captchaString), "png", out);
            out.close();
        } catch (IOException ex) {
            System.out.println("Error while sending captcha");
        }
        captchaService.getCaptchaEntities().add(new CaptchaEntity(md5CaptchaString, captchaString, LocalDateTime.now().plusSeconds(CAPTCHA_TTL)));
    }
    
    @ResponseBody
    @RequestMapping(value = "/postcaptcha", method = RequestMethod.POST)
    public String postCaptcha(
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
