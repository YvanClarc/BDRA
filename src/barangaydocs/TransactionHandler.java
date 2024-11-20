package barangaydocs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TransactionHandler {
    private final config conf = new config();
    private final Scanner sc = new Scanner(System.in);
    
     public void proceedToTransaction() {
        System.out.println("Would you like to proceed to a transaction? (yes/no)");
        String response = sc.next().trim().toLowerCase();
        
        if ("yes".equals(response) || "y".equals(response)) {
            transaction();
        } else if ("no".equals(response) || "n".equals(response)) {
            System.out.println("Exiting the system. Thank you!");
            return;
        } else {
            System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            proceedToTransaction();
        }
    }

    public void transaction() {
        System.out.println("Proceeding to transaction method...");
        System.out.println("Enter Request ID for transaction: ");

        int Rid = -1;
        while (true) {
            try {
                Rid = sc.nextInt();

                
                String checkRequestSql = "SELECT COUNT(*) FROM requests WHERE r_id = ?";
                if (conf.getSingleValue(checkRequestSql, Rid) == 0) {
                    System.out.println("Request ID does not exist. Please enter a valid Request ID.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid Request ID.");
                sc.next();
            }
        }   
            String status = getRequestStatus(Rid);
        if ("Paid".equalsIgnoreCase(status)) {
            System.out.printf("Request ID %d has already been paid. No further action is allowed.\n", Rid);
            return;
        }

        
        double fee = getFeeForRequest(Rid);
        if (fee <= 0) {
            System.out.println("No fee associated with this request.");
            return;
        }

        System.out.printf("The fee for Request ID %d is: %.2f\n", Rid, fee);
        System.out.println("Enter the amount of cash you have: ");

        double cashAvailable = -1;
        while (true) {
            try {
                cashAvailable = sc.nextDouble();
                if (cashAvailable >= fee) {
                    break;
                } else {
                    System.out.println("Insufficient cash. Please enter an amount equal to or greater than the fee.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid cash amount.");
                sc.next();
            }
        }

        
        cashAvailable -= fee;
        System.out.printf("Payment of %.2f successful! Remaining cash: %.2f\n", fee, cashAvailable);

        
        processPayment(Rid, fee);
        System.out.println("Transaction completed successfully.");
    }

    private double getFeeForRequest(int Rid) {
        double fee = 0.0;
        String query = "SELECT fee FROM document WHERE d_id = (SELECT d_id FROM requests WHERE r_id = ?)";

        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, Rid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                fee = rs.getDouble("fee");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving fee: " + e.getMessage());
        }
        return fee;
    }

    private void processPayment(int Rid, double fee) {
    String updateRequestQuery = "UPDATE requests SET status = 'Paid' WHERE r_id = ?";
    String insertPaymentQuery = "INSERT INTO payments (r_id, amount, payment_date) VALUES (?, ?, ?)";

    try (Connection conn = conf.connectDB();
         PreparedStatement updatePstmt = conn.prepareStatement(updateRequestQuery);
         PreparedStatement insertPstmt = conn.prepareStatement(insertPaymentQuery)) {
        
        updatePstmt.setInt(1, Rid);
        int rowsUpdated = updatePstmt.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.printf("Request ID %d status updated to 'Paid'.\n", Rid);
        } else {
            System.out.printf("No request found with ID %d. Status not updated.\n", Rid);
        }

        insertPstmt.setInt(1, Rid);
        insertPstmt.setDouble(2, fee);

        insertPstmt.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
        
        insertPstmt.executeUpdate();

        System.out.printf("Payment of %.2f recorded for Request ID %d.\n", fee, Rid);

    } catch (SQLException e) {
        System.out.println("Error processing payment: " + e.getMessage());
    }
}
    private String getRequestStatus(int Rid) {
        String status = "";
        String query = "SELECT status FROM requests WHERE r_id = ?";

        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, Rid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                status = rs.getString("status");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving request status: " + e.getMessage());
        }
        return status;
    }
}