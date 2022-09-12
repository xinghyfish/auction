package com.xosmos.service.impl;

import com.xosmos.entity.Auction;
import com.xosmos.entity.AuctionRecord;
import com.xosmos.mapper.AuctionRecordMapper;
import com.xosmos.service.AuctionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuctionRecordServiceImpl implements AuctionRecordService {

    @Autowired
    private AuctionRecordMapper auctionRecordMapper;

    @Override
    public AuctionRecord queryAuctionRecordByID(int auctionRecordID) {
        return auctionRecordMapper.queryAuctionRecordByID(auctionRecordID);
    }

    @Override
    public List<AuctionRecord> queryAuctionRecordByCustomerID(int customerID) {
        return auctionRecordMapper.queryAuctionRecordByCustomerID(customerID);
    }

    @Override
    public List<AuctionRecord> queryAuctionRecordByAuctionVenueID(int venueID) {
        return auctionRecordMapper.queryAuctionRecordByAuctionVenueID(venueID);
    }

    @Override
    public List<AuctionRecord> queryAllAuctionRecords() {
        return auctionRecordMapper.queryAllAuctionRecords();
    }

    @Override
    public AuctionRecord queryAuctionRecordByVenueIDAndAuctionID(int venueID, int auctionID) {
        return auctionRecordMapper.queryAuctionRecordByVenueIDAndAuctionID(venueID, auctionID);
    }

    @Override
    public int addAuctionRecord(AuctionRecord auctionRecord) {
        return auctionRecordMapper.addAuctionRecord(auctionRecord);
    }

    @Override
    public int updateAuctionRecord(AuctionRecord auctionRecord) {
        return auctionRecordMapper.updateAuctionRecord(auctionRecord);
    }
}
