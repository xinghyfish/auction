package com.xosmos.mapper;

import com.xosmos.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CustomerMapper {

    Customer queryCustomerByCustomerID(int customerID);

    Customer queryCustomerByEmail(String email);

    List<Customer> queryAllCustomers();

    int addCustomer(Customer customer);

    int updateCustomer(Customer customer);

    int deleteCustomerByID(int customerID);
}
