package com.sabhari.app.transaction.HealthCheck;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import com.sabhari.app.transaction.Database.DatabaseConnection;

import org.springframework.web.bind.annotation.GetMapping;
import java.sql.Connection;



@RestController
@RequestMapping("healthcheck")
public class HealthCheck {
    LocalTime currentTime = LocalTime.now();
    LocalDate today = LocalDate.now();
    @GetMapping("")
    public String healthCheck() {
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();
        String res = "HealthCheck succesfully! Backend initiated at "  + today.toString() + ", "+currentTime.toString();
        if (conn != null) {
           res += "\ndb Connected Succesfully";
        }
        return res;
    }
    
}
