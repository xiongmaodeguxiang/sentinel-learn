/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zl.learn.init1;

import com.alibaba.csp.sentinel.cluster.ClusterStateManager;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientAssignConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfigManager;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.util.AppNameUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.List;

/**
 * @author Eric Zhao
 */
public class ClusterInitFunc implements InitFunc {

    private static final String APP_NAME = AppNameUtil.getAppName();

    private final String remoteAddress = "localhost:8848";
    private final String groupId = "SENTINEL_GROUP";

    private final String flowDataId = APP_NAME +"-flow-rules";
    private final String paramDataId = APP_NAME + "-param-rules";
    private final String configDataId = APP_NAME + "-cluster-client-config";
    private final String clusterMapDataId = APP_NAME + "-cluster-map";
    private final String clusterStateDataId = APP_NAME + "-cluster-state";

    @Override
    public void init() throws Exception {
        //注册flowRule规则的监听
        initDynamicRule();

        initClusterClientConfig();

        initClusterServerAssignConfig();

        initStateConfig1();
    }

    private void initStateConfig() {
        ReadableDataSource<String, Integer> stateDataSource =  new NacosDataSource<Integer>(remoteAddress,
                groupId, clusterMapDataId, source -> {
            if(StringUtil.isEmpty(source)){
                return ClusterStateManager.CLUSTER_NOT_STARTED;
            }
            JSONObject jsonObject = JSON.parseObject(source);
            Integer mode = jsonObject.getInteger("mode");
            System.out.println("mode变为了："+mode);
            return mode;
        });
        ClusterStateManager.registerProperty(stateDataSource.getProperty());
    }
    private void initStateConfig1() {
        ReadableDataSource<String, Integer> stateDataSource =  new NacosDataSource<Integer>(remoteAddress,
                groupId, clusterStateDataId, source -> {
            if(StringUtil.isEmpty(source)){
                return ClusterStateManager.CLUSTER_NOT_STARTED;
            }

            return Integer.valueOf(source);
        });
        ClusterStateManager.registerProperty(stateDataSource.getProperty());
    }

    private void initClusterServerAssignConfig() {
        ReadableDataSource<String, ClusterClientAssignConfig> clusterConfigDataSource =  new NacosDataSource<>(remoteAddress,
                groupId, clusterMapDataId, source -> {
            ClusterClientAssignConfig clusterClientAssignConfig = JSON.parseObject(source, new TypeReference<ClusterClientAssignConfig>(){});
            System.out.println("clusterClientAssignConfig变为了："+ source);
            return clusterClientAssignConfig;
        });
        ClusterClientConfigManager.registerServerAssignProperty(clusterConfigDataSource.getProperty());
    }

    private void initClusterClientConfig() {
        ReadableDataSource<String, ClusterClientConfig> clusterConfigDataSource =  new NacosDataSource<>(remoteAddress,
                groupId, configDataId, source -> JSON.parseObject(source, new TypeReference<ClusterClientConfig>(){}));
        ClusterClientConfigManager.registerClientConfigProperty(clusterConfigDataSource.getProperty());
    }

    private void initDynamicRule() {
      ReadableDataSource<String, List<FlowRule>> flowDataSource =  new NacosDataSource<>(remoteAddress,
              groupId, flowDataId, source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>(){}));
      FlowRuleManager.register2Property(flowDataSource.getProperty());
    }


}