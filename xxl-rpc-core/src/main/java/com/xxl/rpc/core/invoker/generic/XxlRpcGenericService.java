package com.xxl.rpc.core.invoker.generic;

/**
 * @author xuxueli 2018-12-04
 */
public interface XxlRpcGenericService {

    /**
     * generic invoke       TODO-1，泛化调用
     *
     * @param iface                 iface name
     * @param version               iface version
     * @param method                method name
     * @param parameterTypes        parameter types, limit base type like "int、java.lang.Integer、java.util.List、java.util.Map ..."
     * @param args
     * @return
     */
    public Object invoke(String iface,
                         String version,
                         String method,
                         String[] parameterTypes,
                         Object[] args);

}