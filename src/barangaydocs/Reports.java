package barangaydocs;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Reports{
    Scanner sc = new Scanner(System.in);
    String response;
    TransactionHandler TH = new TransactionHandler();
public void getReports(){
                        
        do {
    System.out.println(" ====================");
    System.out.println("|       REPORTS      |");
    System.out.println(" ====================");
    System.out.println("1. Overall Requests  |");
    System.out.println("2. Detailed Requests |");
    System.out.println("3. EXIT              |");
    System.out.println("Enter Action:        |");
    System.out.println("------------------- ");

    int action = -1;
    boolean validAction = false;

    while (!validAction) {
        try {
            action = sc.nextInt();
            if (action >= 1 && action <= 3) {
                validAction = true;
            } else {
                System.out.println("Invalid action. Please enter a number between 1 and 3.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter an integer.");
            sc.next();
        }
    }

    Reports tsa = new Reports();

    switch(action) {
        case 1:
            tsa.overallRequests();
            break;
        case 2:
            tsa.detailedReports();
            TH.proceedToTransaction();
            break;
        case 3:
            System.out.println("Exiting Viewing Reports. Returning to Main Menu");
            return;
    }

    while (true) {
        System.out.println("Do you want to continue to Viewing Reports? (Y/N): ");
        response = sc.next();
        
        if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("n")) {
            break;
        } else {
            System.out.println("Invalid response. Please enter 'Y' or 'N'.");
        }
    }

} while (response.equalsIgnoreCase("y"));
      
    }

    private void overallRequests() {
    config conf = new config();
    
    String overallQuery = "SELECT d.document_name, COUNT(r.r_id) AS total_requests, SUM(d.fee) AS total_fees " +
                          "FROM requests r " +
                          "JOIN document d ON r.d_id = d.d_id " +
                          "GROUP BY d.document_name";

    String[] overallHeaders = {"Document Name", "Total Requests", "Total Fees"};
    String[] overallColumns = {"document_name", "total_requests", "total_fees"};

    conf.viewRecords(overallQuery, overallHeaders, overallColumns);
}
       private void detailedReports() {
    config conf = new config();
    
    String detailedQuery = "SELECT r.r_id, c.c_fname, c.c_lname, d.document_name, d.fee, r.created_at, r.status " +
                           "FROM requests r " +
                           "JOIN citizen c ON r.c_id = c.c_id " +
                           "JOIN document d ON r.d_id = d.d_id " +
                           "ORDER BY r.created_at DESC";

    String[] detailedHeaders = {"Request ID", "First Name", "Last Name", "Document Name", "Fee", "Date Created", "Status"};
    String[] detailedColumns = {"r_id", "c_fname", "c_lname", "document_name", "fee", "created_at", "status"};

    conf.viewRecords(detailedQuery, detailedHeaders, detailedColumns);
}
}