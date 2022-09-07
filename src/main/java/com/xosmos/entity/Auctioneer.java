package com.xosmos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auctioneer {
    private int auctioneerID;
    private String auctioneerName;
    private String pwd;
    private String email;
    private String phone;
    private boolean isLogout;
}
