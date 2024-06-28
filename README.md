nacos中
group1-server-namespace-set的的内容
```json
["app1","app2"]
```
group1-server-transport-config中内容
```json
{
    "port": 9000,
    "idleSeconds": 600
}
```
group1-cluster-client-config内容
```json
{
    "requestTimeout": 200
}
```
group1-ref-token-server内容为：
```json
{
    "serverHost": "192.168.28.5",
    "serverPort": 9000,
    "mode": 0
}
```
token-client1 的启动参数：
```shell
-Dcsp.sentinel.dashboard.server=127.0.0.1:8080 -Dproject.name=app1 -Dcsp.sentinel.log.use.pid=true
```

token-client2 的启动参数：
```shell
-Dcsp.sentinel.dashboard.server=127.0.0.1:8080 -Dproject.name=app2 -Dcsp.sentinel.log.use.pid=true
```
