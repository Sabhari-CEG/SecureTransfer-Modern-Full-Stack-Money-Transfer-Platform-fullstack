package com.sabhari.app.transaction.Transaction;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sabhari.app.transaction.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("transaction")
public class Transactions {

    @PostMapping("/credit/{userId}")
    public TransactionResponse creditAmount(@RequestBody TransactionResponse formData,
    @PathVariable String userId) {
        TransactionResponse res =  new TransactionResponse();
        res.setType("credit");
        res.setAmount(formData.getAmount());
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();

       try {
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        
        String insertSql = "INSERT INTO transactionhistory (userId, amount, type, timestamp) VALUES (?, ?, ?, NOW())";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            pstmt.setString(1, userId);
            pstmt.setFloat(2, formData.getAmount());
            pstmt.setString(3, "credit");
            pstmt.executeUpdate();
        }
        
        String updateSql = "UPDATE users SET balance = balance + ? WHERE userId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            pstmt.setFloat(1, formData.getAmount());
            pstmt.setString(2, userId);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected == 1) {
                conn.commit();
                res.setSuccess(true);
            } else {
                conn.rollback();
                res.setSuccess(false);
            }
        }
        
    } catch (SQLException e) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }

    } finally {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
        
        return res;
    }
    
    @PostMapping("/transfer/{userId}")
public TransactionResponse transferMoney(@RequestBody TransactionResponse formData,
@PathVariable String userId) {
    TransactionResponse res = new TransactionResponse();
    res.setType("credit");
    res.setAmount(formData.getAmount());
    DatabaseConnection db = new DatabaseConnection();
    Connection conn = db.getConnection();
    int receiverUserId = 0;
    
    try {
        // Properly parameterized query
        String sql = "SELECT userId FROM users WHERE mobile = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, formData.getReceiverMobile());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                receiverUserId = rs.getInt("userId");
            } else {
                res.setSuccess(false);
                res.setMessage("Receiver not found");
                return res;
            }
        }

        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        
        // First transaction record
        String insertSql = "INSERT INTO transactionhistory (userId, amount, type, timestamp) VALUES (?, ?, ?, NOW())";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            pstmt.setString(1, userId);
            pstmt.setFloat(2, formData.getAmount());
            pstmt.setString(3, "debit");
            pstmt.executeUpdate();
        }

        // Second transaction record
        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            pstmt.setInt(1, receiverUserId);
            pstmt.setFloat(2, formData.getAmount());
            pstmt.setString(3, "credit");
            pstmt.executeUpdate();
        }
        
        // Update sender balance
        String updateSql = "UPDATE users SET balance = balance - ? WHERE userId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            pstmt.setFloat(1, formData.getAmount());
            pstmt.setString(2, userId);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected == 1) {
                // Update receiver balance
                String updateSql1 = "UPDATE users SET balance = balance + ? WHERE userId = ?";
                try (PreparedStatement pstmt1 = conn.prepareStatement(updateSql1)) {
                    pstmt1.setFloat(1, formData.getAmount());  // Fixed: using pstmt1 instead of pstmt
                    pstmt1.setInt(2, receiverUserId);
                    int rowsAffected1 = pstmt1.executeUpdate();
                    if (rowsAffected1 == 1) {
                        conn.commit();
                        res.setSuccess(true);
                        res.setMessage("Transfer successful");
                    }
                }
            } else {
                conn.rollback();
                res.setSuccess(false);
                res.setMessage("Sender update failed");
            }
        }
        
    } catch (SQLException e) {
        try {
            conn.rollback();
            res.setSuccess(false);
            res.setMessage("Transaction failed: " + e.getMessage());
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    } finally {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
    
    return res;
}

@GetMapping("/log/{userId}")
public ArrayList<LogResponse> getTransactionDetails(@PathVariable String userId) {
    ArrayList<LogResponse> res = new ArrayList<LogResponse>();
    DatabaseConnection db = new DatabaseConnection();
    Connection conn = db.getConnection();
    ResultSet rs;
    if (conn != null) {
        String sql = "SELECT * FROM transactionhistory WHERE userId = " + userId;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LogResponse temp = new LogResponse();
                temp.setType(rs.getString("type"));
                temp.setAmount(rs.getFloat("amount"));
                temp.setTime(rs.getTimestamp("timestamp"));
                res.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return res;
}


    
}


