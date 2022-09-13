package com.xosmos.service;

import com.xosmos.entity.AuctionVenue;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuctionVenueService {

    List<AuctionVenue> queryOnlineAuctionVenue();

    AuctionVenue queryAuctionVenueByID(int venueID);

    List<AuctionVenue> queryAuctionVenueByAuctioneerID(int auctioneerID);

    int addAuctionVenue(AuctionVenue auctionVenue);

    int updateAuctionVenue(AuctionVenue auctionVenue);
}
