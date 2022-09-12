package com.xosmos.service;

import com.xosmos.entity.Auction;
import com.xosmos.entity.AuctionRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface AuctionRecordService {

    AuctionRecord queryAuctionRecordByID(int auctionRecordID);

    List<AuctionRecord> queryAuctionRecordByCustomerID(int customerID);

    List<AuctionRecord> queryAuctionRecordByAuctionVenueID(int venueID);

    List<AuctionRecord> queryAllAuctionRecords();

    AuctionRecord queryAuctionRecordByVenueIDAndAuctionID(int venueID, int auctionID);

    int addAuctionRecord(AuctionRecord auctionRecord);

    int updateAuctionRecord(AuctionRecord auctionRecord);
}
