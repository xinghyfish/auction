package com.xosmos.controller;

import com.xosmos.entity.Admin;
import com.xosmos.entity.Auction;
import com.xosmos.entity.AuctionRecord;
import com.xosmos.entity.Auctioneer;
import com.xosmos.service.AdminService;
import com.xosmos.service.AuctionRecordService;
import com.xosmos.service.AuctionService;
import com.xosmos.service.AuctioneerService;
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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private AuctioneerService auctioneerService;
    @Autowired
    private AuctionRecordService auctionRecordService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "admin/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody Admin admin, HttpServletRequest request) {
        String plaintext = admin.getPwd();
        String ciphertext = EncryptUtils.encrypt(plaintext);
        Admin queryAdmin = adminService.queryAdminByAdminID(admin.getAdminID());
        if (queryAdmin == null) {
            return JSONUtils.getJSONString(1, "系统管理员ID不存在！");
        } else if (!queryAdmin.getPwd().equals(ciphertext)) {
            return JSONUtils.getJSONString(2, "密码错误，请重新输入！");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("adminID", admin.getAdminID());
            return JSONUtils.getJSONString(0);
        }
    }

    @GetMapping("/center")
    public String center(Model model) {
        model.addAttribute("cite", "info");
        return "admin/center";
    }

    @GetMapping("/info")
    public String center_info(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        model.addAttribute("adminID", session.getAttribute("adminID"));
        return "admin/info";
    }

    @GetMapping("/auction-list")
    public String auction_list(Model model) {
        model.addAttribute("cite", "auction-list-iframe");
        return "admin/center";
    }

    @GetMapping("/auction-list-iframe")
    public String auction_list_iframe(Model model) {
        List<Auction> auctions = auctionService.queryAllAuctions();
        model.addAttribute("auctions", auctions);
        return "admin/auction-list";
    }

    @GetMapping("/add-auction")
    public String add_auction(Model model) {
        model.addAttribute("cite", "add-auction-iframe");
        return "admin/center";
    }

    @GetMapping("/add-auction-iframe")
    public String add_auction_iframe() {
        return "admin/add-auction";
    }

    @PostMapping("/add-auction")
    public String add_auction(@RequestBody Auction auction) {
        auction.setStatus("待拍卖");
        int flag = auctionService.addAuction(auction);
        if (flag != 1)
            System.err.println("添加拍卖品失败");
        return "redirect:/admin/auction-list";
    }

    @GetMapping("/auction-records")
    public String auction_records(Model model) {
        model.addAttribute("cite", "auction-records-iframe");
        return "admin/center";
    }

    @GetMapping("/auction-records-iframe")
    private String auction_records_iframe(Model model) {
        List<AuctionRecord> auctionRecords = auctionRecordService.queryAllAuctionRecords();
        model.addAttribute("auctionRecords", auctionRecords);
        return "admin/auction-records";
    }

    @GetMapping("/auctioneer-list")
    public String auctioneer_list(Model model) {
        model.addAttribute("cite", "auctioneer-list-iframe");
        return "admin/center";
    }

    @GetMapping("/auctioneer-list-iframe")
    public String auctioneer_list_iframe(Model model) {
        List<Auctioneer> auctioneers = auctioneerService.queryLoginAuctioneers();
        model.addAttribute("auctioneers", auctioneers);
        return "admin/auctioneer-list";
    }

    @GetMapping("/login-auctioneer")
    public String add_auctioneer(Model model) {
        model.addAttribute("cite", "login-auctioneer-iframe");
        return "admin/center";
    }

    @GetMapping("/login-auctioneer-iframe")
    public String add_auctioneer_iframe() {
        return "admin/add-auctioneer";
    }

    @PostMapping("/add-auctioneer")
    public String add_auctioneer(Auctioneer auctioneer) {
        int flag = auctioneerService.addAuctioneer(auctioneer);
        if (flag != 1)
            System.err.println("注册拍卖师账号失败！");
        return "admin/auctioneer-list";
    }

    @GetMapping("/logout-auctioneer")
    public String logout_auctioneer(Model model) {
        model.addAttribute("cite", "logout-auctioneer-iframe");
        return "admin/center";
    }

    @GetMapping("/logout-auctioneer-iframe")
    public String logout_auctioneer_iframe() {
        return "admin/logout-auctioneer";
    }

    @PostMapping("/logout-auctioneer")
    public String logout_auctioneer(@RequestBody Auctioneer auctioneer) {
        int flag = auctioneerService.logoutAuctioneer(auctioneer);
        if (flag != 1)
            System.err.println("注销用户失败！");
        return "redirect:/admin/auctioneer-list";
    }
}
