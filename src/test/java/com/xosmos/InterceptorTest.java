package com.xosmos;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class InterceptorTest {

    @Test
    public void test() throws MalformedURLException {
        String u = "http://localhost:8080/auctioneer/add-auction-venue";
        URL url = new URL(u);
        String path = url.getPath();
        String[] domains = path.split("/");
        System.out.println(domains[1]);
    }
}
