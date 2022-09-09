package com.xosmos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionVenue {
    private int venueID;
    private String location;
    private List<Auction> auctions;
    private Auctioneer auctioneer;
    private Timestamp startTime;
    private Timestamp endTime;
}