package com.meawallet.sdkregistry.out.cloudmessaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloudMessagingServiceProperties {

    @NotNull
    @URL
    private String registrationUrl;

    @NotNull
    private Duration timeout;

}
