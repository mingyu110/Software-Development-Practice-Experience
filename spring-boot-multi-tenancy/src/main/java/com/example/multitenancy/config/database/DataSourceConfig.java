package com.example.multitenancy.config.database;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final DataSourcePropertiesConfig dataSourcePropertiesConfig;

    @Bean
    @Primary
    public DataSource routingDataSource() {
        return RoutingDataSource.of(dataSourcePropertiesConfig.getDataSources());
    }
}
