package com.xosmos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionRecord {
    enum Status {
        INIT, SUCCESS, FAIL
    }

    private int recordID;
    private Auction auction;
    private Customer customer;
    private AuctionVenue auctionVenue;
    private Timestamp startTime;
    private Timestamp endTime;
    private Status status;
}
