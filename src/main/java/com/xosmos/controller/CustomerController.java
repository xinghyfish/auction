package com.xosmos.controller;

import com.xosmos.entity.AuctionRecord;
import com.xosmos.entity.Customer;
import com.xosmos.service.AuctionRecordService;
import com.xosmos.service.CustomerService;
import com.xosmos.utils.EncryptUtils;
import com.xosmos.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private AuctionRecordService auctionRecordService;

    @GetMapping("/login")
    public String login() {
        return "customer/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody Customer customer, HttpServletRequest request) {
        String plaintext = customer.getPwd();
        String ciphertext = EncryptUtils.encrypt(plaintext);
        customer.setPwd(ciphertext);
        Customer queryCustomer = customerService.queryCustomerByEmail(customer.getEmail());
        if (queryCustomer == null) {
            return JSONUtils.getJSONString(1, "邮箱不存在，请先进行注册！");
        } else if (queryCustomer.getPwd().equals(customer.getPwd())) {
            return JSONUtils.getJSONString(2, "密码错误，请重新输入！");
        } else {
            System.out.println(queryCustomer);
            request.setAttribute("customerName", queryCustomer.getCustomerName());
            request.setAttribute("customerID", queryCustomer.getCustomerName());
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
    public String register(@RequestBody Customer customer, HttpServletRequest request) {
        System.out.println(customer);
        String email = customer.getEmail();
        Customer queryCustomer = customerService.queryCustomerByEmail(email);
        if (queryCustomer != null) {
            return JSONUtils.getJSONString(1, "邮箱已经被注册");
        } else {
            // 注册用户
            customerService.addCustomer(customer);
            return JSONUtils.getJSONString(0);
        }
    }

    @GetMapping("/center")
    public String center(Model model) {
        return "customer/info";
    }

    @GetMapping("/info")
    public String info(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer customerID = (Integer) session.getAttribute("customerID");
        Customer customer = customerService.queryCustomerByCustomerID(customerID);
        model.addAttribute("customer", customer);
        return "customer/info";
    }

    @GetMapping("/auction-records")
    public String auction_records(Model model) {
        model.addAttribute("cite", "/auction-records-iframe");
        return "customer/center";
    }

    @GetMapping("/auction-records-iframe")
    private String auction_records_iframe(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String customerName = (String) session.getAttribute("customerName");
        Integer customerID = (Integer) session.getAttribute("customerID");
        model.addAttribute("customerName", customerName);
        List<AuctionRecord> auctionRecords = auctionRecordService.queryAuctionRecordByCustomerID(customerID);
        model.addAttribute("auctionRecords", auctionRecords);
        return "customer/auction-records";
    }

    @GetMapping("/update-info")
    public String update_info(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String customerName = (String) session.getAttribute("customerName");
        model.addAttribute("customerName", customerName);
        return "customer/update-info";
    }

    @PostMapping("/update-info")
    public String update_info(@RequestBody Customer customer, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer customerID = (Integer) session.getAttribute("customerID");
        Customer old_customer = customerService.queryCustomerByCustomerID(customerID);
        old_customer.setCustomerName(customer.getCustomerName());
        old_customer.setPwd(customer.getPwd());
        customerService.updateCustomer(old_customer);

        return "redirect:/customer/info";
    }
}
