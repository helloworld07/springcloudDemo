1注册中心服务流程：
`1`.一个服务注册中心，eureka server,端口为8761
`2`.service-hi工程跑了两个实例，端口分别为8762,8763，为服务**提供者**，
    分别向服务注册中心注册(分别启动两次，实际部署也是同一应用部署在两台服务器上)
`3`.service-ribbon端口为8764,为服务**消费者**，向服务注册中心注册
`4`.当service-ribbon通过restTemplate调用service-hi的hi接口时，
    因为用ribbon进行了负载均衡，会轮流的调用service-hi：8762和8763 两个端口的hi接口；

2service-feign由于版本问题，暂时无法使用。（莫名其妙过来一天就好了，mdzz）
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