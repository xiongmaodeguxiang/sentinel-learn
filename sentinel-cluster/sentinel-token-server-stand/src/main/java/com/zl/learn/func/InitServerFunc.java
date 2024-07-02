package com.zl.learn.func;

import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterParamFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.server.config.ClusterServerConfigManager;
import com.alibaba.csp.sentinel.cluster.server.config.ServerTransportConfig;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Set;

public class InitServerFunc implements InitFunc {
    public static final String NACOS_ADDR = "localhost";
    public static final String NACOS_NAMESPACE = "SENTINEL_GROUP";
    public static final String APP_NAME = "cluster-server";
    public static final String NAME_SPACE_SET_DATAID_SUFFIX = "-namespace-set";
    public static final String SERVER_TRANSPORT_CONFIG_SUFFIX = "-transport-config";

    public static final String FLOW_RULE_SUFFIX = "-flow-rules";
    public static final String PARAM_FLOW_RULE_SUFFIX = "-param-rules";
    @Override
    public void init() throws Exception {

//        //注册普通集群限流
//        ClusterFlowRuleManager.setPropertySupplier(namespace -> {
//            ReadableDataSource<String, List<FlowRule>> flowRuleSource = new NacosDataSource<>(NACOS_ADDR, NACOS_NAMESPACE, namespace + FLOW_RULE_SUFFIX,
//                    source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
//            return flowRuleSource.getProperty();
//        });
//
//        //注册热点参数限流规则
//        ClusterParamFlowRuleManager.setPropertySupplier(namespace -> {
//            ReadableDataSource<String, List<ParamFlowRule>> paramFlowRuleSource = new NacosDataSource<>(NACOS_ADDR, NACOS_NAMESPACE, namespace + PARAM_FLOW_RULE_SUFFIX, source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {}));
//            return paramFlowRuleSource.getProperty();
//        });
//
//        //初始化nameSet
//        ReadableDataSource<String, Set<String>> nameSetDataSource = new NacosDataSource<Set<String>>(NACOS_ADDR, NACOS_NAMESPACE, APP_NAME + NAME_SPACE_SET_DATAID_SUFFIX,
//                source -> JSON.parseObject(source, new TypeReference<Set<String>>() {}));
//        ClusterServerConfigManager.registerNamespaceSetProperty(nameSetDataSource.getProperty());
//        //初始化transportConfig
//        ReadableDataSource<String,ServerTransportConfig> serverTransPortDataSource = new NacosDataSource<>(NACOS_ADDR, NACOS_NAMESPACE, APP_NAME + SERVER_TRANSPORT_CONFIG_SUFFIX,
//                source -> JSON.parseObject(source, new TypeReference<ServerTransportConfig>() {}));
//        ClusterServerConfigManager.registerServerTransportProperty(serverTransPortDataSource.getProperty());

    }
}
