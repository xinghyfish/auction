package com.xosmos.service;

import com.xosmos.entity.Auction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuctionService {

    Auction queryAuctionByID(int auctionID);

    List<Auction> queryAllAuctions();

    List<Auction> queryUnsoldAuctions();

    List<Auction> queryAuctionsByVenueID(int venueID);

    int addAuction(Auction auction);

    int updateAuction(Auction auction);
}
