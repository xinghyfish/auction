package com.xosmos.service.impl;

import com.xosmos.entity.Admin;
import com.xosmos.mapper.AdminMapper;
import com.xosmos.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin queryAdminByAdminID(String adminID) {
        return adminMapper.queryAdminByAdminID(adminID);
    }
}
