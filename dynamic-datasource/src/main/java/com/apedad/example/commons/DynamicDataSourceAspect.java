package com.apedad.example.commons;

import com.alibaba.druid.pool.DruidDataSource;
import com.apedad.example.annotation.TargetDataSource;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qiqy [qiqy@yonyou.com]
 * @version 1.0
 */
@Aspect
@Order(-1)
@Component
public class DynamicDataSourceAspect {
    private static final Logger LOG = Logger.getLogger(DynamicDataSourceAspect.class);

    @Qualifier(value = "dynamicDatasource")
    @Autowired
    DataSource dynamicDatasource;



    @Pointcut("execution(* com.apedad.example.service..*.*(..))")
    public void pointCut() {
    }

    /**
     * 执行方法前更换数据源
     *
     * @param joinPoint        切点
     * @param targetDataSource 动态数据源
     */
    @Before("@annotation(targetDataSource)")
    public void doBefore(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        DataSourceKey dataSourceKey = targetDataSource.dataSourceKey();
        String dbName = targetDataSource.dbName();
        if (dataSourceKey == DataSourceKey.DB_DYNAMIC) {
            LOG.info(String.format("设置数据源为  %s", DataSourceKey.DB_DYNAMIC));
            createDatasource(dbName,DataSourceKey.DB_DYNAMIC);
        } else {
            LOG.info(String.format("使用默认数据源  %s", DataSourceKey.DB_SPRING));
        }
    }

    /**
     * 创建数据源
     * @param dbName dbname
     * @param dataSourceKey 数据源的key
     */
    private void createDatasource(String dbName,DataSourceKey dataSourceKey) {
        DruidDataSource druidDataSource = new DruidDataSource();
        DruidDataSource dynamicDatasource = (DruidDataSource) this.dynamicDatasource;
        String url = (dynamicDatasource.getUrl());
        String targetUrl=String.format(url, dbName);
        druidDataSource.setUrl(targetUrl);
        druidDataSource.setUsername(dynamicDatasource.getUsername());
        druidDataSource.setPassword(dynamicDatasource.getPassword());
        Map<Object, Object> dataSourceMap = new HashMap<>(1);
        dataSourceMap.put(DataSourceKey.DB_DYNAMIC, druidDataSource);
        DynamicDataSourceContextHolder.setDataSourceMap(dataSourceMap);
        DynamicDataSourceContextHolder.set(dataSourceKey);
    }

    /**
     * 执行方法后清除数据源设置
     *
     * @param joinPoint        切点
     * @param targetDataSource 动态数据源
     */
    @After("@annotation(targetDataSource)")
    public void doAfter(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        LOG.info(String.format("当前数据源  %s  执行清理方法", targetDataSource.dataSourceKey()));
        DynamicDataSourceContextHolder.clear();
        DynamicDataSourceContextHolder.clearDatasourceMap();
        LOG.info(String.format("当前数据源  %s  已被清理", targetDataSource.dataSourceKey()));
    }

    @Before(value = "pointCut()")
    public void doBeforeWithSlave(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取当前切点方法对象
        Method method = methodSignature.getMethod();
        //判断是否为接口方法
        if (method.getDeclaringClass().isInterface()) {
            try {
                //获取实际类型的方法对象
                method = joinPoint.getTarget().getClass()
                        .getDeclaredMethod(joinPoint.getSignature().getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                LOG.error("方法不存在！", e);
            }
        }
        if (null == method.getAnnotation(TargetDataSource.class)) {
            DynamicDataSourceContextHolder.set(DataSourceKey.DB_SPRING);
        }
    }
}
