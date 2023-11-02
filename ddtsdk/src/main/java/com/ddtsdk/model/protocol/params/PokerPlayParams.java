package com.ddtsdk.model.protocol.params;

public class PokerPlayParams {
    private String playmoney;
    private String playmoneytype;

    public PokerPlayParams(String jackpot,String type){
        this.playmoney = jackpot;
        this.playmoneytype = type;
    }
}
