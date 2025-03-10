package com.example.reportgenerator.worker;

import com.example.reportgenerator.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class ReportWorker {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void generateReport(int campaignId) {
        String fileName = "reportes/campaña_" + campaignId + ".csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("ID, Teléfono, Mensaje, Estado\n");
            int offset = 0, limit = 10000;
            while (true) {
                List<Map<String, Object>> details = jdbcTemplate.queryForList("EXEC GetCampaignDetails ?, ?, ?", campaignId, offset, limit);
                if (details.isEmpty()) break;
                for (Map<String, Object> row : details) {
                    writer.write(row.get("id") + "," + row.get("telefono") + "," + row.get("mensaje") + "," + row.get("estado") + "\n");
                }
                offset += limit;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
