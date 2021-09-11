package com.meawallet.sdkregistry.model.entities;

import com.meawallet.sdkregistry.model.Tables;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Table(Tables.SDK_INSTANCE)
@AllArgsConstructor
@NoArgsConstructor
public class SdkInstanceEntity {
    @Id
    private Long id;

    @Column("sdk_instance_id")
    private String sdkInstanceId;

    @Column("sdk_id")
    private String sdkId;

    @Column("fcm_registration_id")
    private String fcmRegistrationId;

    @Column("latitude")
    private BigDecimal latitude;

    @Column("longitude")
    private BigDecimal longitude;

    @Column("created_at")
    @EqualsAndHashCode.Exclude
    private ZonedDateTime createdAt;

    public SdkInstanceEntity(String sdkInstanceId,
                             String sdkId,
                             String fcmRegistrationId,
                             BigDecimal latitude,
                             BigDecimal longitude) {
        this.sdkInstanceId = sdkInstanceId;
        this.sdkId = sdkId;
        this.fcmRegistrationId = fcmRegistrationId;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
