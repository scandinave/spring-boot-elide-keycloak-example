package com.example.demo.services;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

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
     * Decode a JWT token and verify it's signature
     * 
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws IllegalArgumentException
     * @throws MalformedJwtException
     * @throws UnsupportedJwtException
     * @throws ExpiredJwtException
     * @throws SignatureException
     */
    public static Claims extractToken(String token, String signinKey) throws SignatureException, ExpiredJwtException,
            UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
        // JwtConsumer jwtConsumer = new JwtConsumerBuilder().setRequireExpirationTime()
        // // the JWT must have an expiration
        // time
        // .setRequireSubject() // the JWT must have a subject claim
        // // .setExpectedIssuer("Issuer") // whom the JWT needs to have been issued by
        // // .setExpectedAudience("Audience") // to whom the JWT is intended for
        // .setVerificationKey(signinKey.getBytes("UTF-8")) // verify the signature with
        // the public key
        // .build(); // create the JwtConsumer instance

        // JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
        // System.out.println("JWT validation succeeded! ");

        // return Jwts.parser()
        // .setSigningKey(signinKey.getBytes("UTF-8"))
        // .parseClaimsJws(token).getBody();
        return null;
}