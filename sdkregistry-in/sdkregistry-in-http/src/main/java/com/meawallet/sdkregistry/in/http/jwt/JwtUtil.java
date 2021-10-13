package com.meawallet.sdkregistry.in.http.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.ParseException;

import static reactor.core.publisher.Mono.justOrEmpty;

@Component
public class JwtUtil {

    public Boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = parseSignedJwt(token);
            final String kid = signedJWT.getHeader().getKeyID();
            // get db record

            // check headers
            // 1. are only alg, kid
            // 2. alg == rs256
            signedJWT.getHeader();

            // check claims
            // 1. are only iss, iid, exp, iat
            // 2. exp, iat
            // 3. iss == db.merchant_name
            signedJWT.getJWTClaimsSet().getClaims();

            // verifySignature(db.public_key)
        } catch (JOSEException | ParseException e) {
            e.printStackTrace();
        }

        return true;
    }

    private SignedJWT parseSignedJwt(String token) throws JOSEException, ParseException {
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(token);
        } catch (java.text.ParseException e) {
            // Invalid signed JWT encoding
        }
        return signedJWT;
    }

    private Mono<SignedJWT> parseSignedJwtMono(String token) {
        try {
            return Mono.just(SignedJWT.parse(token));
        } catch (ParseException e) {
            return Mono.empty();
        }
    }

    private boolean verifySignature (JWSObject jwsObject, RSAKey publicKey) throws JOSEException {
        JWSVerifier verifier = new RSASSAVerifier(publicKey);
        return jwsObject.verify(verifier);
    }
}
