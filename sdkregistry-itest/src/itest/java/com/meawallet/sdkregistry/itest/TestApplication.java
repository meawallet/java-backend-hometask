package com.meawallet.sdkregistry.itest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(
        scanBasePackages = "com.meawallet",
        exclude = {
                LiquibaseAutoConfiguration.class,
                DataSourceAutoConfiguration.class
        }
)
public class TestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(TestApplication.class).run(args);
    }

}
