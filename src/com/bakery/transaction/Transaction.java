package com.bakery.transaction;

import Application.SweetTreats;
import com.bakery.OperationFailedException;
import com.bakery.stocks.Stocks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Transaction {
    private static final Random random = new Random();

    public static String generateTransactionId() {
        Date currentDate = new Date();
        int date = currentDate.getDate();// get curent date
        long timestamp = currentDate.getTime();// Get current time in milliseconds
        int randomInt = random.nextInt(1_000_000); // Generate a random number between 0 and 999,999
        return String.format("TXN-%d%d-%06d", timestamp,date, randomInt);
    }
    public static void placeOrder(String bakeryName) {
        Scanner scanner = new Scanner(System.in);
        int totalAmount = Stocks.getTotalAmount();
        System.out.println("\nEnter Amount: ");
        int amount = scanner.nextInt();

        if(amount < totalAmount) {
            throw new OperationFailedException("Not enough money!");
        } else if(amount > totalAmount){
            throw new OperationFailedException("More money not allowed!");
        }

        save(bakeryName);

        Stocks.cart = new ArrayList<>();
    }

    private static void save(String bakeryName) {
        String path = "./src/Bakeries/" + bakeryName + "/CustomerTransaction/CustomerTransaction.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(path, true))) {
            // Write header
            if(new File(path).length() == 0){
                writer.println("CustomerName, TransactionID, Items, TotalAmount");
            }
            // Write data
            writer.println(SweetTreats.customer.getCustomerID() + ", " + generateTransactionId() + ", " + Arrays.toString(Stocks.getItemList().toArray()) + ", "
                    + Stocks.getTotalAmount());

        } catch (IOException io){
            throw new OperationFailedException("Unable to save transaction!", io);
        }
    }
}
