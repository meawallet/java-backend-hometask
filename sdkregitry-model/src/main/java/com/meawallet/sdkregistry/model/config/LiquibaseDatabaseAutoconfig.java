package com.meawallet.sdkregistry.model.config;

import liquibase.integration.spring.SpringLiquibase;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(ExtendedLiquibaseProperties.class)
@AllArgsConstructor
public class LiquibaseDatabaseAutoconfig {

    private final ExtendedLiquibaseProperties liquibaseProperties;

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = this.liquibaseProperties.isCloseDatasource()
                                    ? new DataSourceClosingSpringLiquibase()
                                    : new SpringLiquibase();
        liquibase.setDataSource(liquibaseDataSource());
        liquibase.setChangeLog(this.liquibaseProperties.getChangeLog());
        liquibase.setContexts(this.liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(this.liquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(this.liquibaseProperties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(this.liquibaseProperties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogTable(this.liquibaseProperties.getDatabaseChangeLogTable());
        liquibase.setDatabaseChangeLogLockTable(this.liquibaseProperties.getDatabaseChangeLogLockTable());
        liquibase.setDropFirst(this.liquibaseProperties.isDropFirst());
        liquibase.setShouldRun(this.liquibaseProperties.isEnabled());
        liquibase.setLabels(this.liquibaseProperties.getLabels());
        liquibase.setChangeLogParameters(this.liquibaseProperties.getParameters());
        liquibase.setRollbackFile(this.liquibaseProperties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(this.liquibaseProperties.isTestRollbackOnUpdate());
        liquibase.setClearCheckSums(this.liquibaseProperties.isClearChecksums());
        liquibase.setTag(this.liquibaseProperties.getTag());
        return liquibase;
    }

    private DataSource liquibaseDataSource() {
        return DataSourceBuilder.create()
                                .driverClassName(liquibaseProperties.getDriverClassName())
                                .url(liquibaseProperties.getUrl())
                                .username(liquibaseProperties.getUser())
                                .password(liquibaseProperties.getPassword())
                                .build();
    }
}
