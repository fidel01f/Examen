package com.example.reportgenerator.controller;

import com.example.reportgenerator.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reporte")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping
    public ResponseEntity<String> generarReporte(@RequestParam String fecha) {
        reportService.processReport(fecha);
        return ResponseEntity.ok("Procesando reportes para la fecha " + fecha);
    }
}