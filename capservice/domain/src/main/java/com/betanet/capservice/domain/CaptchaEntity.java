package com.betanet.capservice.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Alexander Shkirkov
 */
@Getter
@Setter
@AllArgsConstructor
public class CaptchaEntity {
    String requestId;
    String captchaKey;
    LocalDateTime expirationDateTime;
}
