package com.xxl.rpc.core.remoting.entity;

/**
 * beat for keep-alive
 *
 * @author xuxueli 2019-09-27
 */
public final class XxlRpcBeat {

    public static final int BEAT_INTERVAL = 30;
    public static final String BEAT_ID = "BEAT_PING_PONG";

    public static XxlRpcRequest BEAT_PING;

    static {
        BEAT_PING = new XxlRpcRequest(){};
        BEAT_PING.setRequestId(BEAT_ID);
    }

}
