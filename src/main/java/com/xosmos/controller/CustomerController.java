package com.xosmos.controller;

import com.xosmos.entity.*;
import com.xosmos.service.AuctionRecordService;
import com.xosmos.service.AuctionService;
import com.xosmos.service.AuctionVenueService;
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
    @Autowired
    private AuctionVenueService auctionVenueService;
    @Autowired
    private AuctionService auctionService;

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
        } else if (!queryCustomer.getPwd().equals(customer.getPwd())) {
            return JSONUtils.getJSONString(2, "密码错误，请重新输入！");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("customerName", queryCustomer.getCustomerName());
            session.setAttribute("customerID", queryCustomer.getCustomerID());
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
            customer.setPwd(EncryptUtils.encrypt(customer.getPwd()));
            // 注册用户
            customerService.addCustomer(customer);
            return JSONUtils.getJSONString(0);
        }
    }

    @GetMapping("/center")
    public String center(HttpServletRequest request, Model model) {
        model.addAttribute("cite", "info");
        return "customer/center";
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
        model.addAttribute("cite", "auction-records-iframe");
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
    public String update_info(Model model) {
        model.addAttribute("cite", "update-info-iframe");
        return "customer/center";
    }

    @GetMapping("/update-info-iframe")
    public String update_info_iframe(Model model, HttpServletRequest request) {
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

    @GetMapping("/auction-venue-list")
    public String auction_venue_list(Model model) {
        model.addAttribute("cite", "auction-venue-list-iframe");
        return "customer/center";
    }

    @GetMapping("/auction-venue-list-iframe")
    public String auction_venue_list_iframe(Model model) {
        List<AuctionVenue> auctionVenues = auctionVenueService.queryOnlineAuctionVenue();
        model.addAttribute("auctionVenues", auctionVenues);
        return "customer/auction-venue-list";
    }

    @GetMapping("/auction-venue")
    public String auction_venue(@RequestParam int venueID, Model model) {
        List<AuctionRecord> auctionRecords = auctionRecordService.queryAuctionRecordByAuctionVenueID(venueID);
        model.addAttribute("auctionRecords", auctionRecords);
        return "customer/auction-venue";
    }

    @GetMapping("/auction-venue-iframe")
    public String auction_venue_iframe(@RequestBody AuctionVenue auctionVenue, Model model) {
        List<AuctionRecord> auctionRecords = auctionRecordService.queryAuctionRecordByAuctionVenueID(auctionVenue.getVenueID());
        model.addAttribute("auctionRecords", auctionRecords);
        return "customer/auction-venue";
    }

    @PostMapping("/search")
    public String search(@RequestParam("venueID") int venueID, Model model) {
        model.addAttribute("cite", "search-iframe/" + venueID);
        return "customer/center";
    }

    @GetMapping("/search-iframe/{venueID}")
    public String search_iframe(@PathVariable int venueID, Model model) {
        AuctionVenue queryAuctionVenue = auctionVenueService.queryAuctionVenueByID(venueID);
        model.addAttribute("flag", queryAuctionVenue != null);
        model.addAttribute("auctionVenues",
                queryAuctionVenue == null ? null : List.of(new AuctionVenue[]{queryAuctionVenue}));
        return "customer/search-auction-venue-list";
    }

    @GetMapping("/auction-venue/{venueID}")
    public String auctioneer_venue(@PathVariable int venueID, Model model) {
        List<Auction> auctions = auctionService.queryAuctionsByVenueID(venueID);
        AuctionVenue auctionVenue = auctionVenueService.queryAuctionVenueByID(venueID);
        model.addAttribute("auctions", auctions);
        model.addAttribute("auctionVenue", auctionVenue);
        return "customer/auction-venue";
    }

    @GetMapping("/auction-venue/{venueID}/{auctionID}")
    public String auction_venue_auctionID(HttpServletRequest request,
                                          @PathVariable int venueID, @PathVariable int auctionID, Model model) {
        List<Auction> auctions = auctionService.queryAuctionsByVenueID(venueID);
        model.addAttribute("auctions", auctions);
        Auction auction = auctionService.queryAuctionByID(auctionID);
        model.addAttribute("auction", auction);
        HttpSession session = request.getSession();
        Integer customerID = (Integer) session.getAttribute("customerID");
        model.addAttribute("customerID", customerID);
        return "customer/auction-venue-page";
    }
}
