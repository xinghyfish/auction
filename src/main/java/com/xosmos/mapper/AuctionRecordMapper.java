package com.xosmos.mapper;

import com.xosmos.entity.Auction;
import com.xosmos.entity.AuctionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AuctionRecordMapper {

    AuctionRecord queryAuctionRecordByID(int auctionRecordID);

    List<AuctionRecord> queryAllAuctionRecords();

    List<AuctionRecord> queryAuctionRecordByCustomerID(int customerID);

    List<AuctionRecord> queryAuctionRecordByAuctionVenueID(int venueID);

    AuctionRecord queryAuctionRecordByVenueIDAndAuctionID(int venueID, int auctionID);

    List<Auction> queryAuctionsByVenueID(int venueID);

    int addAuctionRecord(AuctionRecord auctionRecord);

    int updateAuctionRecord(AuctionRecord auctionRecord);
}
