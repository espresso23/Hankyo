package dao;

import model.ExpertBank;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpertBankDAO {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public ExpertBankDAO() {
    }

    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) DBConnect.getInstance().closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ExpertBank> getExpertBanks(int expertId) {
        List<ExpertBank> banks = new ArrayList<>();
        String sql = "SELECT * FROM ExpertBank WHERE expertID = ?";

        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, expertId);
            rs = ps.executeQuery();

            while (rs.next()) {
                ExpertBank bank = new ExpertBank(
                    rs.getInt("eBankID"),
                    rs.getString("bankName"),
                    rs.getString("bankAccount"),
                    rs.getString("binCode"),
                    rs.getInt("expertID")
                );
                banks.add(bank);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return banks;
    }

    public boolean addExpertBank(ExpertBank bank) {
        boolean success = false;
        String sql = "INSERT INTO ExpertBank (bankName, bankAccount, binCode, expertID) VALUES (?, ?, ?, ?)";

        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, bank.getBankName());
            ps.setString(2, bank.getBankAccount());
            ps.setString(3, bank.getBinCode());
            ps.setInt(4, bank.getExpertID());

            success = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return success;
    }

    public boolean updateExpertBank(ExpertBank bank) {
        boolean success = false;
        String sql = "UPDATE ExpertBank SET bankName = ?, bankAccount = ?, binCode = ? WHERE eBankID = ?";

        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, bank.getBankName());
            ps.setString(2, bank.getBankAccount());
            ps.setString(3, bank.getBinCode());
            ps.setInt(4, bank.geteBankID());

            success = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return success;
    }

    public boolean deleteExpertBank(int eBankId) {
        boolean success = false;
        String sql = "DELETE FROM ExpertBank WHERE eBankID = ?";

        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, eBankId);

            success = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return success;
    }

    public ExpertBank getExpertBankById(int eBankId) {
        ExpertBank bank = null;
        String sql = "SELECT * FROM ExpertBank WHERE eBankID = ?";

        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, eBankId);
            rs = ps.executeQuery();

            if (rs.next()) {
                bank = new ExpertBank(
                    rs.getInt("eBankID"),
                    rs.getString("bankName"),
                    rs.getString("bankAccount"),
                    rs.getString("binCode"),
                    rs.getInt("expertID")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return bank;
    }
} 