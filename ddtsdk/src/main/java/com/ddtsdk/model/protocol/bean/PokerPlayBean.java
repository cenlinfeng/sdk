package com.ddtsdk.model.protocol.bean;


import java.util.ArrayList;
import java.util.List;

public class PokerPlayBean {
    /**
     * {
     * "hand": [
     * "spade_2",
     * "spade_10",
     * "heart_9",
     * "heart_14",
     * "spade_11"
     * ],
     * "suitpattern": "HighCard",
     * "multiple": "0.5",
     * "money": "0.0",
     * "give_money": "18900.00"
     * }
     */
    private List<String> hand;
    private String suitpattern;
    private String multiple;
    private String money;
    private String give_money;

    public List<Integer> getHand() {
        return SerializedCard(hand);
    }

    public String getSuitpattern() {
        return suitpattern;
    }

    public String getMultiple() {
        return multiple;
    }

    public String getMoney() {
        return money;
    }

    public String getGive_money() {
        return give_money;
    }

    private List<Integer> SerializedCard(List<String> hand) {
        List<Integer> newHand = new ArrayList<>();
        for (int i = 0; i < hand.size(); i++) {
            newHand.add(i, getIndex(hand.get(i)));
        }
        return newHand;
    }

    private int getIndex(String card) {
        switch (card) {
            case "diamond_2":
                return 0;
            case "diamond_3":
                return 1;
            case "diamond_4":
                return 2;
            case "diamond_5":
                return 3;
            case "diamond_6":
                return 4;
            case "diamond_7":
                return 5;
            case "diamond_8":
                return 6;
            case "diamond_9":
                return 7;
            case "diamond_10":
                return 8;
            case "diamond_11":
                return 9;
            case "diamond_12":
                return 10;
            case "diamond_13":
                return 11;
            case "diamond_14":
                return 12;
            case "club_2":
                return 13;
            case "club_3":
                return 14;
            case "club_4":
                return 15;
            case "club_5":
                return 16;
            case "club_6":
                return 17;
            case "club_7":
                return 18;
            case "club_8":
                return 19;
            case "club_9":
                return 20;
            case "club_10":
                return 21;
            case "club_11":
                return 22;
            case "club_12":
                return 23;
            case "club_13":
                return 24;
            case "club_14":
                return 25;
            case "heart_2":
                return 26;
            case "heart_3":
                return 27;
            case "heart_4":
                return 28;
            case "heart_5":
                return 29;
            case "heart_6":
                return 30;
            case "heart_7":
                return 31;
            case "heart_8":
                return 32;
            case "heart_9":
                return 33;
            case "heart_10":
                return 34;
            case "heart_11":
                return 35;
            case "heart_12":
                return 36;
            case "heart_13":
                return 37;
            case "heart_14":
                return 38;
            case "spade_2":
                return 39;
            case "spade_3":
                return 40;
            case "spade_4":
                return 41;
            case "spade_5":
                return 42;
            case "spade_6":
                return 43;
            case "spade_7":
                return 44;
            case "spade_8":
                return 45;
            case "spade_9":
                return 46;
            case "spade_10":
                return 47;
            case "spade_11":
                return 48;
            case "spade_12":
                return 49;
            case "spade_13":
                return 50;
            case "spade_14":
                return 51;
        }
        return -1;
    }

}