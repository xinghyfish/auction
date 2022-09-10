package com.xosmos.controller;

import com.xosmos.entity.Auctioneer;
import com.xosmos.service.AuctioneerService;
import com.xosmos.utils.EncryptUtils;
import com.xosmos.utils.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/auctioneer")
public class AuctioneerController {

    @Autowired
    private AuctioneerService auctioneerService;

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
            return JSONUtils.getJSONString(0);
        }
    }

    @RequestMapping("/center")
    public String center() {
        return "auctioneer/center";
    }
}
