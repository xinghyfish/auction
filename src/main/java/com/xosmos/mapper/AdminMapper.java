package com.xosmos.mapper;

import com.xosmos.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AdminMapper {

    Admin queryAdminByAdminID(String adminID);
}
