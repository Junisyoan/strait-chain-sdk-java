# 海峡链开发者平台Java开发工具包

## StraitChainClient 客户端使用说明

1. 将com.shangchain.straitchain下的源文件复制粘贴到对应的工程中，方便使用和修改
2. 初始化参数 com.shangchain.straitchain.config.StraitChainInit
```java
@Configuration
public class StraitChainInit {
    @Bean
    public StraitChainClient init(){
        StraitChainClient client = new StraitChainClient();
        client.setAppId(appId);
        client.setAppKey(appKey);
        // 这里url不需要加请求后缀，在client中会加上后缀/api/develop/straits/action
        client.setUrl(url);
        return client;
    }
}
```
3. 异常捕获，sdk没有对client请求进行全局异常的捕获，需要使用者在使用的时候对异常进行捕获处理
4. 代码示例 src/test/com.shangchain.straitchain

## 错误码 
com.shangchain.straitchain.constants.StraitChainErrorConstant

| 错误码   | 描述       |
|-------|----------|
| 10002 | 参数异常     |
| 10006 | 业务操作异常   |
| 10007 | 铸造等待中    |
| 20001 | 系统级别异常   |
| 20006 | 签名验签异常   |
| 30003 | 调用合约返回失败 |
| 30004 | 接口开放限制   |
