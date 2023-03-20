package com.ccagigao.pocs.security;

import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Component
public class TokenProvider {
    @Autowired
    private TokenRoleProperties tokenRoleProperties;

    private Jwt getToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof Jwt) {
           return (Jwt) auth.getPrincipal();
        }
        throw new AuthorizationServiceException("Service without valid Bearer Token");
    }

    public boolean hasRole(String role) {
        var claimsObject = getClaimsObject();
        if (claimsObject instanceof List<?>) {
            return ((List<?>) claimsObject).contains(role);
        } else {
            return claimsObject.equals(role);
        }
    }

    public boolean hasAnyRole(String... roles) {
        Set<String> roleSet = new HashSet<String>(Arrays.asList(roles));
        var claimsObject = getClaimsObject();
        if (claimsObject instanceof List<?>) {
            return ((List<?>) claimsObject).stream().anyMatch(x -> roleSet.contains(x));
        } else {
            return roleSet.contains(claimsObject);
        }
    }

    private Object getClaimsObject() {
        var tokenClaims = getToken().getClaims();
        JSONObject map = new JSONObject(tokenRoleProperties.getStructure());
        while(true) {
            String currentValue = (String)map.keySet().toArray()[0];
            if (map.get(currentValue) instanceof Map<?,?>) {
                var currentObject = (Map<String, Object>) map.get(currentValue);
                map = new JSONObject(currentObject);
                tokenClaims = (Map<String, Object>) tokenClaims.get(currentValue);
            } else {
                String roleContainerObject = (String) map.get(currentValue);
                if (roleContainerObject.equals("list")) {
                    return ((List<String>)tokenClaims.get(currentValue));
                } else if (roleContainerObject.equals("value")) {
                    return tokenClaims.get(currentValue);
                } else {
                    throw new NotFoundException("Invalid type: Must be 'list' or 'value'");
                }
            }
        }
    }

}
