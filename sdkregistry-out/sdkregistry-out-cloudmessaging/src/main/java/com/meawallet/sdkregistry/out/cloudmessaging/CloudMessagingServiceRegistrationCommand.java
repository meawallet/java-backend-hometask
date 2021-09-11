package com.meawallet.sdkregistry.out.cloudmessaging;

import lombok.Value;

@Value
public class CloudMessagingServiceRegistrationCommand {

    String sdkInstanceId;
    String fcmRegistrationId;
    String dataEncKey;
    String dataMacKey;
    String messageMacKey;

}
