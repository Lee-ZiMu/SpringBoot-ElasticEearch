package org.example.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Version v1.0
 * @Description:
 * @Author Lee
 * @Date 2023/9/10 16:35
 * @Copyright 李子木
 */
@Configuration
public class ElasticSearchConfig {

    @Value("${spring.elasticsearch.ip}")
    private String esIp;

    @Value("${spring.elasticsearch.port}")
    private Integer port;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(esIp, port, "http")
                )
        );
        return client;
    }
}

