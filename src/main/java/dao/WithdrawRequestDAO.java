package dao;

import model.WithdrawRequest;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WithdrawRequestDAO {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public WithdrawRequestDAO() {
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

    public boolean createWithdrawRequest(WithdrawRequest request) {
        boolean success = false;
        String sql = "INSERT INTO WithdrawRequest (expertID, amount, requestDate, status, note, eBankID) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, request.getExpertID());
            ps.setDouble(2, request.getAmount());
            ps.setTimestamp(3, Timestamp.valueOf(request.getRequestDate()));
            ps.setString(4, request.getStatus());
            ps.setString(5, request.getNote());
            ps.setInt(6, request.geteBankID());

            success = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return success;
    }

    public List<WithdrawRequest> getWithdrawRequestsByExpertId(int expertId) {
        List<WithdrawRequest> requests = new ArrayList<>();
        String sql = "SELECT wr.*, eb.bankName, eb.bankAccount "
                + "FROM WithdrawRequest wr "
                + "JOIN ExpertBank eb ON wr.eBankID = eb.eBankID "
                + "WHERE wr.expertID = ? "
                + "ORDER BY wr.requestDate DESC";

        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, expertId);
            rs = ps.executeQuery();

            while (rs.next()) {
                WithdrawRequest request = new WithdrawRequest();
                request.setRequestID(rs.getInt("requestID"));
                request.setExpertID(rs.getInt("expertID"));
                request.setAmount(rs.getDouble("amount"));
                request.setRequestDate(rs.getTimestamp("requestDate").toLocalDateTime());
                request.setStatus(rs.getString("status"));
                request.setNote(rs.getString("note"));
                request.seteBankID(rs.getInt("eBankID"));
                request.setBankName(rs.getString("bankName"));
                request.setBankAccount(rs.getString("bankAccount"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return requests;
    }

    public boolean updateWithdrawRequestStatus(int requestId, String status, String note) {
        boolean success = false;
        String sql = "UPDATE WithdrawRequest SET status = ?, note = ? WHERE requestID = ?";

        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, note);
            ps.setInt(3, requestId);

            success = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return success;
    }

    public WithdrawRequest getWithdrawRequestById(int requestId) {
        WithdrawRequest request = null;
        String sql = "SELECT wr.*, eb.bankName, eb.bankAccount "
                + "FROM WithdrawRequest wr "
                + "JOIN ExpertBank eb ON wr.eBankID = eb.eBankID "
                + "WHERE wr.requestID = ?";

        try {
            conn = DBConnect.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, requestId);
            rs = ps.executeQuery();

            if (rs.next()) {
                request = new WithdrawRequest();
                request.setRequestID(rs.getInt("requestID"));
                request.setExpertID(rs.getInt("expertID"));
                request.setAmount(rs.getDouble("amount"));
                request.setRequestDate(rs.getTimestamp("requestDate").toLocalDateTime());
                request.setStatus(rs.getString("status"));
                request.setNote(rs.getString("note"));
                request.seteBankID(rs.getInt("eBankID"));
                request.setBankName(rs.getString("bankName"));
                request.setBankAccount(rs.getString("bankAccount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return request;
    }
} 