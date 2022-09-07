package com.xosmos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auction {
    enum Status {
        TO_BE_SALE,
        ON_SALE,
        FAILED_SALE,
        SUCCESSFUL_SALE
    }

    private int auctionID;
    private String auctionName;
    private int startPrice;
    private Status status;
}
