package com.jk.ndt.etl.controller;

import com.alibaba.fastjson.JSONObject;
import com.jk.ndt.etl.SpringTest;
import com.jk.ndt.etl.entity.system.Admin;
import com.jk.ndt.etl.entity.system.Resource;
import com.jk.ndt.etl.service.system.AdminService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by 朱生 on 2017/5/23.
 */
public class AdminTest extends SpringTest {


    @Autowired
    private AdminService adminService;

    @Test
    public void listResource() {
        List<Resource> list=adminService.listResource();
        System.out.println(JSONObject.toJSON(list));
        System.out.println();
    }

    @Test
    public void getName() {
        Admin queryAdmin= adminService.getByName("admin");
    }

}
