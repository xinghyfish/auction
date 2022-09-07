package com.xosmos;

import com.xosmos.service.AdminService;
import com.xosmos.service.impl.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminTests {

    @Autowired
    private AdminService adminService;

    @Test
    public void login() {
        System.out.println(adminService.queryAdminByAdminID("admin0001"));
    }
}
