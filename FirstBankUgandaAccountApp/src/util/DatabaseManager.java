 package util;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:ucanaccess://FirstBankDB.accdb;showSchema=true";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static boolean testConnection() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            System.out.println("✅ Database connected successfully!");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveAccount(String accountNumber, String firstName, String lastName,
            String nin, String email, String phone, String pin, String dob,
            String accountType, String branch, double openingDeposit) {

        String sql = "INSERT INTO Accounts (AccountNumber, FirstName, LastName, NIN, Email, Phone, " +
                     "PIN, DOB, AccountType, Branch, OpeningDeposit, DateCreated) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accountNumber);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, nin);
            pstmt.setString(5, email);
            pstmt.setString(6, phone);
            pstmt.setString(7, pin);
            pstmt.setString(8, dob);
            pstmt.setString(9, accountType);
            pstmt.setString(10, branch);
            pstmt.setDouble(11, openingDeposit);
            pstmt.setString(12, LocalDateTime.now().format(FORMATTER));

            int rows = pstmt.executeUpdate();
            System.out.println("✅ SUCCESS: Saved " + rows + " record(s) to database");
            return true;

        } catch (Exception e) {
            System.err.println("❌ SAVE FAILED: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static int getNextSequence(String branchCode, String year) {
        String sql = "SELECT COUNT(*) FROM Accounts WHERE AccountNumber LIKE ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, branchCode + "-" + year + "-%");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1) + 1;
        } catch (Exception e) {
            System.err.println("Sequence warning: " + e.getMessage());
        }
        return 1;
    }
}