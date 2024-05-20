package com.bakery.admin;

import com.bakery.*;
import com.bakery.employee.Employee;
import com.bakery.stocks.Stocks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public final class Admin extends AdminHelper {

    private String adminID;
    private String adminName;
    private String bakeryName;

    private final Employee employee = new Employee();


    public Admin() {

    }
    public Admin(String adminID, String adminName, String bakeryName){

        this.adminID = adminID;
        this.adminName = adminName;
        this.bakeryName = bakeryName;

    }
    public void mainMenu(){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Add Admin");
            System.out.println("2. Change ID");
            System.out.println("3. Change Name");
            System.out.println("4. Remove Admin");
            System.out.println("5. Add Employee");
            System.out.println("6. Remove Employee");
            System.out.println("7. View Employee status");
            System.out.println("8. View All Employees");
            System.out.println("9. Stock Management");
            System.out.println("10. Exit ");
            System.out.println("\n Enter Choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:

                    System.out.println("Enter Admin ID: ");
                    String adminID = scanner.nextLine();

                    System.out.println("Enter Admin Name: ");
                    String adminName = scanner.nextLine();
                    try {

                        addAdmin(adminID, adminName, this.bakeryName);
                    } catch (UserAlreadyFoundException | IOException ie){
                        System.out.println(ie.getMessage());
                    }

                    break;
                case 2:
                    System.out.println("Enter Admin ID: ");
                    adminID = scanner.nextLine();
                    try {
                        setAdminID(adminID);
                    } catch (UserAlreadyFoundException |IllegalArgumentException | IOException ie) {
                        System.out.println(ie.getMessage());
                    }

                    break;
                case 3:
                    System.out.println("Enter Admin Name:");
                    adminName = scanner.nextLine();
                    try {
                        setAdminName(adminName);
                    } catch (IllegalArgumentException | IOException ie) {
                        System.out.println(ie.getMessage());
                    }

                    break;
                case 4:
                    System.out.println("Enter Admin ID: ");
                    adminID = scanner.nextLine().trim();
                    try {
                        removeAdmin(adminID);
                    } catch (IllegalArgumentException | IOException ie) {
                        System.out.println(ie.getMessage());
                    }

                    break;
                case 5:
                    System.out.println("Enter Employee ID: ");
                    String empID = scanner.nextLine();

                    System.out.println("Enter Employee Name: ");
                    String empName = scanner.nextLine();

                    System.out.println("Enter Employee Department: ");
                    String empDept = scanner.nextLine();

                    addEmployee(empID, empName, empDept);

                    break;
                case 6:
                    System.out.println("Enter Employee ID: ");
                    empID = scanner.nextLine();

                    removeEmployee(empID);
                    break;
                case 7:
                    System.out.println("Enter Employee ID: ");
                    empID = scanner.nextLine();

                    displayEmployee(empID);
                    break;
                case 8:
                    displayAllEmployees();
                    break;
                case 9:
                    manageStocks(scanner);
                    break;
                case 10:
                    System.exit(0);
            }
        }

    }

    private void manageStocks(Scanner scanner) {

        while (true){
            System.out.println("\n\t\tSTOCK MANAGEMENT");
            System.out.println("1. View Stock List");
            System.out.println("2. Create Stock List");
            System.out.println("3. Create Price List");
            System.out.println("4. Modify Stock List");
            System.out.println("5. Modify Price List");
            System.out.println("6. Remove Stock List");
            System.out.println("7. Remove Price List");
            System.out.println("8. Back");

            System.out.println("\nEnter choice:");
            int choice = scanner.nextInt();

            switch (choice){
                case 1:
                    try {
                        displayStocks(this.bakeryName);
                    }catch (OperationFailedException oe){
                        System.out.println(oe.getMessage());
                    }
                    break;
                case 2:
                    try {
                        createStocks(this.bakeryName);
                        System.out.println("Stocks Created!");
                    }catch (OperationFailedException oe){
                        System.out.println(oe.getMessage());
                    }
                    break;
                case 3:
                    try {
                        createPriceList(this.bakeryName);
                        System.out.println("Price List Created!");
                    }catch (OperationFailedException oe){
                        System.out.println(oe.getMessage());
                    }
                    break;
                case 4:
                    try {
                        modifyStockList(scanner);
                    } catch (OperationFailedException oe){
                        System.out.println(oe.getMessage());
                    }
                    break;
                case 5:
                    try {
                        modifyPriceList(scanner);
                    } catch (OperationFailedException oe){
                        System.out.println(oe.getMessage());
                    }
                    break;
                case 6:
                    try {
                        Stocks.stocks.removeList("./src/Bakeries/" + this.bakeryName + "/Stock/StockList.csv");
                        System.out.println("Removed Successfully!");
                    } catch (OperationFailedException oe){
                        System.out.println(oe.getMessage());
                    }
                    break;
                case 7:
                    try {
                        Stocks.stocks.removeList("./src/Bakeries/" + this.bakeryName + "/Stock/PriceList.csv");
                        Stocks.stocks.removeList("./src/Bakeries/" + this.bakeryName + "/Stock/StockList.csv");
                        System.out.println("Removed Successfully!");
                    } catch (OperationFailedException oe){
                        System.out.println(oe.getMessage());
                    }
                    break;
                case 8:
                    mainMenu();
                    break;
            }
        }
    }

    private void modifyStockList(Scanner scanner) {
        String path = "./src/Bakeries/" + this.bakeryName + "/Stock/StockList.csv";
            while (true) {
                System.out.println("\n1. Change item name");
                System.out.println("2. Change item category");
                System.out.println("3. Change item status");
                System.out.println("4. Change item price");
                System.out.println("5. Add Stock");
                System.out.println("6. Remove Stock");
                System.out.println("7. Back");
                System.out.println("\nEnter choice:");

                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        System.out.println("\nEnter item name to change:");
                        String oldValue = scanner.nextLine().trim();

                        System.out.println("\nEnter new item name:");
                        String newValue = scanner.nextLine().trim();

                        if (Stocks.stocks.isItemFound(path,newValue)) {
                            System.out.println("Item already in StockList");
                            break;
                        }

                        try {
                            if (Stocks.stocks.isItemFound(path,oldValue)) {
                                Stocks.stocks.UpdateStockList(path, oldValue, newValue, "id");
                                System.out.println("Updated Successfully!");
                            } else {
                                System.out.println("Enter valid item name");
                            }
                        } catch (IOException ie) {
                            throw new OperationFailedException("Update failed!", ie);
                        }
                        break;
                    case 2:
                        System.out.println("\nEnter item name to change:");
                        oldValue = scanner.nextLine().trim();

                        System.out.println("\nEnter new item category name:");
                        newValue = scanner.nextLine().trim();

                        try {
                            if (Stocks.stocks.isItemFound(path,oldValue)) {
                                Stocks.stocks.UpdateStockList(path, oldValue, newValue, "name");
                                System.out.println("Updated Successfully!");
                            } else {
                                System.out.println("Enter valid item name!");
                            }
                        } catch (IOException ie) {
                            throw new OperationFailedException("Update failed!", ie);
                        }
                        break;
                    case 3:
                        try {
                            changeItemStatus(path, scanner);
                            System.out.println("Updated Successfully!");
                        }catch (IOException ie){
                            throw new OperationFailedException("Update Failed!", ie);
                        }
                        break;
                    case 4:
                        System.out.println("\nEnter item name to change:");
                        oldValue = scanner.nextLine().trim();
                        if (!Stocks.stocks.isItemFound(path, oldValue)) {
                            System.out.println("Enter valid item name!");
                            break;
                        }
                        System.out.println("\nEnter item price:");
                        int newStockValue = scanner.nextInt();

                        try {
                            if (Stocks.stocks.isValidPrice(this.bakeryName, oldValue, newStockValue)) {
                                Stocks.stocks.UpdateStockList(path, oldValue, String.valueOf(newStockValue), "price");
                                System.out.println("Updated Successfully!");
                            } else {
                                System.out.println("Entered item price is more then the MRP(" + Stocks.stocks.getMRP(
                                        "./src/Bakeries/"+this.bakeryName+"/Stock/PriceList.csv" , oldValue) + "/-).");
                            }
                        } catch (IOException ie) {
                            throw new OperationFailedException("Update failed!", ie);
                        }
                        break;
                    case 5:
                        System.out.println("\nEnter item name:");
                        String itemName = scanner.nextLine().trim();

                        if (!Stocks.stocks.isItemFound("./src/Bakeries/"+this.bakeryName+"/Stock/PriceList.csv", itemName))
                            throw new OperationFailedException("Item not found in priceList.");

                        System.out.println("\nEnter item Category name:");
                        String itemCategory = scanner.nextLine().trim();

                        System.out.println("\nEnter item price:");
                        int price = scanner.nextInt();

                        if (!Stocks.stocks.isValidPrice(this.bakeryName, itemName, price))
                            throw new OperationFailedException("Item price is maximum to MRP(" + Stocks.stocks.getMRP(
                                    "./src/Bakeries/"+this.bakeryName+"/Stock/PriceList.csv" , itemName) + "/-).");

                        try {
                            Stocks.stocks.add(path, itemName, itemCategory, "NIL", price);
                            System.out.println("Added Successfully!");
                        } catch (OperationFailedException ie) {
                            throw new OperationFailedException(ie.getMessage());
                        }
                        break;
                    case 6:
                        System.out.println("\nEnter item name:");
                        itemName = scanner.nextLine().trim();
                        if (!Stocks.stocks.isItemFound(path, itemName))
                            throw new OperationFailedException("Item not found!");
                        try {
                            Stocks.stocks.remove(path, itemName);
                            System.out.println("Removed Successfully!");
                        } catch (IOException ie) {
                            throw new OperationFailedException("Unable to remove!", ie);
                        }
                    case 7:
                        manageStocks(scanner);
                        break;
                }
            }
    }

    private void changeItemStatus(String path,Scanner scanner) throws IOException {
        ArrayList<String> itemNames = new ArrayList<>();
        while (true){
            System.out.println("\nEnter item name to change(or 'exit' to stop):");
            String itemName = scanner.nextLine().trim();

            if(itemName.equalsIgnoreCase("exit")) break;

            if (Stocks.stocks.isItemFound(path,itemName)) {
                itemNames.add(itemName);
            } else {
                System.out.println("Enter valid item name!");
            }

        }

        Stocks.stocks.changeItemStatus(path, itemNames);

    }

    private void modifyPriceList(Scanner scanner) {
        String path = "./src/Bakeries/" + this.bakeryName + "/Stock/PriceList.csv";
        if(!(new File(path).length() == 0)) {
            while (true) {
                System.out.println("\n1.Change item price");
                System.out.println("2. Add item");
                System.out.println("3. Remove item");
                System.out.println("4. Back");
                System.out.println("\nEnter choice:");

                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        System.out.println("\nEnter item name to change:");
                        String oldValue = scanner.nextLine().trim();
                        if (!Stocks.stocks.isItemFound(path, oldValue)) {
                            System.out.println("Enter valid item name!");
                            break;
                        }
                        System.out.println("\nEnter item price:");
                        int newPrice = scanner.nextInt();

                        try {
                            Stocks.stocks.UpdateStockList(path, oldValue, String.valueOf(newPrice), "name");
                            System.out.println("Updated Successfully!");
                        } catch (IOException ie) {
                            throw new OperationFailedException("Update failed!", ie);
                        }
                        break;
                    case 2:
                        System.out.println("\nEnter item name:");
                        String itemName = scanner.nextLine().trim();

                        if (Stocks.stocks.isItemFound(path, itemName))
                            throw new OperationFailedException("Item Already Found!");

                        System.out.println("\nEnter item price:");
                        int price = scanner.nextInt();
                        try {
                            Stocks.stocks.add(path, itemName, price);
                            System.out.println("Added Successfully!");
                        } catch (IOException io) {
                            throw new OperationFailedException("Unable to add priceList.");
                        }
                        break;
                    case 3:
                        System.out.println("\nEnter item name:");
                        itemName = scanner.nextLine().trim();
                        if (!Stocks.stocks.isItemFound(path, itemName))
                            throw new OperationFailedException("Item not found!");
                        try {
                            Stocks.stocks.remove(path, itemName);
                            Stocks.stocks.remove("./src/Bakeries/" + this.bakeryName + "/Stock/StockList.csv", itemName);
                            System.out.println("Removed Successfully!");
                        } catch (IOException ie) {
                            throw new OperationFailedException("Unable to remove!", ie);
                        }
                        break;
                    case 4:
                        manageStocks(scanner);
                        break;
                }
            }
        } else {
            throw new OperationFailedException("Create a PriceList first!.");
        }
    }
    private void removeAdmin(String adminID) throws IOException,IllegalArgumentException {
        try {


            if (this.adminID.equals(adminID)) throw new IllegalArgumentException("Current admin in use.");
            else if (adminID.contains("@Employee")) {
                throw new IllegalArgumentException("Employee id cannot be admin id");
            } else if (isValidAdmin(adminID, this.bakeryName)) {
                throw new IllegalArgumentException("Admin not found");
            }
        } catch (UserAlreadyFoundException ue) {
            String path = "./src/Bakeries/" + this.bakeryName + "/Admin/Admin.csv";

            WriteControl.remove(path, adminID);
            System.out.println("Removed Successfully!");
        }
    }

    public void addAdmin(String adminID, String adminName, String bakeryName) throws UserAlreadyFoundException,OperationFailedException,IOException{

        if (isValidAdmin(adminID, bakeryName)) {
            saveAdmin(adminID, adminName, bakeryName);
            System.out.println("Admin Added!");

        }
    }

    private void updateAdmin(String value, String type) throws IOException{
        String path = "./src/Bakeries/" + this.bakeryName + "/Admin/Admin.csv";

        WriteControl.update(path, this.adminID, value, type);
        System.out.println("Updated Successfully!");

    }

    public String getAdminID() {
        return adminID;
    }

    private void setAdminID(String adminID) throws IOException,UserAlreadyFoundException {
        if (this.adminID.equals(adminID)) throw new IllegalArgumentException("This is a current Admin ID");
        else if (adminID.contains("@Employee")) throw new IllegalArgumentException("Employee id cannot be Admin id");
        isValidAdmin(adminID, this.bakeryName);
        updateAdmin(adminID,"id");
        this.adminID = adminID;

    }

    public String getAdminName() {
        return adminName;
    }

    private void setAdminName(String adminName) throws IOException,IllegalArgumentException {
        if (this.adminName.equals(adminName)) throw new IllegalArgumentException("This is a current Admin Name");
        updateAdmin(adminName, "name");
        this.adminName = adminName;
    }

    private void addEmployee(String empID, String empName, String empDept) {
        try {
            employee.addEmployee(empID, empName, empDept,this.adminID ,this.bakeryName);
            System.out.println("Employee Added!");

        }catch ( IOException | UserAlreadyFoundException | IllegalArgumentException | OperationFailedException ie){
            System.out.println(ie.getMessage());

        }
        mainMenu();
    }

    public void removeEmployee(String empID){
        String path = "./src/Bakeries/" + this.bakeryName + "/Employee/Employee.csv";
        try {
            employee.removeEmployee(path, empID, this.bakeryName);
            System.out.println("Employee Removed!");
        }catch (IllegalArgumentException | UserNotFoundException | IOException ue){
            System.out.println(ue.getMessage());
        }

    }

    public void displayEmployee(String empID){
        try {
            employee.displayEmployeeDetails(empID, this.bakeryName);
        } catch (UserNotFoundException | IOException |OperationFailedException | IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        }

    }

    public void displayAllEmployees(){
        HashMap<String, String> employees = employee.getEmployees(this.bakeryName);

        if(!employees.isEmpty()) {
            System.out.println("\n\tID\t\t\t\tName");
            for (Map.Entry<String, String> emp : employees.entrySet()) {
                System.out.println("\t" + emp.getKey() + "\t" + emp.getValue());
            }
        } else {
            System.out.println("No employees");
        }
    }
}
