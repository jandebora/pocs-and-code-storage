package com.ccagigao.pocs.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@PreAuthorize("isAuthenticated()")
public class MyController {

    @GetMapping(value = "/helloWorld")
    @PreAuthorize("@tokenProvider.hasAnyRole('randomRole','admin')")
    public String helloWorld(@Parameter(hidden = true) @AuthenticationPrincipal Jwt principal){
        return "Hello World " + principal.getClaim("preferred_username");
    }
}
