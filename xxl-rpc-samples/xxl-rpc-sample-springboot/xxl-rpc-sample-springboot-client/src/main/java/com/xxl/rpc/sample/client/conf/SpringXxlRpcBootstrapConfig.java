package com.xxl.rpc.sample.client.conf;

import com.xxl.rpc.core.boot.config.BaseConfig;
import com.xxl.rpc.core.boot.support.SpringXxlRpcBootstrap;
import com.xxl.rpc.core.invoker.config.InvokerConfig;
import com.xxl.rpc.core.provider.config.ProviderConfig;
import com.xxl.rpc.core.register.impl.XxlConfRegister;
import com.xxl.rpc.core.remoting.impl.netty.client.NettyClient;
import com.xxl.rpc.core.serializer.impl.JsonbSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl-rpc invoker config
 *
 * @author xuxueli 2018-10-19
 */
@Configuration
public class SpringXxlRpcBootstrapConfig {

    @Value("${xxl.conf.client.env}")
    private String env;

    @Value("${xxl.conf.client.appname}")
    private String appname;

    @Value("${xxl.conf.admin.address}")
    private String address;

    @Value("${xxl.conf.admin.accesstoken}")
    private String accesstoken;

    @Value("${xxl-rpc.invoker.open}")
    private boolean invokerOpen;

    @Value("${xxl-rpc.provider.open}")
    private boolean providerOpen;

    @Bean
    public SpringXxlRpcBootstrap xxlRpcSpringFactory() {

        // XxlRpc Bootstrap
        SpringXxlRpcBootstrap factory = new SpringXxlRpcBootstrap();
        factory.setBaseConfig(new BaseConfig(env, appname));
        factory.setRegister(new XxlConfRegister(address, accesstoken));
        factory.setInvokerConfig(new InvokerConfig(true, NettyClient.class, JsonbSerializer.class, null));
        factory.setProviderConfig(new ProviderConfig(providerOpen));

        return factory;
    }

}