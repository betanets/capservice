package com.betanet.capservice.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alexander Shkirkov
 */
@Service
public class CaptchaService {
    
    private final int CAPTCHA_LENGTH = 16;
    private final int CAPTCHA_RANGE = 21;
    
    public String generateCaptchaString(){
        Random random = new Random(System.currentTimeMillis());
        String captchaString = "";
        
        for(int i = 0; i < CAPTCHA_LENGTH; i++){
            int currentNumber = random.nextInt(CAPTCHA_RANGE);
            if(currentNumber % 2 == 0){
                captchaString += (char)('a' + currentNumber);
            } else {
                captchaString += String.valueOf(random.nextInt(10));
            }
        }
        return captchaString;
    }
    
    public BufferedImage generateImage(String text){
        BufferedImage image = new BufferedImage(400, 40, BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 400, 40);
        GradientPaint gradientPaint = new GradientPaint(10, 5, Color.BLUE, 20, 10, Color.LIGHT_GRAY, true);
        graphics.setPaint(gradientPaint);
        Font font = new Font("Comic Sans MS", Font.BOLD, 30);
        graphics.setFont(font);
        graphics.drawString(text, 5, 30);
        graphics.dispose();
        return image;
    }
}
