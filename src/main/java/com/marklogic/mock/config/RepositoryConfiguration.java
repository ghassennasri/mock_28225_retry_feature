package com.marklogic.mock.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.marklogic.mock.domain"})
@EnableJpaRepositories(basePackages = {"com.marklogic.mock.repositories"})
@EnableTransactionManagement
public class RepositoryConfiguration {

}
