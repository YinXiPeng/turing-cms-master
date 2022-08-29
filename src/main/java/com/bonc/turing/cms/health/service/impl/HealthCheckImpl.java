package com.bonc.turing.cms.health.service.impl;

import com.bonc.turing.cms.health.service.IHealthCheck;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckImpl implements IHealthCheck{
    @Override
    public String healthCheck() {
        return "OK";
    }
}
