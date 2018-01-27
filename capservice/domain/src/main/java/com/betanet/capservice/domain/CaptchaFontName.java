package com.betanet.capservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Alexander Shkirkov
 */
@AllArgsConstructor
public enum CaptchaFontName {
    COMIC_SANS("Comic Sans MS"),
    HELVETICA("Helvetica"),
    TAHOMA("Tahoma"),
    TIMES("Times"),
    TIMES_NEW_ROMAN("Times New Roman"),
    VERDANA("Verdana");
    
    @Getter
    private final String fontName;
}
