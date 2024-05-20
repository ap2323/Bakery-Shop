package com.bakery.customer;

import com.bakery.UserAlreadyFoundException;
import com.bakery.WriteControl;


import java.io.IOException;
import java.util.Scanner;

public class Customer extends CustomerHelper {

    private String customerName;
    private String customerID;
    private String customerPassword;

    public Customer(){}

    public Customer(String customerID, String customerPassword, String customerName){
        this.customerID = customerID;
        this.customerPassword = customerPassword;
        this.customerName = customerName;
    }

    public void addCustomer(String customerID, String customerPassword, String customerName) throws IOException {
        if(isValidCustomer(customerID)) {
            saveCustomer(customerID, customerPassword, customerName);
        }
    }

    public String getCustomerID(){
        return customerID;
    }

    public void setCustomerID(String customerID) throws IOException {
        if(!customerID.endsWith("@gmail.com")) throw new IllegalArgumentException("Customer ID must be end with '@gmail.com'.");
        if(this.customerID.equalsIgnoreCase(customerID)) throw new IllegalArgumentException("This is current ID.");
        isValidCustomer(customerID);
        updateCustomer(customerID, "id");
        this.customerID = customerID;
    }

    public void setCustomerPassword(String customerPassword) throws IOException {
        if(customerPassword.length() < 8) throw new IllegalArgumentException("Password at least 8 characters.");
        if(this.customerPassword.equals(customerPassword)) throw new IllegalArgumentException("This is current password.");
        updateCustomer(customerPassword, "password");
        this.customerPassword = customerPassword;
    }

    public void setCustomerName(String customerName) throws IOException {
        if(this.customerName.equals(customerName)) throw new IllegalArgumentException("This is a current name.");
        updateCustomer(customerName, "cusName");
        this.customerName = customerName;
    }

    public void mainMenu(Scanner scanner) {
            while (true){
                System.out.println("\n\t\tOPTIONS");
                System.out.println("\n1. Change ID");
                System.out.println("2. Change Name");
                System.out.println("3. Change password");
                System.out.println("4. Remove Account");
                System.out.println("5. Exit");
                System.out.println("\nEnter choice");
                int choice = scanner.nextInt();

                scanner.nextLine();

                switch (choice){
                    case 1:
                        System.out.println("\nEnter ID:");
                        String value = scanner.nextLine().trim();
                        try {
                            setCustomerID(value);
                            System.out.println("\nUpdated Successfully!.");
                        }catch (IllegalArgumentException | IOException | UserAlreadyFoundException ie ){
                            System.out.println("\n" + ie.getMessage());
                        }

                        break;
                    case 2:
                        System.out.println("\nEnter Name:");
                        value = scanner.nextLine().trim();

                        try {
                            setCustomerName(value);
                            System.out.println("\nUpdated Successfully!.");
                        } catch (IllegalArgumentException | IOException | UserAlreadyFoundException ie ){
                            System.out.println("\n" + ie.getMessage());
                        }
                        break;
                    case 3:
                        System.out.println("\nEnter Password:");
                        value = scanner.nextLine().trim();

                        try {
                            setCustomerPassword(value);
                            System.out.println("\nUpdated Successfully!.");
                        } catch (IllegalArgumentException | IOException | UserAlreadyFoundException ie ){
                            System.out.println("\n" + ie.getMessage());
                        }
                        break;
                    case 4:
                        System.out.println("\nEnter ID:");
                        value = scanner.nextLine().trim();
                        try {
                            removeCustomer(value);
                        }catch (IllegalArgumentException | IOException ie ){
                            System.out.println("\n" + ie.getMessage());
                        }

                        break;
                    case 5:
                        System.exit(0);
                }
            }
    }

    private void removeCustomer(String customerID) throws IOException {
        String path = "./src/Bakeries/Customer/Customer.csv";
        try {

            if (!customerID.contains("@gmail.com")) {
                throw new IllegalArgumentException("Customer ID must be end with '@gmail.com'.");
            } else if (isValidCustomer(customerID)) {
                throw new IllegalArgumentException("Customer not found");
            }
        } catch (UserAlreadyFoundException ue) {
            WriteControl.remove(path, customerID);
            System.out.println("Removed Successfully!");
            if (this.customerID.equalsIgnoreCase(customerID)) System.exit(0);
        }
    }

    private void updateCustomer(String value, String type) throws IOException {
        String path = "./src/Bakeries/Customer/Customer.csv";

        WriteControl.update(path, this.customerID, value, type);

    }


    public String getCustomerName() {
        return customerName;
    }
}
