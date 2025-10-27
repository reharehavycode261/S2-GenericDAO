import database.core.GenericDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main<T> {

    private final GenericDAO<T> genericDAO;

    public Main(Connection connection) {
        this.genericDAO = new GenericDAO<>(connection);
    }

    public void displayPagedResults() {
        Scanner scanner = new Scanner(System.in);
        int page = 0;
        int pageSize = 10;
        List<T> results;

        do {
            try {
                results = genericDAO.findAllPaged(pageSize, page * pageSize);
                System.out.println("Page " + (page + 1));
                displayResults(results);
                System.out.println("Enter 'n' for next page, 'p' for previous page, or 'q' to quit:");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("n")) {
                    page++;
                } else if (input.equalsIgnoreCase("p") && page > 0) {
                    page--;
                } else if (input.equalsIgnoreCase("q")) {
                    break;
                }
            } catch (SQLException e) {
                System.out.println("Error retrieving data: " + e.getMessage());
                break;
            }
        } while (true);

        scanner.close();
    }

    private void displayResults(List<T> results) {
        for (T result : results) {
            System.out.println(result);
        }
    }
}