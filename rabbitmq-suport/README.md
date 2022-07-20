1.消息中间件工具类，其他模块如需使用rabbitmq，需要在pom中增加依赖

        <dependency>
			<groupId>eazybuilder.devops</groupId>
			<artifactId>rabbitmq-suport</artifactId>
			<version>1.0-SNAPSHOT</version>
		</dependency>

2.消息中间件的使用示例，参见rancher-suport工程。
SendMsgController为发送消息示例
ReceiveMsg为接受消息示例
yml配置文件中相关参数
bootstrap.yml,需要引入rabbitmq.yml
# 公用的配置文佳，如：redis,kafka的配置
shared-configs:
- data-id: cache.yml
group: dev
refresh: true
- data-id: redis-common.yml
group: dev
refresh: true
- data-id: rabbitmq.yml
group: dev
refresh: true
application.yml中（参照rancher-suport.yml）,需要引入相关配置
message:
#工程中需要使用的消息路由器
topicExchange: rancher_topic_exchange
#工程中需要使用的消息队列
queue: rancher_queue
#消息队列绑定的路由规则（接受符合规则的key）
routingKey: "rancher.#"
#消息发送到路由器后，处理类（beanName）,不配置，则使用默认的
confirmCallbackBean: eazybuilderRabbitmqConfirmCallbackService
#消息发送到路由器失败后，处理类（beanName）,不配置，则使用默认的
returnCallbackBean: