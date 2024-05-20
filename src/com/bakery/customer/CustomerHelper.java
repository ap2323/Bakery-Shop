package com.bakery.customer;

import com.bakery.OperationFailedException;
import com.bakery.UserAlreadyFoundException;

import java.io.*;

class CustomerHelper {
    protected boolean isValidCustomer(String customerID) throws IOException {
        File file = new File("./src/Bakeries/Customer/Customer.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            reader.readLine();
            String headerLine;
            while ( (headerLine = reader.readLine()) != null) {
                // Split the header line into columns
                String[] headers = headerLine.split(",");

                // Get the name of the first column
                String id = headers[0];

                if(id.equals(customerID)) throw new UserAlreadyFoundException("Customer Already exists!");

            }

        }
        return true;
    }

    protected void saveCustomer(String customerID, String customerPassword, String customerName){
        String path = "./src/Bakeries/Customer/Customer.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(path, true))) {
            // Write header
            if(new File(path).length() == 0){
                writer.println("ID, Password, Name");
            }
            // Write data
            writer.println(customerID + ", " + customerPassword + ", " + customerName );

        } catch (IOException io){
            throw new OperationFailedException("Unable to add customer", io);
        }
    }
}
