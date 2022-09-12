package com.xosmos.controller;

import com.xosmos.entity.Auction;
import com.xosmos.entity.AuctionRecord;
import com.xosmos.entity.AuctionVenue;
import com.xosmos.entity.Auctioneer;
import com.xosmos.service.AuctionRecordService;
import com.xosmos.service.AuctionService;
import com.xosmos.service.AuctionVenueService;
import com.xosmos.service.AuctioneerService;
import com.xosmos.utils.EncryptUtils;
import com.xosmos.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/auctioneer")
public class AuctioneerController {

    @Autowired
    private AuctioneerService auctioneerService;
    @Autowired
    private AuctionVenueService auctionVenueService;
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private AuctionRecordService auctionRecordService;

    @GetMapping("/login")
    public String login() {
        return "auctioneer/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody Auctioneer auctioneer, HttpServletRequest request) {
        String plaintext = auctioneer.getPwd();
        String ciphertext = EncryptUtils.encrypt(plaintext);
        Auctioneer queryAuctioneer = auctioneerService.queryAuctioneerByEmail(auctioneer.getEmail());
        if (queryAuctioneer == null) {
            return JSONUtils.getJSONString(1);
        } else if (!ciphertext.equals(queryAuctioneer.getPwd())) {
            return JSONUtils.getJSONString(2);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("auctioneerID", queryAuctioneer.getAuctioneerID());
            return JSONUtils.getJSONString(0);
        }
    }

    @GetMapping("/center")
    public String center(Model model) {
        model.addAttribute("cite", "info");
        return "auctioneer/center";
    }

    @GetMapping("/info")
    public String info(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer auctioneerID = (Integer) session.getAttribute("auctioneerID");
        Auctioneer auctioneer = auctioneerService.queryAuctioneerByID(auctioneerID);
        model.addAttribute("auctioneer", auctioneer);
        return "auctioneer/info";
    }

    @GetMapping("/update-info")
    public String update_info(Model model) {
        model.addAttribute("cite", "update-info-iframe");
        return "auctioneer/center";
    }

    @GetMapping("/update-info-iframe")
    public String update_info_iframe(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer auctioneerID = (Integer) session.getAttribute("auctioneerID");
        Auctioneer auctioneer = auctioneerService.queryAuctioneerByID(auctioneerID);
        model.addAttribute("auctioneer", auctioneer);
        return "auctioneer/update-info";
    }

    @GetMapping("/auction-venue-list")
    public String auction_venue_list(Model model) {
        model.addAttribute("cite", "auction-venue-list-iframe");
        return "auctioneer/center";
    }

    @GetMapping("/auction-venue-list-iframe")
    public String auction_venue_list_iframe(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Integer auctioneerID = (Integer) session.getAttribute("auctioneerID");
        List<AuctionVenue> auctionVenues = auctionVenueService.queryAuctionVenueByAuctioneerID(auctioneerID);
        model.addAttribute("auctionVenues", auctionVenues);
        return "auctioneer/auction-venue-list";
    }

    @GetMapping("/add-auction-venue")
    public String add_auction_venue(Model model) {
        model.addAttribute("cite", "add-auction-venue-iframe");
        return "auctioneer/center";
    }

    @GetMapping("/add-auction-venue-iframe")
    public String add_auction_venue_iframe(Model model) {
        List<Auction> auctions = auctionService.queryUnsoldAuctions();
        model.addAttribute("auctions", auctions);
        return "auctioneer/add-auction-venue";
    }

    @PostMapping("/add-auction-venue")
    public String add_auction_venue(HttpServletRequest request,
                                    @RequestParam("location") String location) {
        String[] auctionIDStrings = request.getParameterValues("auctionIDs");
        HttpSession session = request.getSession();
        Integer auctioneerID = (Integer) session.getAttribute("auctioneerID");
        // 1. 创建拍卖会场
        AuctionVenue auctionVenue = new AuctionVenue();
        auctionVenue.setLocation(location);
        auctionVenue.setAuctioneerID(auctioneerID);
        auctionVenue.setStartTime(new Timestamp(System.currentTimeMillis()));
        auctionVenue.setOnline(true);
        int flag = auctionVenueService.addAuctionVenue(auctionVenue);
        if (flag == 1) {
            int venueID = auctionVenue.getVenueID();
            // 2. 创建各个拍卖纪录
            for (String auctionIDString : auctionIDStrings) {
                Integer auctionID = Integer.valueOf(auctionIDString);
                AuctionRecord auctionRecord =
                        new AuctionRecord(null, auctionID, null,
                                venueID, null, null, null,  0);
                int flag1 = auctionRecordService.addAuctionRecord(auctionRecord);
                if (flag1 != 1)
                    System.err.println("记录插入失败！");
            }
        }
        return "redirect:/auctioneer/auction-venue-list";
    }

    @GetMapping("/auction-venue/{venueID}")
    public String auctioneer_venue(@PathVariable int venueID, Model model) {
        List<Auction> auctions = auctionService.queryAuctionsByVenueID(venueID);
        AuctionVenue auctionVenue = auctionVenueService.queryAuctionVenueByID(venueID);
        model.addAttribute("auctions", auctions);
        model.addAttribute("auctionVenue", auctionVenue);
        return "auctioneer/auction-venue";
    }

    @GetMapping("/auction-venue/{venueID}/{auctionID}")
    public String auction_venue_auctionID(@PathVariable int venueID, @PathVariable int auctionID, Model model) {
        List<Auction> auctions = auctionService.queryAuctionsByVenueID(venueID);
        model.addAttribute("auctions", auctions);
        Auction auction = auctionService.queryAuctionByID(auctionID);
        model.addAttribute("auction", auction);
        return "auctioneer/auction-venue-page";
    }
}
