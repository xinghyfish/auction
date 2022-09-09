package com.xosmos.service.impl;

import com.xosmos.entity.Auction;
import com.xosmos.mapper.AuctionMapper;
import com.xosmos.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    private AuctionMapper auctionMapper;

    @Override
    public Auction queryAuctionByID(int auctionID) {
        return auctionMapper.queryAuctionByID(auctionID);
    }

    @Override
    public List<Auction> queryAllAuctions() {
        return auctionMapper.queryAllAuctions();
    }

    @Override
    public int addAuction(Auction auction) {
        return auctionMapper.addAuction(auction);
    }

    @Override
    public int updateAuction(Auction auction) {
        return auctionMapper.updateAuction(auction);
    }
}
