package com.meawallet.sdkregistry.model.entities;

import com.meawallet.sdkregistry.model.Tables;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(Tables.SDK_SHARED_KEY)
public class SdkSharedKeyEntity {
    @Id
    private Long id;

    @Column("fingerprint")
    private String fingerprint;

    @Column("shared_aes_key_hex")
    private String sharedAesKeyHex;

}
