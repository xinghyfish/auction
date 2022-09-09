package com.xosmos.service.impl;

import com.xosmos.entity.Customer;
import com.xosmos.mapper.CustomerMapper;
import com.xosmos.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public Customer queryCustomerByCustomerID(int customerID) {
        return customerMapper.queryCustomerByCustomerID(customerID);
    }

    @Override
    public Customer queryCustomerByEmail(String email) {
        return customerMapper.queryCustomerByEmail(email);
    }

    @Override
    public List<Customer> queryAllCustomers() {
        return customerMapper.queryAllCustomers();
    }

    @Override
    public int addCustomer(Customer customer) {
        return customerMapper.addCustomer(customer);
    }

    @Override
    public int updateCustomer(Customer customer) {
        return customerMapper.updateCustomer(customer);
    }

    @Override
    public int deleteCustomerByID(int customerID) {
        return customerMapper.deleteCustomerByID(customerID);
    }
}
