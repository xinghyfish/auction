package com.xosmos.service.impl;

import com.xosmos.entity.AuctionVenue;
import com.xosmos.mapper.AuctionVenueMapper;
import com.xosmos.service.AuctionVenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctionVenueServiceImpl implements AuctionVenueService {

    @Autowired
    private AuctionVenueMapper auctionVenueMapper;

    @Override
    public AuctionVenue queryAuctionVenueByID(int venueID) {
        return auctionVenueMapper.queryAuctionVenueByID(venueID);
    }

    @Override
    public List<AuctionVenue> queryOnlineAuctionVenue() {
        return auctionVenueMapper.queryOnlineAuctionVenue();
    }

    @Override
    public List<AuctionVenue> queryAuctionVenueByAuctioneerID(int auctioneerID) {
        return auctionVenueMapper.queryAuctionVenueByAuctioneerID(auctioneerID);
    }

    @Override
    public int addAuctionVenue(AuctionVenue auctionVenue) {
        return auctionVenueMapper.addAuctionVenue(auctionVenue);
    }
}
