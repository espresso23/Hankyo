package dao;

import model.ExpertBank;
import util.DBConnect;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpertBankDAO {
    
    public boolean addExpertBank(ExpertBank expertBank) {
        String sql = "INSERT INTO ExpertBankAccount (BankAccount, BankName, BinCode, ExpertID) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, expertBank.getBankAccount());
            ps.setString(2, expertBank.getBankName());
            ps.setString(3, expertBank.getBinCode());
            ps.setInt(4, expertBank.getExpertID());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public ExpertBank getExpertBankById(int bankId) {
        String sql = "SELECT * FROM ExpertBankAccount WHERE eBankID = ?";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, bankId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ExpertBank(
                        rs.getInt("eBankID"),
                        rs.getString("BankName"),
                        rs.getString("BankAccount"),
                        rs.getString("BinCode"),
                        rs.getInt("ExpertID")
                    );
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean updateExpertBank(ExpertBank expertBank) {
        String sql = "UPDATE ExpertBankAccount SET BankAccount = ? WHERE eBankID = ? AND ExpertID = ?";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, expertBank.getBankAccount());
            ps.setInt(2, expertBank.geteBankID());
            ps.setInt(3, expertBank.getExpertID());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteExpertBank(int bankId) {
        String sql = "DELETE FROM ExpertBankAccount WHERE eBankID = ?";

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, bankId);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<ExpertBank> getExpertBanksByExpertId(int expertId) {
        String sql = "SELECT * FROM ExpertBankAccount WHERE ExpertID = ?";
        List<ExpertBank> banks = new ArrayList<>();

        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, expertId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    banks.add(new ExpertBank(
                        rs.getInt("eBankID"),
                        rs.getString("BankName"),
                        rs.getString("BankAccount"),
                        rs.getString("BinCode"),
                        rs.getInt("ExpertID")
                    ));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return banks;
    }
} 