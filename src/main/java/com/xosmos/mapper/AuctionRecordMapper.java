package com.xosmos.mapper;

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

    int addAuctionRecord(AuctionRecord auctionRecord);

    int updateAuctionRecord(AuctionRecord auctionRecord);
}
