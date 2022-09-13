package com.xosmos.mapper;

import com.xosmos.entity.AuctionVenue;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AuctionVenueMapper {

    AuctionVenue queryAuctionVenueByID(int venueID);

    List<AuctionVenue> queryOnlineAuctionVenue();

    List<AuctionVenue> queryAuctionVenueByAuctioneerID(int auctioneerID);

    int addAuctionVenue(AuctionVenue auctionVenue);

    int updateAuctionVenue(AuctionVenue auctionVenue);

}
