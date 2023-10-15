package com.doksanbir.ratelimiter.config;

import com.doksanbir.ratelimiter.controller.DynamicAlgorithmController;
import com.doksanbir.ratelimiter.model.AlgorithmType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


@Component
@RequiredArgsConstructor
public class DynamicEndpointConfigurer implements ApplicationListener<ContextRefreshedEvent> {


    private final RequestMappingHandlerMapping requestMappingHandlerMapping;


    private final DynamicAlgorithmController dynamicAlgorithmController;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        try {
            RequestMappingInfo requestMappingInfo = RequestMappingInfo
                    .paths("/dynamic/customAlgorithm")
                    .methods(RequestMethod.GET)
                    .build();

            this.requestMappingHandlerMapping.registerMapping(requestMappingInfo,
                    dynamicAlgorithmController,
                    DynamicAlgorithmController.class.getDeclaredMethod("handleDynamicAlgorithm", AlgorithmType.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
