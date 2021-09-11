package com.meawallet.sdkregistry.model.entities;

import com.meawallet.sdkregistry.model.Tables;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;

@Data
@Table(Tables.SDK_INSTANCE_KEYS)
@AllArgsConstructor
@NoArgsConstructor
public class SdkInstanceKeysEntity {

    @Id
    private Long id;

    @Column("sdk_instance_id")
    private String sdkInstanceId;

    @Column("keyset_id")
    private String keySetId;

    @Column("push_data_enc_key")
    private String pushDataEncKey;

    @Column("push_data_mac_key")
    private String pushDataMacKey;

    @Column("push_message_mac_key")
    private String pushMessageMacKey;

    @Column("attestation_enc_key")
    private String attestationEncKey;

    @Column("attestation_mac_key")
    private String attestationMacKey;

    @Column("transaction_enc_key")
    private String transactionEncKey;

    @Column("transaction_mac_key")
    private String transactionMacKey;

    @Column("secret_key")
    private String secretKey;

    @Column("secret_key_fingerprint")
    private String secretKeyFingerprint;

    @Column("is_active")
    private boolean active;

    @Column("created_at")
    @EqualsAndHashCode.Exclude
    private ZonedDateTime createdAt;

    @Column("updated_at")
    @EqualsAndHashCode.Exclude
    private ZonedDateTime updatedAt;

    public SdkInstanceKeysEntity(String sdkInstanceId,
                                 String keySetId,
                                 String pushDataEncKey,
                                 String pushDataMacKey,
                                 String pushMessageMacKey,
                                 String attestationEncKey,
                                 String attestationMacKey,
                                 String transactionEncKey,
                                 String transactionMacKey,
                                 String secretKey,
                                 String secretKeyFingerprint) {
        this.sdkInstanceId = sdkInstanceId;
        this.keySetId = keySetId;
        this.pushDataEncKey = pushDataEncKey;
        this.pushDataMacKey = pushDataMacKey;
        this.pushMessageMacKey = pushMessageMacKey;
        this.attestationEncKey = attestationEncKey;
        this.attestationMacKey = attestationMacKey;
        this.transactionEncKey = transactionEncKey;
        this.transactionMacKey = transactionMacKey;
        this.secretKey = secretKey;
        this.secretKeyFingerprint = secretKeyFingerprint;
    }
}
