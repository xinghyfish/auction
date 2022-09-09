package com.xosmos.mapper;

import com.xosmos.entity.Auction;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AuctionMapper {

    Auction queryAuctionByID(int auctionID);

    List<Auction> queryAllAuctions();

    int addAuction(Auction auction);

    int updateAuction(Auction auction);
}
