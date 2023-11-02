package com.ddtsdk.model.protocol.params;

/**
 * Created by CZG on 2020/4/16.
 */
public class RoleInfoParams {
    private String serverid;
    private String servername;
    private String roleid;
    private String idfa;
    private String rolename;
    private String rolelevel;
    private String scene_Id;
    private int isLandscape;  //0:竖屏    1：横屏


    public RoleInfoParams(String serverid, String servername, String roleid, String rolename, String rolelevel,String scene_Id,int isLandscape) {
        this.serverid = serverid;
        this.servername = servername;
        this.roleid = roleid;
        this.idfa = "";
        this.rolename = rolename;
        this.rolelevel = rolelevel;
        this.scene_Id = scene_Id;
        this.isLandscape = isLandscape;
    }
}
