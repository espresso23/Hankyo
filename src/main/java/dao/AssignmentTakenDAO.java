package dao;

import model.AssignmentTaken;
import util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentTakenDAO {

    public boolean addAssignmentTaken(AssignmentTaken taken) {
        String sql = "INSERT INTO Assignment_Taken (assignmentID, learnerID, dateCreated, finalMark, skipQues, doneQues) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, taken.getAssignmentID());
            pstmt.setInt(2, taken.getLearnerID());
            pstmt.setTimestamp(3, new java.sql.Timestamp(taken.getDateCreated().getTime()));
            pstmt.setFloat(4, taken.getFinalMark());
            pstmt.setInt(5, taken.getSkipQues());
            pstmt.setInt(6, taken.getDoneQues());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        taken.setAssignTakenID(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean updateAssignmentTaken(AssignmentTaken taken) {
        String sql = "UPDATE Assignment_Taken SET finalMark = ?, skipQues = ?, doneQues = ? WHERE assignTakenID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setFloat(1, taken.getFinalMark());
            pstmt.setInt(2, taken.getSkipQues());
            pstmt.setInt(3, taken.getDoneQues());
            pstmt.setInt(4, taken.getAssignTakenID());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    public AssignmentTaken getAssignmentTakenById(int takenID) {
        String sql = "SELECT * FROM Assignment_Taken WHERE assignTakenID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, takenID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToAssignmentTaken(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public List<AssignmentTaken> getAssignmentTakenByLearner(int learnerID) {
        List<AssignmentTaken> takenList = new ArrayList<>();
        String sql = "SELECT * FROM Assignment_Taken WHERE learnerID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, learnerID);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                takenList.add(mapResultSetToAssignmentTaken(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return takenList;
    }

    public AssignmentTaken getAssignmentTakenByLearnerAndAssignment(int learnerID, int assignmentID) {
        String sql = "SELECT * FROM Assignment_Taken WHERE learnerID = ? AND assignmentID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, learnerID);
            pstmt.setInt(2, assignmentID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToAssignmentTaken(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    private AssignmentTaken mapResultSetToAssignmentTaken(ResultSet rs) throws SQLException {
        AssignmentTaken taken = new AssignmentTaken();
        taken.setAssignTakenID(rs.getInt("assignTakenID"));
        taken.setAssignmentID(rs.getInt("assignmentID"));
        taken.setLearnerID(rs.getInt("learnerID"));
        taken.setDateCreated(rs.getTimestamp("dateCreated"));
        taken.setFinalMark(rs.getFloat("finalMark"));
        taken.setSkipQues(rs.getInt("skipQues"));
        taken.setDoneQues(rs.getInt("doneQues"));
        return taken;
    }

    /**
     * Lấy thông tin bài làm theo ID
     * 
     * @param takenID ID của bài làm
     * @return Đối tượng AssignmentTaken hoặc null nếu không tìm thấy
     */
    public AssignmentTaken getAssignmentTakenByID(int takenID) {
        String sql = "SELECT * FROM Assignment_Taken WHERE assignTakenID = ?";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, takenID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToAssignmentTaken(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public AssignmentTaken getLatestAssignmentTaken(int learnerID, int assignmentID) {
        String sql = "SELECT TOP 1 * FROM Assignment_Taken " +
                    "WHERE learnerID = ? AND assignmentID = ? " +
                    "ORDER BY dateCreated DESC";
        
        try (Connection conn = DBConnect.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, learnerID);
            pstmt.setInt(2, assignmentID);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAssignmentTaken(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
} 