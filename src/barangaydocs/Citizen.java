
package barangaydocs;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Citizen {
    Scanner sc = new Scanner(System.in);
    String response;
    
    public void getCitizen() {
    Scanner sc = new Scanner(System.in);
    String response;

    do {
        System.out.println(" ================= ");
        System.out.println("|     CITIZEN     |");
        System.out.println(" ================= ");
        System.out.println("1. ADD Citizen    |");
        System.out.println("2. VIEW Citizen   |");
        System.out.println("3. UPDATE Citizen |");
        System.out.println("4. DELETE Citizen |");
        System.out.println("5. EXIT           |");
        System.out.println("Enter Action:     |");
        System.out.println("------------------ ");

        int action = -1;
        boolean validAction = false;

        // Validate action input
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
                sc.next(); // Clear the invalid input
            }
        }

        Citizen hehe = new Citizen();

        // Execute the chosen action
        switch(action) {
            case 1:
                hehe.addCitizen();
                break;
            case 2:
                hehe.viewCitizens();
                break;    
            case 3:
                hehe.updateCitizen();
                break;     
            case 4:
                hehe.deleteCitizen();
                break;
            case 5:
                System.out.println("Exiting Citizen. Returning to Main Menu...");
                return;
            default:
                System.out.println("Invalid action. Please try again.");
                break;
        }

        // Validate continuation response
        while (true) {
            System.out.println("Do you want to continue with Citizen management? (Y/N): ");
            response = sc.next();
            if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("n")) {
                break; // Valid input, exit the loop
            } else {
                System.out.println("Invalid response. Please enter 'Y' or 'N'.");
            }
        }

    } while (response.equalsIgnoreCase("y"));
}
    
    public void addCitizen(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        System.out.print("Citizen First Name: ");
        String fname = sc.next();
        System.out.print("Citizen Last Name: ");
        String lname = sc.next();
        System.out.print("Citizen Email: ");
        String email = sc.next();
        System.out.print("Citizen Status: ");
        String status = sc.next();
        System.out.print("Citizen Postal Code: ");
        String pcode = sc.next();
        
        String sql = "INSERT INTO citizen (c_fname, c_lname, c_email, c_status, c_postalCode) VALUES (?, ?, ?, ?, ?)";


        conf.addRecord( sql, fname, lname, email, status, pcode);       
    }
    
    private void viewCitizens() {
    config conf = new config();
    String countQuery = "SELECT COUNT(*) FROM citizen";

    if (conf.getSingleValue(countQuery) == 0) {
        System.out.println("No citizens found in the database.");
        return;
    }

    String BDRAQuery = "SELECT * FROM citizen";
    String[] BDRAHeaders = { "ID", "First Name", "Last Name", "Email", "Status", "Postal Code" };
    String[] BDRAColumns = { "c_id", "c_fname", "c_lname", "c_email", "c_status", "c_postalCode" };

    conf.viewRecords(BDRAQuery, BDRAHeaders, BDRAColumns);
}
    private void updateCitizen() {
    Scanner sc = new Scanner(System.in);
    config conf = new config();

    String countQuery = "SELECT COUNT(*) FROM citizen";
    if (conf.getSingleValue(countQuery) == 0) {
        System.out.println("No citizens found in the database yet.");
        return;
    }

    int id = -1;

    while (true) {
        System.out.print("Enter Citizen ID to Update: ");
        
        try {
            id = sc.nextInt();
            String csql = "SELECT c_id FROM citizen WHERE c_id = ?";
            if (conf.getSingleValue(csql, id) == 0) {
                System.out.println("Citizen ID does not exist. Please try again.");
            } else {
                break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric Citizen ID.");
            sc.next();
        }
    }

    System.out.print("New First Name: ");
    String fname = sc.next();
    System.out.print("New Last Name: ");
    String lname = sc.next();
    System.out.print("New Email: ");
    String email = sc.next();
    System.out.print("New Status: ");
    String status = sc.next();
    System.out.print("New Postal Code: ");
    String pcode = sc.next();

    String sql = "UPDATE citizen SET c_fname = ?, c_lname = ?, c_email = ?, c_status = ?, c_postalCode = ? WHERE c_id = ?";
    conf.updateRecord(sql, fname, lname, email, status, pcode, id);
}
    private void deleteCitizen() {
    Scanner sc = new Scanner(System.in);
    config conf = new config();

    String countQuery = "SELECT COUNT(*) FROM citizen";
    if (conf.getSingleValue(countQuery) == 0) {
        System.out.println("No citizens found in the database yet.");
        return;
    }

    int id = -1;

    while (true) {
        System.out.print("Enter Citizen ID to Delete: ");
        try {
            id = sc.nextInt();
            String csql = "SELECT c_id FROM citizen WHERE c_id = ?";
            if (conf.getSingleValue(csql, id) == 0) {
                System.out.println("Citizen ID does not exist. Please try again.");
            } else {
                break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numeric Citizen ID.");
            sc.next();
        }
    }

    System.out.print("Are you sure you want to delete Citizen ID " + id + "? (Y/N): ");
    String confirmation = sc.next();
    while (!confirmation.equalsIgnoreCase("y") && !confirmation.equalsIgnoreCase("n")) {
        System.out.print("Invalid response. Please enter 'Y' or 'N': ");
        confirmation = sc.next();
    }

    if (confirmation.equalsIgnoreCase("n")) {
        System.out.println("Deletion of Citizen ID " + id + " has been canceled.");
        return;
    }

    String sql = "DELETE FROM citizen WHERE c_id = ?";
    conf.deleteRecord(sql, id);
    
    System.out.println("Citizen with ID " + id + " has been deleted successfully.");
}
}
