package com.ddtsdk.model.data;

/**
 * Created by CZG on 2020/6/5.
 */
public class RoleData {
    private String scene_Id;    //	场景	string	Y	字段值: enterServer:进入服务器, createRole:创建角色, levelUp:角色升级
    private String roleid;      //	角色id
    private String rolename;    //	角色名
    private String rolelevel;   //	角色等级
    private String zoneid;      //	区服id
    private String zonename;    //	区服名称
    private String serverid;    //	服务器id
    private String servername;   //服务器名称
    private String balance;     //	游戏币余额
    private String vip;         //	用户vip等级
    private String partyname;   //	所属帮派
    private String rolectime;   //	创角时间
    private String rolelevelmtime;//    角色等级变化时间

    public String getScene_Id() {
        return scene_Id;
    }

    public void setScene_Id(String scene_Id) {
        this.scene_Id = scene_Id;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getRolelevel() {
        return rolelevel;
    }

    public void setRolelevel(String rolelevel) {
        this.rolelevel = rolelevel;
    }

//    public String getZoneid() {
//        return zoneid;
//    }
//
//    public void setZoneid(String zoneid) {
//        this.zoneid = zoneid;
//    }
//
//    public String getZonename() {
//        return zonename;
//    }
//
//    public void setZonename(String zonename) {
//        this.zonename = zonename;
//    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getPartyname() {
        return partyname;
    }

    public void setPartyname(String partyname) {
        this.partyname = partyname;
    }

    public String getRolectime() {
        return rolectime;
    }

    public void setRolectime(String rolectime) {
        this.rolectime = rolectime;
    }

    public String getRolelevelmtime() {
        return rolelevelmtime;
    }

    public void setRolelevelmtime(String rolelevelmtime) {
        this.rolelevelmtime = rolelevelmtime;
    }

    public String getServerid() {
        return serverid;
    }

    public void setServerid(String serverid) {
        this.serverid = serverid;
    }

    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }

    public String getZoneid() {
        return zoneid;
    }

    public void setZoneid(String zoneid) {
        this.zoneid = zoneid;
    }

    public String getZonename() {
        return zonename;
    }

    public void setZonename(String zonename) {
        this.zonename = zonename;
    }
}
