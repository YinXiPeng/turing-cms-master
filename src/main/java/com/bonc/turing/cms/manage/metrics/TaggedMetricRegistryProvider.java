package com.bonc.turing.cms.manage.metrics;

import com.github.sps.metrics.OpenTsdbReporter;
import com.github.sps.metrics.TaggedMetricRegistry;
import com.github.sps.metrics.opentsdb.OpenTsdb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * User: ycy
 * Date: 2018-07-07
 * Time: 14:35
 */
@Component
public class TaggedMetricRegistryProvider {

    @Value(value = "${web.service.monitor-mark-address}")
    private String opentsdb_addr;
    @Value(value = "${web.service.monitor-mark-enabled}")
    private boolean opentsdb_enabled;

    private static final Logger logger = LoggerFactory.getLogger(TaggedMetricRegistryProvider.class);


    private TaggedMetricRegistry taggedMetricRegistry;

    public TaggedMetricRegistry getTaggedMetricRegistry() {
        return taggedMetricRegistry;
    }

    @PostConstruct
    private void init() {
        if(opentsdb_enabled){
            this.taggedMetricRegistry = getDefaultMetricsRegistry();
        }

    }

    private TaggedMetricRegistry getDefaultMetricsRegistry() {
        try {
            Map<String, String> tags = new HashMap<>();
            tags.put("component", "activity");
            try {
                String hostName = InetAddress.getLocalHost().getHostName();
                tags.put("host", hostName);
            } catch (UnknownHostException e) {
                logger.error("Cannot resolve local host");
            }

            OpenTsdb openTsdb = OpenTsdb.forService(opentsdb_addr)
                    .withGzipEnabled(true)
                    .create();
            TaggedMetricRegistry metricRegistry = new TaggedMetricRegistry();
            OpenTsdbReporter.forRegistry(metricRegistry)
                    .withTags(tags)
                    .withBatchSize(20)
                    .build(openTsdb)
                    .start(10L, TimeUnit.SECONDS);
            logger.info("init metrics suc");
            return metricRegistry;
        } catch (Exception e) {
            logger.error("init metrics failed!", e);
        }
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }


}
