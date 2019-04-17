package com.apedad.example.commons;

import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author qiqy [qiqy@yonyou.com]
 * @version 1.0
 */
public class DynamicDataSourceContextHolder {
    private static final Logger LOG = Logger.getLogger(DynamicDataSourceContextHolder.class);

    private static final ThreadLocal<DataSourceKey> currentDatesourceKey = new ThreadLocal<>();
    private static final ThreadLocal<Map<Object, Object>> currentDatesource = new ThreadLocal<>();

    /**
     * 清除当前数据源key
     */
    public static void clear() {
        currentDatesourceKey.remove();
    }

    /**
     * 获取当前使用的数据源
     *
     * @return 当前使用数据源的ID
     */
    public static DataSourceKey get() {
        return currentDatesourceKey.get();
    }

    /**
     * 设置当前使用的数据源
     *
     * @param value 需要设置的数据源ID
     */
    public static void set(DataSourceKey value) {
        currentDatesourceKey.set(value);
    }

    /**
     * 设置当前使用的数据源
     *
     * @param dataSourceMap 需要设置的数据源map
     */
    public static void setDataSourceMap(Map<Object, Object> dataSourceMap){
        currentDatesource.set(dataSourceMap);
    }

    /**
     * 获取当前使用的数据源
     *
     * @return 当前使用数据源
     */
    public static Map<Object, Object> getDataSourceMap(){
        return currentDatesource.get();
    }

    /**
     * 清除当前数据源
     */
    public static void clearDatasourceMap() {
        currentDatesource.remove();
    }
}
