/*
 * Copyright (C) 2016 Christoph Wurst <christoph@winzerhof-wurst.at>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.christophwurst.orm.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
@Configuration
@EnableAspectJAutoProxy
@EnableJpaRepositories(basePackages = "at.christophwurst.orm.dao")
@ComponentScan(basePackages = "at.christophwurst.orm")
public class AppConfig {

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.apache.derby.jdbc.ClientDriver");
		dataSource.setUrl("jdbc:derby://localhost/WorkLogDb;create=true");
		dataSource.setUsername("user");
		dataSource.setPassword("test");
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource());
		entityManagerFactoryBean.setPackagesToScan("at.christophwurst.orm.domain");
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactoryBean.setJpaProperties(testProperties());
		return entityManagerFactoryBean;
	}

	@Bean
	public JpaTransactionManager transactionManager() {
		return new JpaTransactionManager(entityManagerFactory().getObject());
	}

	private Properties testProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", "org.hibernate.dialect.DerbyTenSevenDialect");
		properties.put("hibernate.show_sql", "false");
		properties.put("hibernate.format_sql", "false");
		properties.put("hibernate.hbm2ddl.auto", "create");
		return properties;
	}

}
