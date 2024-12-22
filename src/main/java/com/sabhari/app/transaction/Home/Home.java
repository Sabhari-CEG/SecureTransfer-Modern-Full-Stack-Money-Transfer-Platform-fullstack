package com.sabhari.app.transaction.Home;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sabhari.app.transaction.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("home")
public class Home {
    @GetMapping(path = "/{userId}")
    public HomeResponse getMethodName(@PathVariable int userId) {
        HomeResponse res = new HomeResponse();
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;
        if (conn != null) {
            String sql = "SELECT * FROM users WHERE userId = " + userId;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    res.setUserId(rs.getInt("userId"));
                    res.setFirstname(rs.getString("firstName"));
                    res.setLastName(rs.getString("lastName"));
                    res.setAccNo(rs.getString("accountNo"));
                    res.setBalance(rs.getFloat("balance"));
                    return res;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
    

}
