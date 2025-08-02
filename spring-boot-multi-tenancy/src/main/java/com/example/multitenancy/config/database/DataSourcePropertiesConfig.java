package com.example.multitenancy.config.database;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties
@Data
public class DataSourcePropertiesConfig {
    private List<TenantAwareDataSourceProperties> dataSources;
}
