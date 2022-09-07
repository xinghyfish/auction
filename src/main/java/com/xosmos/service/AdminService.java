package com.xosmos.service;

import com.xosmos.entity.Admin;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {

    Admin queryAdminByAdminID(String adminID);
}
