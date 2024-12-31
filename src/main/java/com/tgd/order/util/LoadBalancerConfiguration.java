package com.tgd.order.util;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class LoadBalancerConfiguration {

    @Bean
    public ServiceInstanceListSupplier customServiceInstanceListSupplier(Environment environment, DiscoveryClient discoveryClient,ConfigurableApplicationContext cac) {
        String serviceName = environment.getProperty("spring.application.name");
        return new CustomServiceInstanceListSupplier("CUSTOMER-MS",discoveryClient);
//        return ServiceInstanceListSupplier.builder().withDiscoveryClient().build(cac);
    }


    @Bean
    ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
                                                            LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty("spring.application.name");
        return new RoundRobinLoadBalancer(loadBalancerClientFactory.getLazyProvider("CUSTOMER-MS", ServiceInstanceListSupplier.class),
                name);
    }
}
