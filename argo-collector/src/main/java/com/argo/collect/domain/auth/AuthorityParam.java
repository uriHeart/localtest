package com.argo.collect.domain.auth;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class AuthorityParam {
    private String id;
    private String password;
    private String baseUrl;
    private String tokenUrl;
    private String loginUrl;
}
