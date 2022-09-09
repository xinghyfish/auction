package com.xosmos.controller;

import com.xosmos.entity.Customer;
import com.xosmos.service.CustomerService;
import com.xosmos.utils.EncryptUtils;
import com.xosmos.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/login")
    public String login() {
        return "customer/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody Customer customer) {
        String plaintext = customer.getPwd();
        String ciphertext = EncryptUtils.encrypt(plaintext);
        customer.setPwd(ciphertext);
        Customer queryCustomer = customerService.queryCustomerByEmail(customer.getEmail());
        if (queryCustomer == null) {
            return JSONUtils.getJSONString(1, "邮箱不存在，请先进行注册！");
        } else if (queryCustomer.getPwd().equals(customer.getPwd())) {
            return JSONUtils.getJSONString(2, "密码错误，请重新输入！");
        } else {
            return JSONUtils.getJSONString(0);
        }
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/customer/login";
    }

    @GetMapping(value = "/register")
    public String register() {
        return "customer/register";
    }

    @PostMapping(value = "/register")
    @ResponseBody
    public String register(Customer customer) {
        String email = customer.getEmail();
        Customer queryCustomer = customerService.queryCustomerByEmail(email);
        if (queryCustomer != null) {
            return JSONUtils.getJSONString(1, "邮箱已经被注册");
        } else {
            return JSONUtils.getJSONString(0);
        }
    }

    @GetMapping("/center")
    public String center() {
        return "customer/ceter";
    }
}
