package com.betanet.capservice.web.controllers;

import com.betanet.capservice.service.CaptchaService;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    public void index(Model model, HttpServletResponse response) {
        String captchaString = captchaService.generateCaptchaString();
        response.addHeader("captcha_string", captchaString);
        response.setContentType("image/jpg");
        try {
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(captchaService.generateImage(captchaString), "jpeg", out);
            out.close();
        } catch (IOException ex) {
            log.error("Error while sending captcha");
        }
        
        //return "base";
    }
}
