package com.xosmos.service;

import com.xosmos.entity.Admin;
import com.xosmos.entity.Auction;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {

    Admin queryAdminByAdminID(String adminID);
}
