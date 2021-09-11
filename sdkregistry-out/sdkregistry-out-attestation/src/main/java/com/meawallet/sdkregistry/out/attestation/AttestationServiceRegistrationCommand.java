package com.meawallet.sdkregistry.out.attestation;

import lombok.Value;

@Value
public class AttestationServiceRegistrationCommand {

    String sdkInstanceId;
    String publicKeyDer;
    String encKey;
    String macKey;

}
