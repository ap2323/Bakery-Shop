package Application;

import com.bakery.OperationFailedException;
import com.bakery.Order.Order;
import com.bakery.UserAlreadyFoundException;
import com.bakery.UserNotFoundException;
import com.bakery.customer.Customer;
import com.bakery.stocks.Stocks;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SweetTreats {

    public static Customer customer = new Customer();
    private static final ArrayList<String> bakeryNames;
    static {

        bakeryNames = loadBakeries();

    }

    private static ArrayList<String> loadBakeries() {
        ArrayList<String> bakeries = new ArrayList<>();

        // Specify the directory path
        String directoryPath = "./src/Bakeries";

        // Create a File object representing the specified directory
        File directory = new File(directoryPath);

        // Get list of files and directories inside the specified directory
        File[] filesAndDirectories = directory.listFiles();
        if(filesAndDirectories.length != 0) {
            // Iterate through the array and filter out only directories
            for (File file : filesAndDirectories) {
                if (file.isDirectory()  && !file.getName().equals("Customer")) {
                    bakeries.add(file.getName());
                }
            }
        } else {
            bakeries = null;
        }
        return bakeries;
    }

    private static String[] read(String id) throws IOException {
        File fileName = new File("./src/Bakeries/Customer/Customer.csv");
        String line;
        String[] parts;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {

            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {

                parts = line.split(",");
                if(parts[0].equalsIgnoreCase(id)) return new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim()};

            }
        }
        return new String[0];
    }

    private static void displayBakeries(){
        int bakeryCount = 1;
        if(!bakeryNames.isEmpty()) {
            for (String name : bakeryNames) {
                System.out.println(bakeryCount++ + ". " + name);
            }
        } else {
            System.out.println("\nNo bakeries are available at this time.");
        }

    }

    public static void mainMenu(Scanner scanner){

        while (true){
            System.out.println("\n\t\tSWEET TREATS");
            System.out.println("Options");
            displayBakeries();
            System.out.println("\nEnter bakery name(or 'q' to exit): ");
            String name = scanner.nextLine().trim();
            if(name.toLowerCase().charAt(0) == 'q') {
                System.exit(0);
            } else if (name.equalsIgnoreCase("options")) {
                customer.mainMenu(scanner);
            } else {
                if (bakeryNames.contains(name)) {
                    displayBakeryMainMenu(name);
                    try {
                        Order.takeOrder(name, scanner);
                    }catch (OperationFailedException oe){
                        System.out.println(oe.getMessage());
                        mainMenu(scanner);
                    }
                } else {
                    System.err.println("Please enter valid bakery name.");
                }
            }
        }
    }


    private static void cusRegister(Scanner scanner){
        scanner.nextLine();

        System.out.println("Enter Customer ID: ");
        String cusID = scanner.nextLine();

        System.out.println("Enter Customer Password: ");
        String cusPassword = scanner.nextLine();

        System.out.println("Enter Customer Name: ");
        String cusName = scanner.nextLine();

        if(!cusID.endsWith("@gmail.com")) throw new OperationFailedException("Customer id must be end with '@gmail.com'.");
        else if (cusPassword.length() < 8 ) throw new OperationFailedException("Password contains at least 8 characters.");
        try {
            customer.addCustomer(cusID, cusPassword, cusName);
            System.out.println("Registered Successfully!");
            user();
        } catch (UserAlreadyFoundException | OperationFailedException | IllegalArgumentException |IOException ie) {
            System.out.println(ie.getMessage());
        }
    }

    private static void cusLogin(Scanner scanner) throws IOException {
        scanner.nextLine();

        System.out.println("Enter Customer ID: ");
        String cusID = scanner.nextLine().trim();

        System.out.println("Enter Customer Password: ");
        String cusPassword = scanner.nextLine().trim();

        if(!cusID.endsWith("@gmail.com")) throw new OperationFailedException("Customer id must be end with '@gmail.com'.");

        String[] values = read(cusID);

        if (values.length == 0) {
            throw new UserNotFoundException(cusID + " not found!");
        } else if (!values[1].equals(cusPassword)) {
            throw new OperationFailedException("Invalid Password");
        } else {
            customer = new Customer(cusID, cusPassword, values[2]);
            System.out.println("Login Successfully!");
            mainMenu(scanner);
        }
    }

    private static void user(){
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("\n\t\tSWEET TREATS");
            System.out.println("\n1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.println("\nEnter choice:");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    try {
                       cusLogin(scanner);

                    }catch (IOException | OperationFailedException | UserNotFoundException ex){
                        System.out.println(ex.getMessage());
                    }

                    break;
                case 2:
                    try {
                        cusRegister(scanner);
                    }catch (OperationFailedException oe){
                        System.out.println(oe.getMessage());
                    }
                    break;
                case 3:
                    System.exit(0);
            }
        }

    }

    public static void displayBakeryMainMenu(String bakeryName) {

        Stocks.stocks.displayStocks(bakeryName);

    }

    public static void main(String[] args) {
        user();
    }
}
