package com.ddtsdk.model.protocol.bean;

import java.util.List;

public class ExitMsg {
    public String jump_url;  //确认退出跳转地址如果没有，直接返回游戏，存在的话跳到对应地址
    public int model;  //显示模式 1为游戏推荐 2为banner推荐 3为随机选项
    public List<ExitData> content;

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public List<ExitData> getContent() {
        return content;
    }

    public void setContent(List<ExitData> content) {
        this.content = content;
    }

    public static class ExitData {
        private String name;  //如果为游戏推荐 显示推荐游戏名称；如果为banner推荐 显示banner名称
        private String title;  //如果为banner推荐 显示宣传文案；游戏推荐为空
        private String image_url;  //图片地址
        private String isawaken;  //是否唤醒浏览器 1是 2否
        private String screen;  //横屏还是竖屏 1横屏 0竖屏
        private String h5_url;  //h5游戏地址
        private String android_url;  //安卓下载地址
        private String ios_url;  //ios下载地址
        private String recomm_type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getIsawaken() {
            return isawaken;
        }

        public void setIsawaken(String isawaken) {
            this.isawaken = isawaken;
        }

        public String getScreen() {
            return screen;
        }

        public void setScreen(String screen) {
            this.screen = screen;
        }

        public String getH5_url() {
            return h5_url;
        }

        public void setH5_url(String h5_url) {
            this.h5_url = h5_url;
        }

        public String getAndroid_url() {
            return android_url;
        }

        public void setAndroid_url(String android_url) {
            this.android_url = android_url;
        }

        public String getIos_url() {
            return ios_url;
        }

        public void setIos_url(String ios_url) {
            this.ios_url = ios_url;
        }

        public String getRecomm_type() {
            return recomm_type;
        }

        public void setRecomm_type(String recomm_type) {
            this.recomm_type = recomm_type;
        }
    }

}
