package com.roey.ocr.configure;//package com.zy.superloan.backgroup.dao;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * mybatis配置类
 *
 * @author fuaokai
 * @version 1.0
 * @date 2018-10-17 19:11
 * @since jdk1.8
 */
@Configuration
@MapperScan(basePackages = "com.donglin.jyy.scan.dao", sqlSessionTemplateRef = "basicSqlSessionTemplate")
public class ScanMyBatisConfig {

    @Bean
    public SqlSessionFactory basicSqlSessionFactory(@Qualifier("basicDataSource") DataSource basicDataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(basicDataSource);

        try {
            // 配置分页插件，详情请查阅官方文档
            PageInterceptor interceptor = new PageInterceptor();
            Properties properties = new Properties();
            properties.setProperty("pageSizeZero", "true");
            properties.setProperty("reasonable", "true");
            properties.setProperty("supportMethodsArguments", "true");
            interceptor.setProperties(properties);

            // 添加插件
            sqlSessionFactoryBean.setPlugins(new Interceptor[] { interceptor, new SqlPrintInterceptor()});
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources("classpath:com/donglin/jyy/basic/dao/mapper/*.xml"));
            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Primary
    @Bean(name = "basicDataSource")
    @ConfigurationProperties(prefix = "environment.datasource.jyy-db")
    public DataSource basicDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public SqlSessionTemplate basicSqlSessionTemplate(@Qualifier("basicSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
        return sqlSessionTemplate;
    }

}
