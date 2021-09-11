package com.meawallet.sdkregistry.model.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExtendedLiquibaseProperties extends LiquibaseProperties {

    private boolean closeDatasource = true;

}
