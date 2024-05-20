package com.bakery.Order;

import Application.SweetTreats;
import com.bakery.OperationFailedException;
import com.bakery.stocks.Stocks;
import com.bakery.transaction.Transaction;

import java.util.ArrayList;
import java.util.Scanner;

public class Order {

    public static void takeOrder(String bakeryName, Scanner scanner) {
        String path = "./src/Bakeries/" + bakeryName + "/Stock/StockList.csv";
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        ArrayList<String> itemList;
        String status;
        while (true) {
            itemList = new ArrayList<>();
            System.out.println("\nEnter item name(or 'exit' to stop):");
            String itemName = scanner.nextLine().trim();
            if(itemName.equalsIgnoreCase("exit")) {
                if(!list.isEmpty()) {
                    list.trimToSize();
                    Stocks.cart = list;
                    Stocks.displayCart();
                    orderOptions(bakeryName,scanner);
                } else {
                    SweetTreats.displayBakeryMainMenu(bakeryName);
                }
                break;
            }
            if (!Stocks.stocks.isItemFound(path,itemName))
                throw new OperationFailedException("Please Enter valid item!");
            status = Stocks.stocks.getStatus(path, itemName);
            if (status.equalsIgnoreCase("NIL") || status.equalsIgnoreCase("NO")) throw new OperationFailedException("Item is currently Unavailable!");
            System.out.println("Enter No. of items:");
            int itemCount = scanner.nextInt();
            scanner.nextLine();

            itemList.add(itemName);
            itemList.add(String.valueOf(itemCount));
            itemList.add(String.valueOf(Stocks.stocks.getStockPrice(path, itemName)));
            itemList.add(String.valueOf(Stocks.calculateTotal(Stocks.stocks.getStockPrice(path, itemName) , itemCount)));

            list.add(itemList);
        }
    }
    private static void orderOptions(String bakeryName,Scanner scanner) {
        String itemName;
        int itemCount;
        while (true){
            System.out.println("\n1. Remove Item");
            System.out.println("2. Change Count of item");
            System.out.println("3. Place Order");
            System.out.println("4. Back");
            System.out.println("\nEnter Option:");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice){
                case 1:
                    System.out.println("\nEnter item name:");
                    itemName = scanner.nextLine().trim();
                    for (ArrayList<String> list : Stocks.cart) {
                        if (list.contains(itemName)) {
                            Stocks.cart.remove(list);
                            Stocks.cart.trimToSize();
                            System.out.println("Removed Successfully!");
                            Stocks.displayCart();
                            break;
                        }

                    }
                    break;
                case 2:
                    System.out.println("\nEnter item name:");
                    itemName = scanner.nextLine().trim();
                    for (ArrayList<String> list : Stocks.cart) {
                        if (list.contains(itemName)) {
                            System.out.println("Enter item count:");
                            itemCount = scanner.nextInt();
                            list.set(1, String.valueOf(itemCount));
                            list.set(3, String.valueOf(Stocks.calculateTotal(Integer.parseInt(list.get(2)), itemCount)));
                            Stocks.displayCart();
                            break;
                        }

                    }
                    break;
                case 3:
                    try {
                        Transaction.placeOrder(bakeryName);
                        System.out.println("Order Placed!");
                        SweetTreats.mainMenu(scanner);
                        break;
                    } catch (OperationFailedException oe){
                        System.out.println(oe.getMessage());
                    }
                    break;
                case 4:
                    if(!Stocks.cart.isEmpty()) {
                        System.out.println("Cart will be erased! Are you sure to Back?(y/n):");
                        char op = scanner.next().trim().toLowerCase().charAt(0);

                        if (op == 'y') {
                            Stocks.cart.clear();
                            scanner.nextLine();
                            SweetTreats.mainMenu(scanner);
                            break;
                        } else if (op == 'n') {
                            Stocks.displayCart();
                            break;
                        } else {
                            System.out.println("\n Invalid option!");
                            Stocks.displayCart();
                            break;
                        }
                    } else {
                        SweetTreats.mainMenu(scanner);
                        break;
                    }
            }
        }
    }
}
