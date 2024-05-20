package com.bakery.admin;

import java.io.*;

import com.bakery.OperationFailedException;
import com.bakery.UserAlreadyFoundException;
import com.bakery.stocks.Stocks;

class AdminHelper {

    public boolean isValidAdmin(String adminID, String bakeryName) throws IOException {
        File file = new File("./src/Bakeries/" + bakeryName + "/Admin/Admin.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {


           reader.readLine();
            String headerLine;
            while ( (headerLine = reader.readLine()) != null) {
                // Split the header line into columns
                String[] headers = headerLine.split(",");

                // Get the name of the first column
                String id = headers[0];

                if(id.equals(adminID)) throw new UserAlreadyFoundException("Admin Already exists!");

            }

            // Close the reader

        }
        return true;
    }

    protected void createStocks(String bakeryName) throws OperationFailedException {
        Stocks.stocks.createStockList(bakeryName);
    }

    protected void createPriceList(String bakeryName) throws OperationFailedException{
        Stocks.stocks.createPriceList(bakeryName);
    }

    protected void displayStocks(String bakeryName) throws OperationFailedException {
        Stocks.stocks.displayStocks(bakeryName);
    }


    protected void saveAdmin(String adminID, String adminName, String bakeryName){
        String filePath = "./src/Bakeries/" + bakeryName + "/Admin/Admin.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            // Write header
            if(new File(filePath).length() == 0){
                writer.println("ID, Name");
            }
            // Write data
            writer.println(adminID + ", " + adminName);

        } catch (IOException io){
            throw new OperationFailedException("Unable to createStockList Admin", io);
        }
    }

}
