package com.apedad.example.service.impl;

import com.apedad.example.annotation.TargetDataSource;
import com.apedad.example.commons.DataSourceKey;
import com.apedad.example.dao.TenantMapper;
import com.apedad.example.service.TenantService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author qiqy [qiqy@yonyou.com]
 * @version 1.0
 */
@Service("userInfoService")
public class TenantServiceImpl implements TenantService {
    private static final Logger LOG = Logger.getLogger(TenantServiceImpl.class);
    @Resource
    private TenantMapper tenantMapper;

    @Override
    public Integer queryTenantCount() {
        return tenantMapper.queryTenantCount();
    }

    @TargetDataSource(dataSourceKey = DataSourceKey.DB_DYNAMIC,dbName = "db0000i")
    @Override
    public Integer queryStoryCount() {
        return tenantMapper.queryStoryCount();
    }



    @TargetDataSource(dataSourceKey = DataSourceKey.DB_DYNAMIC,dbName = "db0000k")
    @Override
    public Integer queryDataSetCount() {
        return tenantMapper.queryDataSetCount();
    }
}
