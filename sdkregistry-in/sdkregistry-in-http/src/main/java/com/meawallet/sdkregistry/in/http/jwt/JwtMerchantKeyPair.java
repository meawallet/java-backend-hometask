package com.meawallet.sdkregistry.in.http.jwt;

import com.meawallet.sdkregistry.api.dto.merchantKey.MerchantKey;
import com.nimbusds.jwt.SignedJWT;
import lombok.Value;

@Value
public class JwtMerchantKeyPair {

    SignedJWT signedJWT;
    MerchantKey merchantKey;
}
