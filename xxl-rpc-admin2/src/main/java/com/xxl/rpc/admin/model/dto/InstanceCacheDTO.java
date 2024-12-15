package com.xxl.rpc.admin.model.dto;

import com.xxl.rpc.admin.model.entity.Instance;

import java.io.Serializable;
import java.util.Date;

/**
 *  Instance Cache DTO
 *
 *  Created by xuxueli on '2024-12-15 11:08:18'.
 */
public class InstanceCacheDTO implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * id
     */
    private long id;

    /**
     * Env（环境唯一标识）
     */
    private String env;

    /**
     * AppName（应用唯一标识）
     */
    private String appname;

    /**
     * 注册节点IP
     */
    private String ip;

    /**
     * 注册节点端口号
     */
    private int port;

    /**
     * 扩展信息
     */
    private String extendInfo;

    public InstanceCacheDTO() {
    }
    public InstanceCacheDTO(Instance instance) {
        this.id = instance.getId();
        this.env = instance.getEnv();
        this.appname = instance.getAppname();
        this.ip = instance.getIp();
        this.port = instance.getPort();
        this.extendInfo = instance.getExtendInfo();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

}