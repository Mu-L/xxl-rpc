package com.xxl.rpc.core.provider.support;

import com.xxl.rpc.core.boot.XxlRpcBootstrap;
import com.xxl.rpc.core.provider.annotation.XxlRpcService;
import com.xxl.rpc.core.util.XxlRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * xxl-rpc provider (for spring)
 *
 * @author xuxueli 2018-10-18 18:09:20
 */
public class SpringProviderFactory {
    private static final Logger logger = LoggerFactory.getLogger(SpringProviderFactory.class);

    public static void scanService(ApplicationContext applicationContext, final XxlRpcBootstrap factory) {

        // valid
        if (factory.getProvider() == null) {
            return;
        }

        // scan service
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(XxlRpcService.class);
        if (serviceBeanMap!=null && !serviceBeanMap.isEmpty()) {
            for (Object serviceBean : serviceBeanMap.values()) {
                // valid
                if (serviceBean.getClass().getInterfaces().length ==0) {
                    throw new XxlRpcException("xxl-rpc, service(XxlRpcService) must inherit interface.");
                }
                // add service
                XxlRpcService xxlRpcService = serviceBean.getClass().getAnnotation(XxlRpcService.class);

                String iface = serviceBean.getClass().getInterfaces()[0].getName();
                String version = xxlRpcService.version();

                // add service
                factory.getProvider().addService(iface, version, serviceBean);
            }
        }
        logger.info(">>>>>>>>>>> xxl-rpc, SpringProviderFactory#scanService finish, serviceBeanMap:" + serviceBeanMap);

    }

}
