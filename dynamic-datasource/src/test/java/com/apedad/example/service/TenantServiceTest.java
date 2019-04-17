package com.apedad.example.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qiqy [qiqy@yonyou.com]
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TenantServiceTest {
    private static final Logger LOG = Logger.getLogger(TenantServiceTest.class);

    @Autowired
    private TenantService tenantService;

    @Transactional
    @Test
    public void testTenant(){
        int tenantCount = tenantService.queryTenantCount();
        System.out.println("租户数量："+tenantCount);
    }

    @Transactional
    @Test
    public void testStory(){
        int storyCount = tenantService.queryStoryCount();
        System.out.println("故事板数量："+storyCount);
    }
}
