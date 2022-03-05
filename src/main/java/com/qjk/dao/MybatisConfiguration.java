package com.qjk.dao;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration   //当前类相当于配置文件
public class MybatisConfiguration {
    @Bean  //用于方法的上方，返回一个对象，把返回的对象存于SpringBean工厂中
    @ConditionalOnMissingBean //条件Bean  ,方法的参数，
    //如果得到返回值的bean，必须关联参数bean
    public SqlSessionFactoryBean createSqlSessionFactoryBean(DataSource dataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        //设置数据源
        sqlSessionFactoryBean.setDataSource(dataSource);
        //设置配置文件的路径
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
        //读取映射文件
        PathMatchingResourcePatternResolver resolver=new PathMatchingResourcePatternResolver();
        //声明数组，存放读取到的文件
        Resource[] mapperXml=null;
        //读取
        try {
            mapperXml=resolver.getResources("classpath:mybatis/mapper/*.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //把读取到的映射文件赋值给sqlSessionFactoryBean
        sqlSessionFactoryBean.setMapperLocations(mapperXml);
        //设置别名
        sqlSessionFactoryBean.setTypeAliasesPackage("com.qjk.pojo");
        return sqlSessionFactoryBean;
    }
    @Bean
    @ConditionalOnBean(SqlSessionFactoryBean.class)
    public MapperScannerConfigurer createMapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer=new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.qjk.dao");
        return mapperScannerConfigurer;
    }
}
