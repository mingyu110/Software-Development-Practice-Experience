package com.example.multitenancy.config.database;

import com.example.multitenancy.config.TenantContext;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class RoutingDataSource {

    private RoutingDataSource() {}

    public static DataSource of(List<TenantAwareDataSourceProperties> propertiesList) {
        return new RoutingDataSource().getAbstractRoutingDataSource(propertiesList);
    }

    private AbstractRoutingDataSource getAbstractRoutingDataSource(List<TenantAwareDataSourceProperties> propertiesList) {
        AbstractRoutingDataSource routingDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return TenantContext.getTenantId();
            }
        };

        Map<Object, Object> targetDataSources = new HashMap<>();
        HikariDataSource defaultDataSource = null;

        for (var props : propertiesList) {
            log.info("Setting up datasource for tenants: {}", props.getTenants());
            HikariDataSource dataSource = props.initializeDataSourceBuilder().type(HikariDataSource.class).build();
            dataSource.setMinimumIdle(props.getMinimumIdle());
            dataSource.setMaximumPoolSize(props.getMaximumPoolSize());

            // Manually trigger Flyway migration for each tenant's datasource
            runFlywayMigration(dataSource);

            for (var tenantId : props.getTenants()) {
                log.info("Wiring tenant ID {} to {}", tenantId, props.getUrl());
                targetDataSources.put(tenantId, dataSource);
            }

            if (Objects.isNull(defaultDataSource)) {
                defaultDataSource = dataSource;
            }
        }

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(defaultDataSource);
        routingDataSource.afterPropertiesSet();

        return routingDataSource;
    }

    private void runFlywayMigration(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration") // Common migration scripts
                .load();
        flyway.migrate();
    }
}
