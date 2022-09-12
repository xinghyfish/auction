package com.xosmos.service;

import com.xosmos.entity.Auctioneer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuctioneerService {
    Auctioneer queryAuctioneerByID(int auctioneerID);

    Auctioneer queryAuctioneerByEmail(String email);

    List<Auctioneer> queryAllAuctioneers();

    List<Auctioneer> queryLoginAuctioneers();

    int addAuctioneer(Auctioneer auctioneer);

    int logoutAuctioneer(Auctioneer auctioneer);
}
