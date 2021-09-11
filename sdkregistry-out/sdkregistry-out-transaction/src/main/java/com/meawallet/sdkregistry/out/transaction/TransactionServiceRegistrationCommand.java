package com.meawallet.sdkregistry.out.transaction;

import lombok.Value;

@Value
public class TransactionServiceRegistrationCommand {

    String sdkInstanceId;
    String encKey;
    String macKey;

}
