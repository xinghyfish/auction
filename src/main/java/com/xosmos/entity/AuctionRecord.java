package com.xosmos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionRecord {
    private int recordID;
    private int auctionID;
    private int customerID;
    private int auctionVenueID;
    private Timestamp startTime;
    private Timestamp endTime;
    private int finalPrice;
    private String status;
}
