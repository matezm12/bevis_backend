package com.bevis.appbe.util;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;

public final class SpringLiquibaseUtil {
    private SpringLiquibaseUtil() {
    }

    public static SpringLiquibase createSpringLiquibase(DataSource liquibaseDatasource, LiquibaseProperties liquibaseProperties, DataSource dataSource, DataSourceProperties dataSourceProperties) {
        DataSource liquibaseDataSource = getDataSource(liquibaseDatasource, liquibaseProperties, dataSource);
        if (liquibaseDataSource != null) {
            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setDataSource(liquibaseDataSource);
            return liquibase;
        } else {
            SpringLiquibase liquibase = new DataSourceClosingSpringLiquibase();
            liquibase.setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties));
            return liquibase;
        }
    }

    public static SpringLiquibase createAsyncSpringLiquibase(Environment env, Executor executor, DataSource liquibaseDatasource, LiquibaseProperties liquibaseProperties, DataSource dataSource, DataSourceProperties dataSourceProperties) {
        DataSource liquibaseDataSource = getDataSource(liquibaseDatasource, liquibaseProperties, dataSource);
        AsyncSpringLiquibase liquibase;
        if (liquibaseDataSource != null) {
            liquibase = new AsyncSpringLiquibase(executor, env);
            ((AsyncSpringLiquibase)liquibase).setCloseDataSourceOnceMigrated(false);
            liquibase.setDataSource(liquibaseDataSource);
            return liquibase;
        } else {
            liquibase = new AsyncSpringLiquibase(executor, env);
            liquibase.setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties));
            return liquibase;
        }
    }

    private static DataSource getDataSource(DataSource liquibaseDataSource, LiquibaseProperties liquibaseProperties, DataSource dataSource) {
        if (liquibaseDataSource != null) {
            return liquibaseDataSource;
        } else {
            return liquibaseProperties.getUrl() == null && liquibaseProperties.getUser() == null ? dataSource : null;
        }
    }

    private static DataSource createNewDataSource(LiquibaseProperties liquibaseProperties, DataSourceProperties dataSourceProperties) {
        Objects.requireNonNull(liquibaseProperties);
        Supplier var10000 = liquibaseProperties::getUrl;
        Objects.requireNonNull(dataSourceProperties);
        String url = getProperty(var10000, dataSourceProperties::determineUrl);
        Objects.requireNonNull(liquibaseProperties);
        var10000 = liquibaseProperties::getUser;
        Objects.requireNonNull(dataSourceProperties);
        String user = getProperty(var10000, dataSourceProperties::determineUsername);
        Objects.requireNonNull(liquibaseProperties);
        var10000 = liquibaseProperties::getPassword;
        Objects.requireNonNull(dataSourceProperties);
        String password = getProperty(var10000, dataSourceProperties::determinePassword);
        return DataSourceBuilder.create().url(url).username(user).password(password).build();
    }

    private static String getProperty(Supplier<String> property, Supplier<String> defaultValue) {
        String value = (String)property.get();
        return value != null ? value : (String)defaultValue.get();
    }
}

