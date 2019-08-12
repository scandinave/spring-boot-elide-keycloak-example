package com.example.demo.services;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

public final class OidcUtils {

    public static final String ALGORITM_HMACSHA256 = "HmacSHA256";

    private OidcUtils() {
    }

    /**
     * Get a RPT token that contains the authorization from the AccessToken
     * 
     * @param accessToken The accessToken of the user making the request
     * @return The RPT if found or null otherwise
     */
    public static String requestRPT(String accessToken) {

        Map<String, Object> serverCredentials = new HashMap<String, Object>();
        serverCredentials.put("secret", "ede3c5fd-6888-45d4-ac32-20cae58c0ee2");
        AuthzClient authzClient = AuthzClient.create(new Configuration("http://localhost:8080/auth", "spring",
                "spring-boot-elide", serverCredentials, null));

        // create an authorization request
        AuthorizationRequest authRequest = new AuthorizationRequest();

        // send the entitlement request to the server in order to
        // obtain an RPT with all permissions granted to the user
        AuthorizationResponse response = authzClient.authorization(accessToken).authorize(authRequest);

        return response.getToken();
    }

    /**
     * Verify the token signature
     * 
     * @param jwt the token to verify
     * @param key the key used to signed the token
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IllegalStateException
     * @throws UnsupportedEncodingException
     */
    public static boolean validToken(String jwt, String key, String algoritm)
            throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, UnsupportedEncodingException {
        String[] token = jwt.split("\\.");
        String header = token[0];
        String payload = token[1];
        String originalSignature = token[2];

        SecretKeySpec secret = new SecretKeySpec(Base64.getDecoder().decode(key), algoritm);
        Mac mac = Mac.getInstance(algoritm);
        mac.init(secret);

        StringBuilder headerAndPayload = new StringBuilder(header).append(".").append(payload);

        byte[] signatureBytes = mac.doFinal(headerAndPayload.toString().getBytes(StandardCharsets.UTF_8.name()));
        String calculatedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);

        return calculatedSignature.equals(originalSignature);
    }

    public static extractClaims(String jwt) {

    }
}