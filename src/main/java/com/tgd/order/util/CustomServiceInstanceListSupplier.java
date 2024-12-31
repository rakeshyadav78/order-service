package com.tgd.order.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
public class CustomServiceInstanceListSupplier implements ServiceInstanceListSupplier {

    private final String serviceId;
    private final DiscoveryClient discoveryClient;

    public CustomServiceInstanceListSupplier(String serviceId, DiscoveryClient discoveryClient){
        this.serviceId=serviceId;
        this.discoveryClient= discoveryClient;
    }

    @Override
    public String getServiceId() {
        return this.serviceId;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return Flux.just(fetchInstances());
    }

    private List<ServiceInstance> fetchInstances(){
        log.debug("fetching service instance, serviceId : {}",serviceId);
        List<ServiceInstance> instances=discoveryClient.getInstances(this.serviceId);
        log.debug("service instance data : {}",instances);
        return instances;
    }
}
