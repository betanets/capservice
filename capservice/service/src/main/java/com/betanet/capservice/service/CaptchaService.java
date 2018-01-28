package com.betanet.capservice.service;

import com.betanet.capservice.domain.CaptchaEntity;
import com.betanet.capservice.domain.CaptchaFontName;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Alexander Shkirkov
 */
@Service
public class CaptchaService {
    
    private final int CAPTCHA_LENGTH = 16;
    private final int CAPTCHA_RANGE = 21;
    
    @Getter
    private final List<CaptchaEntity> captchaEntities = new ArrayList<>();
    
    @Scheduled(fixedRate = 5000)
    private void clearEntities(){
        System.out.println("trytodel");
        Iterator it = captchaEntities.iterator();
        while(it.hasNext()){
            CaptchaEntity item = (CaptchaEntity) it.next();
            if(item.getExpirationDateTime().isBefore(LocalDateTime.now())){
                it.remove();
            }
        }
    }
        
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
    
    private static void drawRotate(Graphics2D g2d, double x, double y, int angle, String text) 
    {    
        g2d.translate((float)x,(float)y);
        g2d.rotate(Math.toRadians(angle));
        g2d.drawString(text,0,0);
        g2d.rotate(-Math.toRadians(angle));
        g2d.translate(-(float)x,-(float)y);
    }    
    
    public BufferedImage generateImage(String text){
        Random random = new Random(System.currentTimeMillis());
        
        BufferedImage image = new BufferedImage(400, 40, BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 400, 40);
        GradientPaint gradientPaint = new GradientPaint(10, 5, Color.RED, 20, 10, Color.LIGHT_GRAY, true);
        graphics.setPaint(gradientPaint);
        
        int fontCount = CaptchaFontName.values().length;
        for(int i = 0; i < text.length(); i++){
            graphics.setFont(new Font(CaptchaFontName.values()[random.nextInt(fontCount)].getFontName(), Font.BOLD, 20 + random.nextInt(10)));
            drawRotate(graphics, 20 * (i+1), 30, -30 + random.nextInt(60), String.valueOf(text.charAt(i)));
        }
        
        graphics.dispose();
        return image;
    }
    
    public CaptchaEntity findCaptchaEntity(String requestId, String captchaKey){
        CaptchaEntity entityToFind = new CaptchaEntity(requestId, captchaKey, LocalDateTime.now());
        
        Iterator it = captchaEntities.iterator();
        while(it.hasNext()){
            CaptchaEntity entity = (CaptchaEntity) it.next();
            if(entity.getRequestId().equals(entityToFind.getRequestId())
                    && entity.getCaptchaKey().equals(entityToFind.getCaptchaKey())){
                if(entity.getExpirationDateTime().isAfter(entityToFind.getExpirationDateTime())){
                    it.remove();
                    return entity;
                }
                return null;
            }
        }
        return null;
    }
}
