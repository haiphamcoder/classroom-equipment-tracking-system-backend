package com.classroom.equipment.utils.mysql;

import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@UtilityClass
public class DataSourceUtils {
    public static DataSource getDataSource(DataSourceProperties dataSourceProperties,
                                           long connectionTimeout,
                                           String transactionIsolation,
                                           int minimumIdle,
                                           long idleTimeout,
                                           int maximumPoolSize,
                                           boolean autoCommit) {
        final HikariDataSource dataSource = dataSourceProperties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();

        dataSource.setConnectionTimeout(connectionTimeout);
        dataSource.setTransactionIsolation(transactionIsolation);
        dataSource.setMinimumIdle(minimumIdle);
        dataSource.setIdleTimeout(idleTimeout);
        dataSource.setMaximumPoolSize(maximumPoolSize);
        dataSource.setAutoCommit(autoCommit);
        return dataSource;
    }

    public static LocalContainerEntityManagerFactoryBean getEntityManagerFactoryBean(EntityManagerFactoryBuilder builder, DataSource dataSource, String packages) {
        return builder.dataSource(dataSource).packages(packages).build();
    }

    public static PlatformTransactionManager getTransactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactoryBean.getObject()));
    }
}
