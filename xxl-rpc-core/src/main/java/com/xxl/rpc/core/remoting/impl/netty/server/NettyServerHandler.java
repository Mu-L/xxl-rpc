package com.xxl.rpc.core.remoting.impl.netty.server;

import com.xxl.rpc.core.remoting.entity.XxlRpcBeat;
import com.xxl.rpc.core.remoting.entity.XxlRpcRequest;
import com.xxl.rpc.core.remoting.entity.XxlRpcResponse;
import com.xxl.rpc.core.provider.ProviderFactory;
import com.xxl.rpc.netty.shaded.io.netty.channel.ChannelHandlerContext;
import com.xxl.rpc.netty.shaded.io.netty.channel.SimpleChannelInboundHandler;
import com.xxl.rpc.netty.shaded.io.netty.handler.timeout.IdleStateEvent;
import com.xxl.tool.exception.ThrowableTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * netty server handler
 *
 * @author xuxueli 2015-10-29 20:07:37
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<XxlRpcRequest> {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    private ProviderFactory xxlRpcProviderFactory;
    private ThreadPoolExecutor serverHandlerPool;

    public NettyServerHandler(final ProviderFactory xxlRpcProviderFactory, final ThreadPoolExecutor serverHandlerPool) {
        this.xxlRpcProviderFactory = xxlRpcProviderFactory;
        this.serverHandlerPool = serverHandlerPool;
    }


    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final XxlRpcRequest xxlRpcRequest) throws Exception {

        // filter beat
        if (XxlRpcBeat.BEAT_ID.equalsIgnoreCase(xxlRpcRequest.getRequestId())){
            logger.debug(">>>>>>>>>>> xxl-rpc provider netty server read beat-ping.");
            return;
        }

        // do invoke
        try {
            serverHandlerPool.execute(new Runnable() {
                @Override
                public void run() {
                    // invoke + response
                    XxlRpcResponse xxlRpcResponse = xxlRpcProviderFactory.invokeService(xxlRpcRequest);

                    ctx.writeAndFlush(xxlRpcResponse);
                }
            });
        } catch (Throwable e) {
            // catch error
            XxlRpcResponse xxlRpcResponse = new XxlRpcResponse();
            xxlRpcResponse.setRequestId(xxlRpcRequest.getRequestId());
            xxlRpcResponse.setErrorMsg(ThrowableTool.toString(e));

            ctx.writeAndFlush(xxlRpcResponse);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	logger.error(">>>>>>>>>>> xxl-rpc provider netty server caught exception", cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            ctx.channel().close();      // beat 3N, close if idle
            logger.debug(">>>>>>>>>>> xxl-rpc provider netty server close an idle channel.");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
