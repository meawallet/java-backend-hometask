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
@Table(Tables.SDK_MERCHANT_KEY)
public class SdkMerchantKey {

    @Id
    private Long id;

    @Column("merchant_name")
    private String merchantName;

    @Column("public_key")
    private String publicKey;

    @Column("kid")
    private String kid;
}
