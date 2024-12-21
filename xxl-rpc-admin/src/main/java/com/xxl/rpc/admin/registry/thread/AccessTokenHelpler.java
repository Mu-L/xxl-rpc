package com.xxl.rpc.admin.registry.thread;


import com.xxl.rpc.admin.constant.enums.AccessTokenStatuEnum;
import com.xxl.rpc.admin.model.entity.AccessToken;
import com.xxl.rpc.admin.registry.config.RegistryFactory;
import com.xxl.rpc.admin.registry.model.OpenApiRequest;
import com.xxl.tool.core.CollectionTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * AccessToken Helpler
 *
 * 功能：
 * 1、AccessToken 本次缓存，合法性校验
 *
 * @author xuxueli
 */
public class AccessTokenHelpler {
    private static Logger logger = LoggerFactory.getLogger(AccessTokenHelpler.class);

    /**
     * 客户端监听器

     */
    private volatile Set<String> accessTokenStore = new ConcurrentSkipListSet<>();

    /**
     * registry monitor (will remove instance that expired more than 1 day)
     */
    private Thread accessTokenThread;

    /**
     * thread stop variable
     */
    private volatile boolean toStop = false;


    /**
     * start
     */
    public void start() {

        // deferredResultMonitorThread， for clean
        accessTokenThread = RegistryCacheHelpler.startThread(new Runnable() {
            @Override
            public void run() {
                while (!toStop) {
                    try {
                        // build new data
                        ConcurrentSkipListSet<String> accessTokenStoreNew = new ConcurrentSkipListSet<>();

                        // query valid accesstoken data
                        List<AccessToken> accessTokenList = RegistryFactory.getInstance().getAccessTokenMapper().queryValidityAccessToken(AccessTokenStatuEnum.NORMAL.getValue());
                        if (CollectionTool.isNotEmpty(accessTokenList)) {
                            accessTokenStoreNew.addAll(accessTokenList.stream()
                                    .map(AccessToken::getAccessToken)
                                    .collect(Collectors.toSet()));
                        }

                        // do refresh
                        accessTokenStore = accessTokenStoreNew;
                    } catch (Throwable e) {
                        if (!toStop) {
                            logger.error(">>>>>>>>>>> xxl-rpc, AccessTokenHelpler-accessTokenThread error:{}", e.getMessage(), e);
                        }
                    }
                    try {
                        TimeUnit.SECONDS.sleep(RegistryCacheHelpler.REGISTRY_BEAT_TIME);
                    } catch (Throwable e) {
                        if (!toStop) {
                            logger.error(">>>>>>>>>>> xxl-rpc, AccessTokenHelpler-accessTokenThread error2:{}", e.getMessage(), e);
                        }
                    }
                }
                logger.info(">>>>>>>>>>> xxl-rpc, AccessTokenHelpler-accessTokenThread finish.");
            }
        }, "xxl-rpc, AccessTokenHelpler-accessTokenThread");

    }

    public void stop() {
        // mark stop
        toStop = true;
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }

        // stop thread
        RegistryCacheHelpler.stopThread(accessTokenThread);
    }

    // ---------------------- helper ----------------------


    /**
     * valid Request Token
     *
     * @param request
     * @return
     */
    public boolean validRequestToken(OpenApiRequest request) {
        return request!=null
                && request.getAccessToken()!=null
                && accessTokenStore.contains(request.getAccessToken());
    }

}
