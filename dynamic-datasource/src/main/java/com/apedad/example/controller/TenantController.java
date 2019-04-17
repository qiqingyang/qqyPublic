package com.apedad.example.controller;

import com.apedad.example.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tenant")
public class TenantController {

    @Autowired
    TenantService tenantService;

    @GetMapping("test")
    public ResponseEntity<String> tenantTest(){
        Integer tenantNum = tenantService.queryTenantCount();
        Integer storyNum = tenantService.queryStoryCount();
        Integer dataSetNum = tenantService.queryDataSetCount();
        String resultStr="租户个数："+tenantNum.intValue()+"，故事板数量："+storyNum.intValue()+"，数据集数量："+dataSetNum.intValue();
        System.out.println(resultStr);
        return new ResponseEntity<>(resultStr, HttpStatus.OK );
    }
}
