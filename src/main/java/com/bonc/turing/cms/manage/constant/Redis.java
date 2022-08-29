package com.bonc.turing.cms.manage.constant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class Redis {
    /*redis配置*/
    @Value(value = "${spring.redis.nodes}")
    private String host;
    @Value(value = "${spring.redis.password}")
    private String auth;
}
