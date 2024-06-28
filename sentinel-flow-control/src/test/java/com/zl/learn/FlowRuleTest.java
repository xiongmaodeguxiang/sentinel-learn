package com.zl.learn;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Unit test for simple App.
 */
public class FlowRuleTest
{
    /**
     * 测试直接流控
     */
    @Test
    public void testDirectFlowRule()
    {
        ContextUtil.enter("node1","app1");
        inDirectFlowRules();
        int i = 0;
        while (i++ < 50){
            try (Entry entry = SphU.entry("HelloWord")){
                System.out.println("hello word");
            } catch (BlockException e) {
                System.out.println("blocked");
            }
        }
        ContextUtil.exit();
    }

    /**
     * 测试链路流控
     */
    @Test
    public void testChainFlowRule()
    {
        ContextUtil.enter("node1","app2");
        inChainFlowRules();
        int i = 0;
        while (i++ < 50){
            try (Entry entry = SphU.entry("HelloWord")){
                System.out.println("hello word");
            } catch (BlockException e) {
                System.out.println("blocked");
            }
        }
        ContextUtil.exit();
    }

    /**
     * 测试关联链路流控
     */
    @Test
    public void testRelateFlowRule()
    {
        inRelateFlowRules();
        new Thread(()->{
            int i = 0;
            while (i++ < 100){
                try (Entry entry = SphU.entry("HelloWord2")){
                    System.out.println("HelloWord2");
                } catch (BlockException e) {
                    System.out.println("HelloWord2 Blocked");
                }
                ContextUtil.exit();
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(()->{
            int i = 0;
            while (i++ < 500){
                try (Entry entry = SphU.entry("HelloWord")){
                    System.out.println("hello word");
                } catch (BlockException e) {
                    System.out.println(" HelloWord blocked");
                }
                ContextUtil.exit();
                try {
                    TimeUnit.MILLISECONDS.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        while (true);
    }

    /**
     * 测试冷启动
     */
    @Test
    public void testWarmUp(){
        inDirectWarmUpFlowRules();
        int i = 0;
        while (i++ < 5000){
            try (Entry entry = SphU.entry("HelloWord")){
                System.out.println("hello word");
            } catch (BlockException e) {
                System.out.println("blocked");
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        ContextUtil.exit();
    }

    /**
     * 测试匀速流控，每秒允许通过的请求
     */
    @Test
    public void testRate(){
        inDirectRateFlowRules();
        int i = 0;
        while (i++ < 5000){
            try (Entry entry = SphU.entry("HelloWord")){
                System.out.println("hello word");
            } catch (BlockException e) {
                System.out.println("blocked");
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        ContextUtil.exit();
    }

    /**
     * 如果某个方法每秒最多调用1次，如果MaxQueueingTimeMs（队列中等待的时间超过设置的值）则会触发限流
     */
    private void inDirectRateFlowRules() {
        List<FlowRule> flowRules = new ArrayList<>();
        FlowRule flowRule = new FlowRule();
        flowRule.setResource("HelloWord");
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setCount(5);
        flowRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        flowRule.setMaxQueueingTimeMs(500);
        flowRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER);
        flowRules.add(flowRule);
        FlowRuleManager.loadRules(flowRules);
    }

    private  void inDirectWarmUpFlowRules() {
        List<FlowRule> flowRules = new ArrayList<>();
        FlowRule flowRule = new FlowRule();
        flowRule.setResource("HelloWord");
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setCount(11);
        flowRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        flowRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP);
        //冷启动的时间间隔为2s，也就是2s内达到阈值
        flowRule.setWarmUpPeriodSec(20);
        flowRules.add(flowRule);
        FlowRuleManager.loadRules(flowRules);
    }

    /**
     * 对资源HelloWord2设置限流，限流的条件：是关联的接口达到阈值比如HelloWord的qps达到2时对HelloWord2进行限流
     * 使用场景，比如有一个write_db操作与read_db，由于write_db的优先级比read_db高，因为可能涉及到下订单的操作
     * 因此对read_db操作进行限流，限流的条件式write_db的操作的qps达到某个数，因为达到某个数说明写操作并发量比较高
     * 需要将机器的性能分配给写操作，减少read_db的操作带来的性能
     */
    private  void inRelateFlowRules() {
        List<FlowRule> flowRules = new ArrayList<>();
        FlowRule flowRule = new FlowRule();
        flowRule.setResource("HelloWord2");
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setCount(2);
        flowRule.setStrategy(RuleConstant.STRATEGY_RELATE);
        flowRule.setRefResource("HelloWord");
        flowRules.add(flowRule);
        FlowRuleManager.loadRules(flowRules);
    }


    private  void inDirectFlowRules() {
        List<FlowRule> flowRules = new ArrayList<>();
        FlowRule flowRule = new FlowRule();
        flowRule.setResource("HelloWord");
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setCount(1);
        flowRule.setLimitApp("app1");
        flowRule.setStrategy(RuleConstant.STRATEGY_DIRECT);
        flowRules.add(flowRule);
        FlowRuleManager.loadRules(flowRules);
    }
    private  void inChainFlowRules() {
        List<FlowRule> flowRules = new ArrayList<>();
        FlowRule flowRule = new FlowRule();
        flowRule.setResource("HelloWord");
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setCount(20);
        flowRule.setLimitApp("app1");
        flowRule.setStrategy(RuleConstant.STRATEGY_CHAIN);
        flowRules.add(flowRule);
        FlowRuleManager.loadRules(flowRules);
    }

}
