package com.xosmos.service.impl;

import com.xosmos.entity.Auctioneer;
import com.xosmos.mapper.AuctioneerMapper;
import com.xosmos.service.AuctioneerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctioneerServiceImpl implements AuctioneerService {

    @Autowired
    private AuctioneerMapper auctioneerMapper;

    @Override
    public Auctioneer queryAuctioneerByID(int auctioneerID) {
        return auctioneerMapper.queryAuctioneerByID(auctioneerID);
    }

    @Override
    public Auctioneer queryAuctioneerByEmail(String email) {
        return auctioneerMapper.queryAuctioneerByEmail(email);
    }

    @Override
    public List<Auctioneer> queryAllAuctioneers() {
        return auctioneerMapper.queryAllAuctioneers();
    }

    @Override
    public List<Auctioneer> queryLoginAuctioneers() {
        return auctioneerMapper.queryLoginAuctioneers();
    }

    @Override
    public int addAuctioneer(Auctioneer auctioneer) {
        return auctioneerMapper.addAuctioneer(auctioneer);
    }

    @Override
    public int logoutAuctioneer(Auctioneer auctioneer) {
        auctioneer.setLogout(true);
        return auctioneerMapper.updateAuctioneer(auctioneer);
    }
}
