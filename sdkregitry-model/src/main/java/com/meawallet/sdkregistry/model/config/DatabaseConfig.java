package com.meawallet.sdkregistry.model.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(value = "com.meawallet.sdkregistry.model.repositories")
class DatabaseConfig {
}
