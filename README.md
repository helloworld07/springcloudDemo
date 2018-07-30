1注册中心服务流程：
`1`.一个服务注册中心，eureka server,端口为8761
`2`.service-hi工程跑了两个实例，端口分别为8762,8763，为服务**提供者**，
    分别向服务注册中心注册(分别启动两次，实际部署也是同一应用部署在两台服务器上)
`3`.service-ribbon端口为8764,为服务**消费者**，向服务注册中心注册
`4`.当service-ribbon通过restTemplate调用service-hi的hi接口时，
    因为用ribbon进行了负载均衡，会轮流的调用service-hi：8762和8763 两个端口的hi接口；

2service-feign与service-ribbon功能类似，为服务消费者：
    Feign默认集成了Ribbon，并和Eureka结合，默认实现了负载均衡的效果

3.在service-ribbon中加入断路器Hystrix：
    作用：当某台服务器访问不通时，可以直接返回一个固定值。防止连锁故障。
    操作：a.加入maven
         b.加入@HystrixCommand(fallbackMethod = "调用的方法名")
         
4.在service-zuul中配置了路由：
    通过配置文件，可以配置访问zuul时不同的地址访问不同的工程。
    如：http://localhost:8769/api-a/hi?name=root访问的是service-ribbon 
  还配置了过滤器：
    在filter中进行配置，在访问前检测token是否有值，若无则直接返回。
    
5.config-server和config-client分别作为配置服务器的服务端和客户端。
    服务端连接git服务器，客户端读取数据。
    实现配置放在远程仓库，便于修改。
    注意application.properties的配置，指明文件路径。
    
    config-client中是bootstrap.properties。会比application.properties先加载。
   
6.将config改为eureka负载均衡：
    eurerka-config与config-server构成服务器与客户机，负责访问多台config-server服务器时是均衡的。
    config-server与config-client构成服务器与客户机，作用于5一样。
    相当于多加一层负载均衡，原来的config-client访问config-server变成均衡访问多台config-server。
    
7.在config-client中加入了rabbitMQ：
    配置：需要加入依赖和bootstrap.properties中的配置。
    作用：在远程仓库里修改了配置信息后，可以通过MQ刷新，去重新读取配置信息：
            方法：直接发送POST请求http://localhost:8881/actuator/bus-refresh
    过程：当git文件更改的时候，通过请求/bus/refresh发送一个消息，由消息总线向其他服务传递，
          从而使整个微服务集群都达到更新配置文件。
          
8.在总项目下引入了zipkin-server-2.10.1-exec.jar。可直接启动用于**链路追踪**。
    访问：http://localhost:9411/zipkin/
    通过service-hello和service-miya的调用，可以在zipkin里反应出链路的情况。
    配置：需要配置maven依赖，需要启动zipkin-server-2.10.1-exec.jar服务包。有调用时才会追踪到。
    
9.集群化注册中心：
    配置：hosts配置计算机名peer1/peer2，在启动项里环境变量里配置spring.profiles.active=peer1/peer2
            用于启动两台注册中心
    Eureka-eserver peer1 8761,Eureka-eserver peer2 8769相互感应，
    当有服务注册时，两个Eureka-eserver是对等的，它们都存有相同的信息，
    通过服务器的数量来增加可靠性，当有一台服务器宕机了，服务并不会终止，因为另一台服务存有相同的数据。
    
10.断路器监控Hystrix Dashboard：在原service-hi中加入maven依赖和相应Application注解。
    （貌似需要在配置文件里开启跨域）
        可图形化显示访问断路情况，可单独作为一个工程，在需要监控的项目里加注解，
        集群监控：（service-hii与hi配置相同）
            service-hi和service-hii作为提供者，service-turbine作为集群监控者：
                在http://localhost:8762/hystrix或http://localhost:8763/hystrix中输入：
                    http://localhost:8766/turbine.stream即可监控
             
    
11.
    
    
          
注册中心：eureka-server      8761
    集群化注册中心           8761;8762
服务提供者：service-hi       8762;8763(service-hii)
服务消费者：service-ribbon   8764
           service-feign    8765
断路器监控：service-turbine  8766
路由：service-zuul           8769


配置中心：eureka-config      8889
配置服务端：config-server     8888
配置客户端：config-client     8882

消息总线rabbitMQ:http://localhost:15672

链路追踪调用：service-hello     8988
             service-miya      8989
             
