package com.xosmos.controller;

import com.xosmos.entity.Auction;
import com.xosmos.entity.AuctionRecord;
import com.xosmos.entity.AuctionVenue;
import com.xosmos.service.AuctionRecordService;
import com.xosmos.service.AuctionService;
import com.xosmos.service.AuctionVenueService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket/{venueID}")
@Component
public class WebSocketServer {
    /* online clients */
    private static final Map<String, Session> clients = new ConcurrentHashMap<>();
    private static AuctionService auctionService;
    private static AuctionRecordService auctionRecordService;
    private static AuctionVenueService auctionVenueService;
    private Integer venueID = -1;
    private static int customerID = -1; // 将信息交给服务器来存储
    private static int finalPrice = -1;

    private void init() {
        venueID = -1;
        customerID = -1;
        finalPrice = -1;
    }

    @Autowired
    public void setAuctionRecordService(AuctionRecordService auctionRecordService) {
        WebSocketServer.auctionRecordService = auctionRecordService;
    }

    @Autowired
    public void setAuctionService(AuctionService auctionService) {
        WebSocketServer.auctionService = auctionService;
    }

    @Autowired
    public void setAuctionVenueService(AuctionVenueService auctionVenueService) {
        WebSocketServer.auctionVenueService = auctionVenueService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("venueID") String venueID) {
        clients.put(session.getId(), session);
        this.venueID = Integer.valueOf(venueID);
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(session.getId());
    }

    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        JSONObject json = new JSONObject(message);
        String key = (String) json.get("key");
        switch (key) {
            case "begin" -> { // 进入拍卖系统开始拍卖
                Integer auctionID = (Integer) json.get("auctionID");
                Auction auction = auctionService.queryAuctionByID(auctionID);
                auctionService.updateAuction(auction);
                AuctionRecord auctionRecord = auctionRecordService.queryAuctionRecordByVenueIDAndAuctionID(venueID, auctionID);
                auctionRecord.setStartTime(new Timestamp(System.currentTimeMillis()));
                // 通知每一个用户可以拍卖
            }
            case "end" -> { // 退出拍卖系统
                Integer auctionID = (Integer) json.get("auctionID");
                Auction auction = auctionService.queryAuctionByID(auctionID);
                String status = finalPrice != - 1 ? "已拍卖" : "流拍";
                auction.setStatus(status);
                auctionService.updateAuction(auction);
                // 通知每一个用户拍卖结束
                Timestamp endTime = new Timestamp(System.currentTimeMillis());
                AuctionRecord auctionRecord = auctionRecordService.queryAuctionRecordByVenueIDAndAuctionID(this.venueID, auctionID);
                auctionRecord.setEndTime(endTime);
                // 更新拍卖信息
                if (finalPrice != -1) { // 成功拍卖
                    auctionRecord.setCustomerID(customerID);
                    auctionRecord.setFinalPrice(finalPrice);
                    auctionRecord.setStatus(1);
                    // 通知所有买家结果
                    json.put("customerID", customerID);
                    json.put("status", status);
                } else { // 流拍
                    auctionRecord.setVenueID(2);
                }
                int flag = auctionRecordService.updateAuctionRecord(auctionRecord);
                if (flag != 1)
                    System.err.println("更新拍卖记录失败！");
            }
            case "price" -> { // 叫价，更新价格
                int currentCustomerID = (Integer) json.get("customerID");
                int currentPrice = (Integer) json.get("currentPrice");
                if (currentPrice > finalPrice) {
                    finalPrice = currentPrice;
                    customerID = currentCustomerID;
                }
                Timestamp updateTime = new Timestamp(System.currentTimeMillis());
                json.put("updateTime", updateTime);
                // 仅需要转发
            }
            case "endVenue" -> {
                AuctionVenue auctionVenue = auctionVenueService.queryAuctionVenueByID(venueID);
                auctionVenue.setOnline(false);
                auctionVenue.setEndTime(new Timestamp(System.currentTimeMillis()));
                int flag = auctionVenueService.updateAuctionVenue(auctionVenue);
                if (flag != 1)
                    System.err.println("更新拍卖会场信息失败！");
            }
            default -> {
                System.err.println("非法消息");
            }
        }
        this.sendAll(json);
        if (key.equals("end")) init();
    }


    public void sendAll(JSONObject jsonObject) {
        for (Map.Entry<String, Session> sessionEntry : clients.entrySet())
            sessionEntry.getValue().getAsyncRemote().sendText(JSONObject.valueToString(jsonObject));
    }
}
