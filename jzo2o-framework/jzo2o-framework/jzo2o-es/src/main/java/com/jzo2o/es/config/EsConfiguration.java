package com.jzo2o.es.config;

import cn.hutool.core.date.DatePattern;
import com.jzo2o.es.core.ElasticSearchTemplate;
import com.jzo2o.es.core.impl.ElasticSearchTemplateImpl;
import com.jzo2o.es.properties.EsProperties;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableConfigurationProperties(EsProperties.class)
public class EsConfiguration {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        MAPPER.registerModule(javaTimeModule);

    }

    @Bean
    public ElasticsearchClient esClient(EsProperties esProperties) {
        RestClient restClient = RestClient.builder(new HttpHost(esProperties.getHost(), esProperties.getPort())).build();
        // Create the transport with a Jackson mapper
        // 使用自定义json序列化
        JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper(MAPPER);

        ElasticsearchTransport transport = new RestClientTransport(restClient, jacksonJsonpMapper);
        // And create the API client
        return new ElasticsearchClient(transport);
    }

    @Bean
    public ElasticSearchTemplate template(ElasticsearchClient elasticsearchClient) {
        return new ElasticSearchTemplateImpl(elasticsearchClient);
    }
}
