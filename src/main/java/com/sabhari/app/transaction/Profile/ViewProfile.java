package com.sabhari.app.transaction.Profile;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sabhari.app.transaction.Database.DatabaseConnection;
import com.sabhari.app.transaction.Signup.SignupResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("profile")
public class ViewProfile {
    @GetMapping("info/{userId}")
    public ProfileResponse getMethodName(@PathVariable int userId) {
        ProfileResponse res = new ProfileResponse();
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;
        if (conn != null) {
            String sql = "SELECT * FROM users WHERE userId = " + userId;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    res.setId(rs.getInt("userId"));
                    res.setFirstName(rs.getString("firstName"));
                    res.setLastname(rs.getString("lastName"));
                    res.setAccno(rs.getString("accountNo"));
                    res.setMobile(rs.getString("mobile"));
                    res.setPassword(rs.getString("password"));
                    return res;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @PostMapping("/edit/{userId}")
    public ProfileResponse postMethodName(@RequestBody ProfileResponse formData,
    @PathVariable int userId) throws SQLException {
        ProfileResponse res = new ProfileResponse();
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();
        if (conn != null) {
            String sql = "UPDATE `users` SET `firstName` = ?, `lastName` = ?, `mobile` = ?, `accountNo` = ?, `password` = ? WHERE userId = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, formData.getFirstName());
                pstmt.setString(2, formData.getLastname());
                pstmt.setString(3, formData.getMobile());
                pstmt.setString(4, formData.getAccno());
                pstmt.setString(5, formData.getPassword());
                pstmt.setInt(6, userId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 1) {
                    res.setFirstName(formData.getFirstName());
                    res.setLastname(formData.getLastname());
                    res.setMobile(formData.getMobile());
                    res.setAccno(formData.getAccno());
                    res.setPassword(formData.getPassword());
                    return res;
                }
            }
        }
        
        
        return res;
    }

    @PostMapping("/delete/{userId}")
    public String postMethodName( @PathVariable int userId) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();
        if (conn != null) {
            String sql = "DELETE FROM `users` WHERE userId = " + userId;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 1) {
                    return "User is succesfully deleted!";
                }
        }
        
        
           }
           return "Some error in deleting!";
}
    
    

    
    
}
