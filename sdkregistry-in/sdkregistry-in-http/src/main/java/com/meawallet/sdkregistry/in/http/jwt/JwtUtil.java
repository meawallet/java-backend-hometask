package com.meawallet.sdkregistry.in.http.jwt;

import com.meawallet.sdkregistry.core.merchantkey.MerchantKeyService;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.JSONObjectUtils;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

import java.text.ParseException;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

@Component
public class JwtUtil {

    private static final String HEADER_ALG = "alg";
    private static final String HEADER_KID = "kid";
    private static final Set<String> ALL_HEADERS = Set.of(HEADER_ALG, HEADER_KID);

    private static final String CLAIM_ISS = "iss";
    private static final String CLAIM_IID = "iid";
    private static final String CLAIM_EXP = "exp";
    private static final String CLAIM_IAT = "iat";
    private static final Set<String> ALL_CLAIMS = Set.of(CLAIM_ISS, CLAIM_IID, CLAIM_EXP, CLAIM_IAT);

    private MerchantKeyService merchantKeyService;

    JwtUtil(MerchantKeyService merchantKeyService) {
        this.merchantKeyService = merchantKeyService;
    }

    public Mono<Boolean> validateToken(String token) {
        var jwtWithValidatedClaims = jwtWithValidatedClaims(token);

        return Mono.zip(jwtWithValidatedClaims, jwtWithValidatedClaims.flatMap(t -> merchantKeyService.findByKid(t.getHeader().getKeyID())))
                   .map(TupleUtils.function(JwtMerchantKeyPair::new))
                   .filter(keyBelongsToMerchant)
                   .switchIfEmpty(Mono.error(() -> new RuntimeException("Key does not belong to Merchant")))
                   .filter(signatureIsValid)
                   .switchIfEmpty(Mono.error(() -> new RuntimeException("Signature is not valid")))
                   .map(Objects::nonNull);
    }

    private Mono<SignedJWT> jwtWithValidatedClaims(String token) {
        return parseSignedJwtMono(token)
                .filter(isNotExpired)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("JWT is expired")))
                .filter(isIssuedInPast)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("JWT is not issued in past")))
                .filter(headersExist)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("JWT has wrong headers")))
                .filter(claimsExist)
                .switchIfEmpty(Mono.error(() -> new RuntimeException("JWT has wrong claims")));
    }

    private Predicate<SignedJWT> isNotExpired = token -> getExpirationDate(token).after(Date.from(Instant.now()));

    private Date getExpirationDate(SignedJWT token) {
        try {
            return token.getJWTClaimsSet().getExpirationTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Predicate<SignedJWT> isIssuedInPast = token -> getIssueDate(token).before(Date.from(Instant.now()));

    private Date getIssueDate(SignedJWT token) {
        try {
            return token.getJWTClaimsSet().getIssueTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Mono<SignedJWT> parseSignedJwtMono(String token) {
        try {
            return Mono.just(SignedJWT.parse(token));
        } catch (ParseException e) {
            return Mono.empty();
        }
    }

    private Predicate<SignedJWT> headersExist = token -> token.getHeader() != null &&
            ALL_HEADERS.containsAll(token.getHeader().toJSONObject().keySet());

    private Predicate<SignedJWT> claimsExist = token -> ALL_CLAIMS.containsAll(toClaimNamesOrEmpty(token));

    private Set<String> toClaimNamesOrEmpty(SignedJWT token) {
        try {
            return token.getJWTClaimsSet().getClaims().keySet();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Collections.emptySet();
    }

    private Predicate<JwtMerchantKeyPair> keyBelongsToMerchant = pair -> {
        var jwtMerchantName = toStringValueOrNull(pair.getSignedJWT(), CLAIM_ISS);
        var dbMerchantName = pair.getMerchantKey().getMerchantName();
        return jwtMerchantName != null && jwtMerchantName.equals(dbMerchantName);
    };

    private String toStringValueOrNull(SignedJWT token, String claimName) {
        try {
            return token.getJWTClaimsSet().getClaim(claimName).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Predicate<JwtMerchantKeyPair> signatureIsValid = this::verifySignature;

    @SneakyThrows
    private boolean verifySignature(JwtMerchantKeyPair pair) {
        var publicKey = RSAKey.parse(JSONObjectUtils.parse(pair.getMerchantKey().getPublicKey()));
        var rsaVerifier = new RSASSAVerifier(publicKey);
        return pair.getSignedJWT().verify(rsaVerifier);
    }
}
