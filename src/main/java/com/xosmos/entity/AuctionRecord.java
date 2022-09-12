package com.xosmos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionRecord {
    private Integer recordID;
    private Integer auctionID;
    private Integer customerID;
    private Integer venueID;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer finalPrice;
    private Integer status;
}
