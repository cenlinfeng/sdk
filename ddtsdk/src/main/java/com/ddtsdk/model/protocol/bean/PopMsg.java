package com.ddtsdk.model.protocol.bean;

import java.util.List;

public class PopMsg {
    private String mtype;  //类型 0为H5外链接 1为资讯id 2为appid 3为礼包id 4为盒子内部H5链接
    private String mid;  //对应 资讯id、appid、礼包id
    private String image;  //竖屏图片路径
    private String image2;  //横屏图片路径
    private String url;  //所跳转的H5链接
    private List<String> packname;  //包名
    private List<String> packdownurl;  //盒子下载包地址
    private String direction;  //IOS游戏横竖屏 0：竖屏 1：横屏

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<String> getPackname() {
        return packname;
    }

    public void setPackname(List<String> packname) {
        this.packname = packname;
    }

    public List<String> getPackdownurl() {
        return packdownurl;
    }

    public void setPackdownurl(List<String> packdownurl) {
        this.packdownurl = packdownurl;
    }
}
