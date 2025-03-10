package com.example.reportgenerator.service;

import com.example.reportgenerator.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void processReport(String fecha) {
        List<Map<String, Object>> campaigns = jdbcTemplate.queryForList("EXEC GetCampaignsByDate ?", fecha);

        for (Map<String, Object> campaign : campaigns) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, campaign.get("id"));
        }
    }
}
