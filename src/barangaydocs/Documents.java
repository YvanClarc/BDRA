package barangaydocs;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Documents {
    
    Scanner sc = new Scanner(System.in);
    String response;
    
    public void getDocuments(){
                        
        do {
    System.out.println(" ================== ");
    System.out.println("|     REQUESTS     |");
    System.out.println(" ================== ");
    System.out.println("1. ADD Request     |");
    System.out.println("2. VIEW Request    |");
    System.out.println("3. UPDATE Request  |");
    System.out.println("4. DELETE Request  |");
    System.out.println("5. EXIT            |");
    System.out.println("Enter Action:      |");
    System.out.println("------------------- ");

    int action = -1;
    boolean validAction = false;

    while (!validAction) {
        try {
            action = sc.nextInt();
            if (action >= 1 && action <= 5) {
                validAction = true;
            } else {
                System.out.println("Invalid action. Please enter a number between 1 and 5.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter an integer.");
            sc.next();
        }
    }

    Documents ahay = new Documents();

    switch(action) {
        case 1:
            ahay.addDocument();
            break;
        case 2:
            ahay.viewDocument();
            break;    
        case 3:
            ahay.updateDocument();
            break;     
        case 4:
            ahay.deleteDocument();
            break;
        case 5:
            System.out.println("Exiting the requests management. Returning to Main Menu...");
            return;
    }

    while (true) {
        System.out.println("Do you want to continue to Request Management? (Y/N): ");
        response = sc.next();
        
        if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("n")) {
            break;
        } else {
            System.out.println("Invalid response. Please enter 'Y' or 'N'.");
        }
    }

} while (response.equalsIgnoreCase("y"));
      
    }
    
     public void addDocument() {
    Scanner sc = new Scanner(System.in);
    config conf = new config();
    int cid = -1;
    int did = -1;

    System.out.println("-----------------------------------------------|");
    System.out.println("Here are the list of Documents with their IDs: |"
            + "\n-----------------------------------------------|"
            + "\n1. Birth Certificate" +
              "\n2. Marriage Certificate" +
              "\n3. Residency Certificate" +
              "\n4. Barangay Clearance" +
              "\n5. Certification of Good Moral Character" +
              "\n6. Death Certificate" +
              "\n7. Indigency Certificate" +
              "\n8. Business Permit" +
              "\n9. Community Tax Certificate" +
              "\n10. Transfer of Ownership");
    System.out.println("-----------------------------------------------");

    while (true) {
        System.out.print("Enter Document ID: ");
        try {
            cid = sc.nextInt();
            String checkDocumentSql = "SELECT COUNT(*) FROM document WHERE d_id = ?";
            if (conf.getSingleValue(checkDocumentSql, cid) == 0) {
                System.out.println("Document ID does not exist. Please try again.");
            } else {
                break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric Document ID.");
            sc.next();
        }
    }

    while (true) {
        System.out.print("Enter Citizen ID (To link with): ");
        try {
            did = sc.nextInt();
            String checkCitizenSql = "SELECT COUNT(*) FROM citizen WHERE c_id = ?";
            if (conf.getSingleValue(checkCitizenSql, did) == 0) {
                System.out.println("Citizen ID does not exist. Please try again.");
            } else {
                break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric Citizen ID.");
            sc.next();
        }
    }

    String sql = "INSERT INTO requests (d_id, c_id, created_at) VALUES (?, ?, DATETIME('now'))";
    conf.addRecord(sql, cid, did);
    System.out.println("Document request has been added successfully.");
}
     
     private void viewDocument() {
    config conf = new config();
    String countQuery = "SELECT COUNT(*) FROM requests";

    if (conf.getSingleValue(countQuery) == 0) {
        System.out.println("No requests found in the database.");
        return;
    }

    String BDRAQuery = "SELECT r.r_id, c.c_fname, c.c_lname, d.document_name, r.created_at " +
                       "FROM requests r " +
                       "JOIN citizen c ON r.c_id = c.c_id " +
                       "JOIN document d ON r.d_id = d.d_id";

    String[] BDRAHeaders = { "Request ID", "First Name", "Last Name", "Document Name", "Date Created" };
    String[] BDRAColumns = { "r_id", "c_fname", "c_lname", "document_name", "created_at" };

    conf.viewRecords(BDRAQuery, BDRAHeaders, BDRAColumns);
}
     
     private void updateDocument() {
    Scanner sc = new Scanner(System.in);
    config conf = new config();

    String countQuery = "SELECT COUNT(*) FROM requests";
    if (conf.getSingleValue(countQuery) == 0) {
        System.out.println("No requests found in the database yet.");
        return;
    }

    int requestId = -1;

    while (true) {
        System.out.print("Enter Request ID to Update: ");
        try {
            requestId = sc.nextInt();
            String rsql = "SELECT r_id FROM requests WHERE r_id = ?";
            if (conf.getSingleValue(rsql, requestId) == 0) {
                System.out.println("Request ID does not exist. Please try again.");
            } else {
                break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric Request ID.");
            sc.next();
        }
    }

    System.out.println("-----------------------------------------------|");
    System.out.println("Here are the list of Documents with their IDs: |"
            + "\n-----------------------------------------------|"
            + "\n1. Birth Certificate" +
              "\n2. Marriage Certificate" +
              "\n3. Residency Certificate" +
              "\n4. Barangay Clearance" +
              "\n5. Certification of Good Moral Character" +
              "\n6. Death Certificate" +
              "\n7. Indigency Certificate" +
              "\n8. Business Permit" +
              "\n9. Community Tax Certificate" +
              "\n10. Transfer of Ownership");
    System.out.println("-----------------------------------------------");

    int newDocumentId = -1;
    while (true) {
        System.out.print("Enter new Document ID: ");
        try {
            newDocumentId = sc.nextInt();
            String checkDocumentSql = "SELECT COUNT(*) FROM document WHERE d_id = ?";
            if (conf.getSingleValue(checkDocumentSql, newDocumentId) == 0) {
                System.out.println("Document ID does not exist. Please try again.");
            } else {
                break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric Document ID.");
            sc.next();
        }
    }

    int newCitizenId = -1;
    while (true) {
        System.out.print("Enter new Citizen ID (to link with): ");
        try {
            newCitizenId = sc.nextInt();
            String checkCitizenSql = "SELECT COUNT(*) FROM citizen WHERE c_id = ?";
            if (conf.getSingleValue(checkCitizenSql, newCitizenId) == 0) {
                System.out.println("Citizen ID does not exist. Please try again.");
            } else {
                break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric Citizen ID.");
            sc.next();
        }
    }

    String sql = "UPDATE requests SET c_id = ?, d_id = ? WHERE r_id = ?";
    conf.updateRecord(sql, newCitizenId, newDocumentId, requestId);

    System.out.println("Request ID " + requestId + " has been updated successfully.");
}
     
     private void deleteDocument() {
    Scanner sc = new Scanner(System.in);
    config conf = new config();

    String countQuery = "SELECT COUNT(*) FROM requests";
    if (conf.getSingleValue(countQuery) == 0) {
        System.out.println("No requests found in the database yet.");
        return;
    }

    int requestId = -1;

    while (true) {
        System.out.print("Enter Request ID to Delete: ");
        try {
            requestId = sc.nextInt();
            String rsql = "SELECT r_id FROM requests WHERE r_id = ?";
            if (conf.getSingleValue(rsql, requestId) == 0) {
                System.out.println("Request ID does not exist. Please try again.");
            } else {
                break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric Request ID.");
            sc.next();
        }
    }

    System.out.print("Are you sure you want to delete Request ID " + requestId + "? (Y/N): ");
    String confirmation = sc.next();
    while (!confirmation.equalsIgnoreCase("y") && !confirmation.equalsIgnoreCase("n")) {
        System.out.print("Invalid response. Please enter 'Y' or 'N': ");
        confirmation = sc.next();
    }

    if (confirmation.equalsIgnoreCase("n")) {
        System.out.println("Deletion of Request ID " + requestId + " has been canceled.");
        return;
    }

    String sql = "DELETE FROM requests WHERE r_id = ?";
    conf.deleteRecord(sql, requestId);
    
    System.out.println("Request ID " + requestId + " has been deleted successfully.");
    }
}