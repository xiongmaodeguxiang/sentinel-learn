
token-client1 的启动参数：
```shell
-Dcsp.sentinel.dashboard.server=127.0.0.1:8080 -Dnacos.server=127.0.0.1:8848 -Dproject.name=sentinel_group1 -Dcsp.sentinel.log.use.pid=true
```

token-client2 的启动参数：
```shell
-Dcsp.sentinel.dashboard.server=127.0.0.1:8080 -Dproject.name=app2 -Dcsp.sentinel.log.use.pid=true 
```
