package com.betanet.capservice.service;

import com.betanet.capservice.domain.CaptchaEntity;
import java.time.LocalDateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Alexander Shkirkov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/service-context.xml")
public class CaptchaServiceTest {
    
    @Autowired
    private CaptchaService captchaService;
    
    @Test
    public void captchaStringGeneration() {
        assertEquals(captchaService.getCAPTCHA_LENGTH(), captchaService.generateCaptchaString().length());
    }
    
    @Test(timeout = 5000)
    public void captchaEntityFind() {
        captchaService.getCaptchaEntities().clear();
        captchaService.getCaptchaEntities().add(new CaptchaEntity("examplerequestid", "examplestring", LocalDateTime.now().plusSeconds(5)));
        assertNotNull(captchaService.findCaptchaEntity("examplerequestid", "examplestring"));
    }
    
    @Test
    public void generateExampleImage(){
        assertNotNull(captchaService.generateImage("examplestring"));
    }
}

