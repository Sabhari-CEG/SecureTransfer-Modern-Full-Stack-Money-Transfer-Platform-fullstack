package com.sabhari.app.transaction.Signup;
import com.sabhari.app.transaction.Database.DatabaseConnection;
import com.sabhari.app.transaction.Signup.SignupResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("signup")
public class Signup {
    
    @PostMapping()
    public SignupResponse signup(@RequestBody SignupResponse formData) throws SQLException{
        SignupResponse res = new SignupResponse();
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();
        ResultSet rs = null;
        if (conn != null) {
            String sql = "SELECT * FROM users WHERE mobile = " + formData.getMobile();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    res.setStatus("This mobile number is already registered!");
                    return res;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            sql = "INSERT INTO `users` (`firstName`, `lastname`, `mobile`, `accountNo`, `password`) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, formData.getFirstName());
                pstmt.setString(2, formData.getLastName());
                pstmt.setString(3, formData.getMobile());
                pstmt.setString(4, formData.getAccNo());
                pstmt.setString(5, formData.getPassword());
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 1) {
                    res.setFirstName(formData.getFirstName());
                    res.setLastName(formData.getLastName());
                    res.setMobile(formData.getMobile());
                    res.setAccNo(formData.getAccNo());
                    res.setBalance(Float.parseFloat("0.00"));
                    res.setStatus("User succesfully signed up!");
                    return res;
                }
            }
            
        }



        return res;

    }
}
