package dao;

import model.Expert;
import model.ExpertBank;
import util.DBConnect;
import util.Encrypt;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class ExpertDAO {
    private Connection connection;

    public ExpertDAO() {
        this.connection = DBConnect.getInstance().getConnection();
    }

    public boolean createExpert(Expert expert) throws SQLException {
        String insertUserQuery = "INSERT INTO [User] (username, password, gmail, phone, role, status, fullName, socialID, dateCreate, gender, avatar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertLearnerQuery = "INSERT INTO Expert (userID, certificate, honour_ownedID, CCCD) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            // Mã hóa password trước khi lưu
            expert.setPassword(Encrypt.hashPassword(expert.getPassword()));

            // Insert into Users table
            try (PreparedStatement userStmt = connection.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS)) {
                userStmt.setString(1, expert.getUsername());
                userStmt.setString(2, expert.getPassword());
                userStmt.setString(3, expert.getGmail());
                userStmt.setString(4, expert.getPhone());
                userStmt.setString(5, expert.getRole());
                userStmt.setString(6, expert.getStatus());
                userStmt.setString(7, expert.getFullName());
                userStmt.setString(8, expert.getSocialID());
                userStmt.setDate(9, new java.sql.Date(expert.getDateCreate().getTime()));
                userStmt.setString(10, expert.getGender());
                userStmt.setString(11, expert.getAvatar());
                userStmt.executeUpdate();

                ResultSet generatedKeys = userStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    expert.setUserID(generatedKeys.getInt(1));
                }
            }

            // Insert into Expert table
            try (PreparedStatement expertStmt = connection.prepareStatement(insertLearnerQuery)) {
                expertStmt.setInt(1, expert.getUserID());
                expertStmt.setString(2, expert.getCertificate());
                expertStmt.setInt(3, Types.NULL);
                expertStmt.setString(4, expert.getCCCD());
                expertStmt.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private Expert mapResultSetToExpert(ResultSet rs) throws SQLException {
        Expert expert = new Expert();

        expert.setUserID(rs.getInt("userID"));
        expert.setUsername(rs.getString("username") != null ? rs.getString("username") : "");
        expert.setPassword(rs.getString("password") != null ? rs.getString("password") : "");
        expert.setGmail(rs.getString("gmail") != null ? rs.getString("gmail") : "");
        expert.setPhone(rs.getString("phone") != null ? rs.getString("phone") : "");
        expert.setRole(rs.getString("role") != null ? rs.getString("role") : "");
        expert.setStatus(rs.getString("status") != null ? rs.getString("status") : "");
        expert.setFullName(rs.getString("fullName") != null ? rs.getString("fullName") : "");
        expert.setSocialID(rs.getString("socialID") != null ? rs.getString("socialID") : "");

        if (rs.getDate("dateCreate") != null) {
            expert.setDateCreate(new Date(rs.getDate("dateCreate").getTime()));
        } else {
            expert.setDateCreate(null);
        }

        expert.setGender(rs.getString("gender") != null ? rs.getString("gender") : "");
        expert.setCertificate(rs.getString("certificate") != null ? rs.getString("certificate") : "");
        expert.setCCCD(rs.getString("CCCD") != null ? rs.getString("CCCD") : "");

        int honourOwnedID = rs.getInt("honour_ownedID");
        if (rs.wasNull()) {
            honourOwnedID = 0;
        }
        expert.setHonour_ownedID(honourOwnedID);

        expert.setExpertID(rs.getInt("expertID"));

        return expert;
    }

    public Expert getExpertById(int expertID) throws SQLException {
        String selectExpertQuery = "SELECT e.*, u.* FROM Expert e JOIN [User] u ON e.userID = u.userID WHERE e.expertID = ?";
        try (Connection connection = DBConnect.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(selectExpertQuery)) {
            stmt.setInt(1, expertID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToExpert(rs);
            }
        }
        return null;
    }

    public Expert getExpertByUserID(int userID) {
        String sql = "SELECT e.*, u.* FROM Expert e JOIN [User] u ON e.userID = u.userID WHERE e.userID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToExpert(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addNewBankAccount(ExpertBank expertBank) {
        String sql = "Insert into ExpertBankAccount(bankAccount,binCode,bankName,expertID) values(?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, expertBank.getBankAccount());
            preparedStatement.setString(2, expertBank.getBinCode());
            preparedStatement.setString(3, expertBank.getBankName());
            preparedStatement.setInt(4, expertBank.getExpertID());
            return preparedStatement.executeUpdate() > 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateExpert(Expert expert) throws SQLException {
        String updateUserQuery = "UPDATE [User] SET username=?, password=?, gmail=?, phone=?, role=?, status=?, fullName=?, socialID=?, gender=?, avatar=? WHERE userID=?";
        String updateExpertQuery = "UPDATE Expert SET certificate=?, honour_ownedID=?, CCCD=? WHERE expertID=?";

        try {
            connection.setAutoCommit(false);

            // Mã hóa password trước khi cập nhật
            expert.setPassword(Encrypt.hashPassword(expert.getPassword()));

            // Update User table
            try (PreparedStatement userStmt = connection.prepareStatement(updateUserQuery)) {
                userStmt.setString(1, expert.getUsername());
                userStmt.setString(2, expert.getPassword());
                userStmt.setString(3, expert.getGmail());
                userStmt.setString(4, expert.getPhone());
                userStmt.setString(5, expert.getRole());
                userStmt.setString(6, expert.getStatus());
                userStmt.setString(7, expert.getFullName());
                userStmt.setString(8, expert.getSocialID());
                userStmt.setString(9, expert.getGender());
                userStmt.setString(10, expert.getAvatar());
                userStmt.setInt(11, expert.getUserID());
                userStmt.executeUpdate();
            }

            // Update Expert table
            try (PreparedStatement expertStmt = connection.prepareStatement(updateExpertQuery)) {
                expertStmt.setString(1, expert.getCertificate());
                expertStmt.setInt(2, expert.getHonour_ownedID());
                expertStmt.setString(3, expert.getCCCD());
                expertStmt.setInt(4, expert.getExpertID());
                expertStmt.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public boolean deleteExpert(int expertID) throws SQLException {
        String deleteExpertQuery = "DELETE FROM Expert WHERE expertID=?";
        String deleteUserQuery = "DELETE FROM [User] WHERE userID=(SELECT userID FROM Expert WHERE expertID=?)";

        try {
            connection.setAutoCommit(false);

            // Delete from Expert table first
            try (PreparedStatement expertStmt = connection.prepareStatement(deleteExpertQuery)) {
                expertStmt.setInt(1, expertID);
                expertStmt.executeUpdate();
            }

            // Delete from User table
            try (PreparedStatement userStmt = connection.prepareStatement(deleteUserQuery)) {
                userStmt.setInt(1, expertID);
                userStmt.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<Expert> getAllExperts() throws SQLException {
        List<Expert> experts = new ArrayList<>();
        String query = "SELECT e.*, u.* FROM Expert e JOIN [User] u ON e.userID = u.userID";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                experts.add(mapResultSetToExpert(rs));
            }
        }
        return experts;
    }

    public List<Expert> searchExpertsByName(String name) throws SQLException {
        List<Expert> experts = new ArrayList<>();
        String query = "SELECT e.*, u.* FROM Expert e JOIN [User] u ON e.userID = u.userID WHERE u.fullName LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                experts.add(mapResultSetToExpert(rs));
            }
        }
        return experts;
    }

    public boolean updateExpertStatus(int expertID, String status) throws SQLException {
        String query = "UPDATE [User] SET status = ? WHERE userID = (SELECT userID FROM Expert WHERE expertID = ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, expertID);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Expert> getExpertsByStatus(String status) throws SQLException {
        List<Expert> experts = new ArrayList<>();
        String query = "SELECT e.*, u.* FROM Expert e JOIN [User] u ON e.userID = u.userID WHERE u.status = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                experts.add(mapResultSetToExpert(rs));
            }
        }
        return experts;
    }

    public static void main(String[] args) {
        ExpertDAO expertDAO = new ExpertDAO();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== Expert Management System ===");
            System.out.println("1. Create new expert");
            System.out.println("2. Get expert by ID");
            System.out.println("3. Get expert by User ID");
            System.out.println("4. Update expert");
            System.out.println("5. Delete expert");
            System.out.println("6. Get all experts");
            System.out.println("7. Search experts by name");
            System.out.println("8. Update expert status");
            System.out.println("9. Get experts by status");
            System.out.println("10. Add bank account");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            try {
                switch (choice) {
                    case 1:
                        System.out.println("\n=== Create New Expert ===");
                        Expert newExpert = new Expert();
                        System.out.print("Username: ");
                        newExpert.setUsername(scanner.nextLine());
                        System.out.print("Password: ");
                        newExpert.setPassword(scanner.nextLine());
                        System.out.print("Email: ");
                        newExpert.setGmail(scanner.nextLine());
                        System.out.print("Phone: ");
                        newExpert.setPhone(scanner.nextLine());
                        System.out.print("Full Name: ");
                        newExpert.setFullName(scanner.nextLine());
                        System.out.print("Certificate: ");
                        newExpert.setCertificate(scanner.nextLine());
                        System.out.print("Avatar URL: ");
                        newExpert.setAvatar(scanner.nextLine());
                        newExpert.setRole("expert");
                        newExpert.setStatus("active");
                        newExpert.setDateCreate(new Date());
                        
                        if (expertDAO.createExpert(newExpert)) {
                            System.out.println("Expert created successfully!");
                        } else {
                            System.out.println("Failed to create expert!");
                        }
                        break;
                        
                    case 2:
                        System.out.print("\nEnter Expert ID: ");
                        int expertId = scanner.nextInt();
                        Expert expert = expertDAO.getExpertById(expertId);
                        if (expert != null) {
                            System.out.println("Expert found: " + expert.getFullName());
                        } else {
                            System.out.println("Expert not found!");
                        }
                        break;
                        
                    case 3:
                        System.out.print("\nEnter User ID: ");
                        int userId = scanner.nextInt();
                        Expert expertByUserId = expertDAO.getExpertByUserID(userId);
                        if (expertByUserId != null) {
                            System.out.println("Expert found: " + expertByUserId.getFullName());
                        } else {
                            System.out.println("Expert not found!");
                        }
                        break;
                        
                    case 4:
                        System.out.print("\nEnter Expert ID to update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        Expert expertToUpdate = expertDAO.getExpertById(updateId);
                        if (expertToUpdate != null) {
                            System.out.print("New Full Name: ");
                            expertToUpdate.setFullName(scanner.nextLine());
                            System.out.print("New Email: ");
                            expertToUpdate.setGmail(scanner.nextLine());
                            System.out.print("New Phone: ");
                            expertToUpdate.setPhone(scanner.nextLine());
                            
                            if (expertDAO.updateExpert(expertToUpdate)) {
                                System.out.println("Expert updated successfully!");
                            } else {
                                System.out.println("Failed to update expert!");
                            }
                        } else {
                            System.out.println("Expert not found!");
                        }
                        break;
                        
                    case 5:
                        System.out.print("\nEnter Expert ID to delete: ");
                        int deleteId = scanner.nextInt();
                        if (expertDAO.deleteExpert(deleteId)) {
                            System.out.println("Expert deleted successfully!");
                        } else {
                            System.out.println("Failed to delete expert!");
                        }
                        break;
                        
                    case 6:
                        List<Expert> allExperts = expertDAO.getAllExperts();
                        System.out.println("\nAll Experts:");
                        for (Expert e : allExperts) {
                            System.out.println("ID: " + e.getExpertID() + ", Name: " + e.getFullName());
                        }
                        break;
                        
                    case 7:
                        System.out.print("\nEnter name to search: ");
                        String searchName = scanner.nextLine();
                        List<Expert> searchResults = expertDAO.searchExpertsByName(searchName);
                        System.out.println("\nSearch Results:");
                        for (Expert e : searchResults) {
                            System.out.println("ID: " + e.getExpertID() + ", Name: " + e.getFullName());
                        }
                        break;
                        
                    case 8:
                        System.out.print("\nEnter Expert ID: ");
                        int statusId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("New Status (active/inactive): ");
                        String newStatus = scanner.nextLine();
                        if (expertDAO.updateExpertStatus(statusId, newStatus)) {
                            System.out.println("Status updated successfully!");
                        } else {
                            System.out.println("Failed to update status!");
                        }
                        break;
                        
                    case 9:
                        System.out.print("\nEnter status to filter (active/inactive): ");
                        String filterStatus = scanner.nextLine();
                        List<Expert> statusExperts = expertDAO.getExpertsByStatus(filterStatus);
                        System.out.println("\nExperts with status " + filterStatus + ":");
                        for (Expert e : statusExperts) {
                            System.out.println("ID: " + e.getExpertID() + ", Name: " + e.getFullName());
                        }
                        break;
                        
                    case 10:
                        System.out.print("\nEnter Expert ID: ");
                        int bankExpertId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Bank Account: ");
                        String bankAccount = scanner.nextLine();
                        System.out.print("Bank Name: ");
                        String bankName = scanner.nextLine();
                        System.out.print("BIN Code: ");
                        String binCode = scanner.nextLine();
                        
                        ExpertBank expertBank = new ExpertBank(bankName, bankAccount, binCode, bankExpertId);
                        if (expertDAO.addNewBankAccount(expertBank)) {
                            System.out.println("Bank account added successfully!");
                        } else {
                            System.out.println("Failed to add bank account!");
                        }
                        break;
                        
                    case 0:
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                        
                    default:
                        System.out.println("Invalid option! Please try again.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
