package com.xosmos.controller;

import com.xosmos.entity.Auction;
import com.xosmos.entity.AuctionRecord;
import com.xosmos.service.AuctionRecordService;
import com.xosmos.service.AuctionService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket/{venueID}")
@Component
public class WebSocketServer {
    /* online clients */
    private static Map<String, Session> clients = new ConcurrentHashMap<>();
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private AuctionRecordService auctionRecordService;
    private int venueID;
    private static int customerID = -1; // 将信息交给服务器来存储
    private static int finalPrice = -1;

    @OnOpen
    public void onOpen(Session session, @PathVariable int venueID) {
        clients.put(session.getId(), session);
        this.venueID = venueID;
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
    public void onMessage(String message) {
        JSONObject json = new JSONObject(message);
        String key = (String) json.get("key");
        switch (key) {
            case "begin" -> { // 进入拍卖系统开始拍卖
                Integer auctionID = (Integer) json.get("auctionID");
                Auction auction = auctionService.queryAuctionByID(auctionID);
                auction.setStatus("拍卖中");
                auctionService.updateAuction(auction);
                // 通知每一个用户可以拍卖
                break;
            }
            case "end" -> { // 退出拍卖系统
                Integer auctionID = (Integer) json.get("auctionID");
                Auction auction = auctionService.queryAuctionByID(auctionID);
                String status = finalPrice != - 1 ? "已流拍" : "未拍卖";
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
                } else { // 流拍
                    auctionRecord.setVenueID(2);
                }
                break;
            }
            case "price" -> { // 叫价，更新价格
                int currentCustomerID = (Integer) json.get("customerID");
                int currentPrice = (Integer) json.get("price");
                if (currentPrice > finalPrice) {
                    finalPrice = currentPrice;
                    customerID = currentCustomerID;
                }
                Timestamp updateTime = new Timestamp(System.currentTimeMillis());
                json.put("updateTime", updateTime);
                // 仅需要转发
                break;
            }
            default -> {
                System.err.println("非法消息");
            }
        }
        this.sendAll(json);
    }


    public void sendAll(JSONObject jsonObject) {
        for (Map.Entry<String, Session> sessionEntry : clients.entrySet())
            sessionEntry.getValue().getAsyncRemote().sendText(JSONObject.valueToString(jsonObject));
    }
}
