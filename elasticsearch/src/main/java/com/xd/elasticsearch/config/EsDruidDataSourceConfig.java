package com.xd.elasticsearch.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class EsDruidDataSourceConfig {
    @Value("${spring.datasource.es.configLocation}")
    private String configLocation;

    @Value("${spring.datasource.es.mapperLocations}")
    private String bigdataMapperLocations;

    @Value("${spring.datasource.es.url}")
    private String esUrl;

    @Bean(name = "esDataSource")
    public DataSource esDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        //dataSource.setDriverClassName("org.elasticsearch.xpack.sql.jdbc.jdbc.JdbcDriver");
        dataSource.setDriverClassName("org.elasticsearch.xpack.sql.jdbc.EsDriver");
        dataSource.setUrl(esUrl);
//        dataSource.setUsername("elastic");
//        dataSource.setPassword("J+VukdrFiW66CGECuZQZ");
        return dataSource;
    }

    /**
     * SqlSessionFactory配置
     *
     * @return
     * @throws Exception
     */
    @Bean(name = "esSqlSessionFactory")
    public SqlSessionFactory bigdataSqlSessionFactory(@Qualifier("esDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //配置mapper文件位置
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(bigdataMapperLocations));
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource(configLocation));
        return sqlSessionFactoryBean.getObject();
    }

}
