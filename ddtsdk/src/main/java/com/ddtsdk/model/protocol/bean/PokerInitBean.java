package com.ddtsdk.model.protocol.bean;

import java.util.List;

public class PokerInitBean {
    /**
     * {
     * "money": "0.0",
     * "give_money": "18950.00",
     * "multiple": {
     * "RoyalFlush": "100000",
     * "StraightFlush": "5000",
     * "Four": "500",
     * "FullHouse": "50",
     * "Fluse": "30",
     * "Straight": "20",
     * "ThreeKind": "5",
     * "TwoPair": "2",
     * "OnePair": "1",
     * "HighCard": "0.5"
     * },
     * "Instructions": " 操作说明:</br>1、点击“切换”按钮，切换使用平台币或赠送币参加游戏</br>2、点击“+”““_”按钮，调整下注数量。点击“SPIN”开始。</br>3、最小下数: 100 最大下注数 10000。下注必须为100的倍数。</br>4、中奖返回均为赠送币"
     * }
     */
    private String money;
    private String give_money;
    private Multiple multiple;
    private String Instructions;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getGive_money() {
        return give_money;
    }

    public void setGive_money(String give_money) {
        this.give_money = give_money;
    }

    public Multiple getMultiple() {
        return multiple;
    }

    public void setMultiple(Multiple multiple) {
        this.multiple = multiple;
    }

    public String getInstructions() {
        return Instructions;
    }

    public void setInstructions(String instructions) {
        Instructions = instructions;
    }

   public class Multiple {
        private String RoyalFlush;
        private String StraightFlush;
        private String Four;
        private String FullHouse;
        private String Fluse;
        private String Straight;
        private String ThreeKind;
        private String TwoPair;
        private String OnePair;
        private String HighCard;

        public String getRoyalFlush() {
            return RoyalFlush;
        }

        public void setRoyalFlush(String royalFlush) {
            RoyalFlush = royalFlush;
        }

        public String getStraightFlush() {
            return StraightFlush;
        }

        public void setStraightFlush(String straightFlush) {
            StraightFlush = straightFlush;
        }

        public String getFour() {
            return Four;
        }

        public void setFour(String four) {
            Four = four;
        }

        public String getFullHouse() {
            return FullHouse;
        }

        public void setFullHouse(String fullHouse) {
            FullHouse = fullHouse;
        }

        public String getFluse() {
            return Fluse;
        }

        public void setFluse(String fluse) {
            Fluse = fluse;
        }

        public String getStraight() {
            return Straight;
        }

        public void setStraight(String straight) {
            Straight = straight;
        }

        public String getThreeKind() {
            return ThreeKind;
        }

        public void setThreeKind(String threeKind) {
            ThreeKind = threeKind;
        }

        public String getTwoPair() {
            return TwoPair;
        }

        public void setTwoPair(String twoPair) {
            TwoPair = twoPair;
        }

        public String getOnePair() {
            return OnePair;
        }

        public void setOnePair(String onePair) {
            OnePair = onePair;
        }

        public String getHighCard() {
            return HighCard;
        }

        public void setHighCard(String highCard) {
            HighCard = highCard;
        }
    }


}
