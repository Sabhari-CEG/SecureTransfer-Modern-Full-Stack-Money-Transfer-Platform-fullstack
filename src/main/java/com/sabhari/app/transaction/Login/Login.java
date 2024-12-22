package com.sabhari.app.transaction.Login;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sabhari.app.transaction.Database.DatabaseConnection;
import com.sabhari.app.transaction.Signup.SignupResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("login")
public class Login {

    @GetMapping()
    public SignupResponse getMethodName(@RequestParam(value = "mobile", required = true) String mobile,
    @RequestParam(value = "password", required = true) String password
    ) {
        SignupResponse res = new SignupResponse();
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;
        if (conn != null) {
            String sql = "SELECT * FROM users WHERE mobile = " + mobile;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (!rs.getString("password").equals(password)){
                        res.setStatus("Credentials does not match");
                        return res;
                    }
                    res.setId(rs.getInt("userId"));
                    res.setFirstName(rs.getString("firstName"));
                    res.setLastName(rs.getString("lastName"));
                    res.setMobile(rs.getString("mobile"));
                    res.setAccNo(rs.getString("accountNo"));
                    res.setBalance(rs.getFloat("balance"));
                    res.setPassword(rs.getString("password"));
                    res.setStatus("User found");

                    if (rs.getString("password").equals(password)){
                        return res;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
       return res; 
    }
    
    
}
