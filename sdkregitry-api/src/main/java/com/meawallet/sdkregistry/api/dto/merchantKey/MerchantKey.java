package com.meawallet.sdkregistry.api.dto.merchantKey;

import lombok.Value;

@Value
public class MerchantKey {

    Long id;
    String merchantName;
    String publicKey;
    String kid;

}
