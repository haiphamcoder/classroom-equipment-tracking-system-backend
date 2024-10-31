package com.classroom.equipment.config.mysql;

import com.classroom.equipment.utils.mysql.DataSourceUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "equipmentTrackingEntityManagerFactory",
        transactionManagerRef = "equipmentTrackingTransactionManager",
        basePackages = {"com.classroom.equipment.dao"}
)
public class MySqlEquipmentTrackingConfiguration {
    private final long connectionTimeout;
    private final int minimumIdle;
    private final long idleTimeout;
    private final int maximumPoolSize;

    public MySqlEquipmentTrackingConfiguration(@Value("${jpa.conn-time-out}") long connectionTimeout,
                                               @Value("${jpa.min-idle}") int minimumIdle,
                                               @Value("${jpa.idle-time-out}") long idleTimeout,
                                               @Value("${jpa.max-pool-size}") int maximumPoolSize) {
        this.connectionTimeout = connectionTimeout;
        this.minimumIdle = minimumIdle;
        this.idleTimeout = idleTimeout;
        this.maximumPoolSize = maximumPoolSize;
    }

    @Bean("equipmentTrackingProperties")
    @ConfigurationProperties(prefix = "equipment.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("equipmentTrackingDataSource")
    public DataSource getDataSource() {
        return DataSourceUtils.getDataSource(dataSourceProperties(),
                connectionTimeout,
                "TRANSACTION_READ_COMMITTED",
                minimumIdle,
                idleTimeout,
                maximumPoolSize,
                true);
    }

    @Bean(name = "equipmentTrackingEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
        return DataSourceUtils.getEntityManagerFactoryBean(builder, getDataSource(), "com.classroom.equipment.entity");
    }

    @Bean(name = "equipmentTrackingTransactionManager")
    public PlatformTransactionManager transactionManager(final @Qualifier("equipmentTrackingEntityManagerFactory")
                                                         LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return DataSourceUtils.getTransactionManager(entityManagerFactoryBean);
    }
}
