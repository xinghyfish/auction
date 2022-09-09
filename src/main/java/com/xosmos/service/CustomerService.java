package com.xosmos.service;

import com.xosmos.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {

    Customer queryCustomerByCustomerID(int customerID);

    Customer queryCustomerByEmail(String email);

    List<Customer> queryAllCustomers();

    int addCustomer(Customer customer);

    int updateCustomer(Customer customer);

    int deleteCustomerByID(int customerID);
}
