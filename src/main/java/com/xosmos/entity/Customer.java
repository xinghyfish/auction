package com.xosmos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private int customerID;
    private String customerName;
    private String pwd;
    private String email;
    private String phone;
    private boolean isLogout;
}
