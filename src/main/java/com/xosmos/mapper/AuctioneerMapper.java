package com.xosmos.mapper;

import com.xosmos.entity.Auctioneer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AuctioneerMapper {

    Auctioneer queryAuctioneerByID(int auctioneerID);

    Auctioneer queryAuctioneerByEmail(String email);

    List<Auctioneer> queryAllAuctioneers();

    int addAuctioneer(Auctioneer auctioneer);

    int updateAuctioneer(Auctioneer auctioneer);
}
