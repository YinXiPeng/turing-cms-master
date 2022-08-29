package com.bonc.turing.cms.health.controller;

import com.bonc.turing.cms.health.service.IHealthCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LY
 * @date 2019.07.11
 * @desc 健康检查
 */
@RestController
@RequestMapping({"/"})
public class HealthCheck {
    @Autowired
    IHealthCheck iHealthCheck;

    @RequestMapping(value = "healthcheck", method = RequestMethod.GET)
    public String healthCheck() {
        return iHealthCheck.healthCheck();
    }
}
