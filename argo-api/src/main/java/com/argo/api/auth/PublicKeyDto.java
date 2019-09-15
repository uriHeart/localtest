package com.argo.restapi.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublicKeyDto {
    private String publicKey;
}
