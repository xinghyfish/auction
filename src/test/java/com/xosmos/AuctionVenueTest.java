package com.xosmos;

import com.xosmos.entity.AuctionVenue;
import com.xosmos.service.AuctionVenueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class AuctionVenueTest {

    @Autowired
    private AuctionVenueService auctionVenueService;

    @Test
    public void onlineTest() {
        List<AuctionVenue> auctionVenues = auctionVenueService.queryOnlineAuctionVenue();
        System.out.println(auctionVenues);
    }

    @Test
    public void nullTest() {
        return;
    }
}
