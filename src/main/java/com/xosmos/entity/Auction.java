package com.xosmos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auction {
    private int auctionID;
    private String auctionName;
    private int startPrice;
    private String status;
}
