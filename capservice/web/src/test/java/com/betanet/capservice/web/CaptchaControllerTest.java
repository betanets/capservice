package com.betanet.capservice.web;

import com.betanet.capservice.domain.CaptchaEntity;
import com.betanet.capservice.service.CaptchaService;
import com.betanet.capservice.web.controllers.CaptchaController;
import java.time.LocalDateTime;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.DigestUtils;

/**
 *
 * @author Alexander Shkirkov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/servlet-context.xml")
@WebAppConfiguration
public class CaptchaControllerTest {
    
    @Autowired
    private CaptchaService captchaService;
    
    @Autowired
    private CaptchaController captchaController;

    @Test
    public void testExamplePostMessage() {
        assertEquals("error", captchaController.postCaptcha("examplerequestid", "examplestring"));
    }
    
    @Test
    public void testGeneratedStringPostMessage(){
        String captchaString = captchaService.generateCaptchaString();
        String md5CaptchaString = DigestUtils.md5DigestAsHex(captchaString.getBytes());
        captchaService.getCaptchaEntities().clear();
        captchaService.getCaptchaEntities().add(new CaptchaEntity(md5CaptchaString, captchaString, LocalDateTime.now().plusSeconds(captchaController.getCAPTCHA_TTL())));
        assertEquals("success", captchaController.postCaptcha(md5CaptchaString, captchaString));
        assertEquals("error", captchaController.postCaptcha(md5CaptchaString, captchaString));
    }
    
    @Test
    public void testGeneratedStringAndWaitPostMessage() throws InterruptedException{
        String captchaString = captchaService.generateCaptchaString();
        String md5CaptchaString = DigestUtils.md5DigestAsHex(captchaString.getBytes());
        captchaService.getCaptchaEntities().clear();
        captchaService.getCaptchaEntities().add(new CaptchaEntity(md5CaptchaString, captchaString, LocalDateTime.now().plusSeconds(captchaController.getCAPTCHA_TTL())));
        Thread.sleep((captchaController.getCAPTCHA_TTL() + 1) * 1000);
        assertEquals("error", captchaController.postCaptcha(md5CaptchaString, captchaString));
    }
}
