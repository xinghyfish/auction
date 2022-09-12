package com.xosmos.service.impl;

import com.xosmos.entity.Auction;
import com.xosmos.entity.AuctionRecord;
import com.xosmos.mapper.AuctionMapper;
import com.xosmos.mapper.AuctionRecordMapper;
import com.xosmos.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    private AuctionMapper auctionMapper;
    @Autowired
    private AuctionRecordMapper auctionRecordMapper;

    @Override
    public Auction queryAuctionByID(int auctionID) {
        return auctionMapper.queryAuctionByID(auctionID);
    }

    @Override
    public List<Auction> queryAllAuctions() {
        return auctionMapper.queryAllAuctions();
    }

    @Override
    public List<Auction> queryUnsoldAuctions() {
        List<Auction> results = auctionMapper.queryAuctionsOnStatus("待拍卖");
        results.addAll(auctionMapper.queryAuctionsOnStatus("流拍"));
        return results;
    }

    @Override
    public List<Auction> queryAuctionsByVenueID(int venueID) {
        List<AuctionRecord> auctionRecords = auctionRecordMapper.queryAuctionRecordByAuctionVenueID(venueID);
        List<Auction> auctions = new ArrayList<>();
        for (AuctionRecord record : auctionRecords)
            auctions.add(queryAuctionByID(record.getAuctionID()));
        return auctions;
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
