package com.example.multitenancy.config.database;

import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.util.List;

@Data
public class TenantAwareDataSourceProperties extends DataSourceProperties {
    private List<String> tenants;
    private Integer minimumIdle;
    private Integer maximumPoolSize;
}
