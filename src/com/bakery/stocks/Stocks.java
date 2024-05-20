package com.bakery.stocks;

import com.bakery.OperationFailedException;
import com.bakery.WriteControl;

import java.io.*;
import java.util.*;

public final class Stocks {

    public HashMap<String, Integer> stock = new HashMap<>();
    private final ArrayList<Stocks> stockList = new ArrayList<>();
    private HashMap<String, Integer> priceList = new HashMap<>();
    public static ArrayList<ArrayList<String>> cart = new ArrayList<>();
    public static final Stocks stocks = new Stocks();

    private String stockName;

    private Stocks(){

    }
    private Stocks(HashMap<String, Integer> stock,String stockName){

        this.stock.putAll(stock);
        this.stockName = stockName;
    }

    public void createStockList(String bakeryName) throws OperationFailedException {
        String path = "./src/Bakeries/" + bakeryName + "/Stock/StockList.csv";
        if(!priceListIsEmpty(bakeryName)){
            HashMap<String, Integer> stock = new HashMap<>();
            Scanner scanner = new Scanner(System.in);
            String name;
            int count;
            String categoryName;
            int price;
            while (true) {

                System.out.println("Enter category(or 'exit' to stop):");
                categoryName = scanner.nextLine();

                if(categoryName.equalsIgnoreCase("exit")) {

                    save(bakeryName, "stockList");
                    break;
                }

                System.out.println("Enter item name:");
                name = scanner.nextLine().trim();

                if(isItemFound("./src/Bakeries/" + bakeryName + "/Stock/PriceList.csv",name)) {

                    System.out.println("Enter price: ");
                    price = scanner.nextInt();

                    if(isValidPrice(bakeryName,name,price)) {
                        stock.put(name, price);
                        stockList.add(new Stocks(stock, categoryName));
                        stock.clear();

                    } else {
                        System.out.println("Entered item price is more then the MRP(" + getMRP("./src/Bakeries/"+bakeryName+"/Stock/PriceList.csv",name) + "/-).");
                    }

                } else {
                    System.out.println("Entered item cannot be found in PriceList.");
                }
                scanner.nextLine();

            }
            stockList.trimToSize();

        } else {
            throw new OperationFailedException("First create priceList to create stockList.");
        }

    }

    public int getMRP(String path, String name) {
        String line;
        String[] parts;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine();

            while ((line = reader.readLine()) != null){
                parts = line.split(",");
                if (parts[0].trim().equalsIgnoreCase(name)){
                    return Integer.parseInt(parts[1].trim());
                }
            }

        }catch (IOException e) {
            throw new OperationFailedException("Unable to check price", e);
        }

        return 0;
    }

    public int getStockPrice(String path, String itemName){

        String line;
        String[] parts;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine();

            while ((line = reader.readLine()) != null){
                parts = line.split(",");
                if (parts[0].trim().equalsIgnoreCase(itemName)){
                    return Integer.parseInt(parts[3].trim());
                }
            }

        }catch (IOException e) {
            throw new OperationFailedException("Unable to check price", e);
        }

        return 0;
    }

    public boolean isValidPrice(String bakeryName, String name, int price) {
        String path = "./src/Bakeries/" + bakeryName + "/Stock/PriceList.csv";
        String line;
        String[] parts;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine();

            while ((line = reader.readLine()) != null){
                parts = line.split(",");
                if (parts[0].trim().equalsIgnoreCase(name)){
                    if(price > Integer.parseInt(parts[1].trim())){
                        return false;
                    }
                }
            }

        }catch (IOException e) {
            throw new OperationFailedException("Unable to check price", e);
        }
        return true;
    }

    public boolean isItemFound(String path,String name) {
        String line;
        String itemName;
        String[] parts;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            // Skip header
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                parts = line.split(",");
                itemName = parts[0];
                if(itemName.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new OperationFailedException("Unable to read priceList", e);
        }
        return false;
    }

    private void save(String bakeryName, String type) {
        String path;

            switch (type){
                case "stockList":
                    path = "./src/Bakeries/" + bakeryName + "/Stock/StockList.csv";
                    try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
                        // Write header
                        if (new File(path).length() == 0) {
                            writer.println("ItemName, Category, Status, ItemPrice");
                        }
                        // Write data
                        if (!stockList.isEmpty()) {
                            for (Stocks stock : stockList) {
                                for (Map.Entry<String, Integer> set : stock.stock.entrySet()) {
                                    writer.println( set.getKey() + ", " + stock.stockName + ", " + "NIL" + ", " +set.getValue());
                                }
                            }
                        }
                    }catch (IOException io){
                        throw new OperationFailedException("Unable to create stockList.", io);
                    }
                    break;
                case "priceList":
                    path = "./src/Bakeries/" + bakeryName + "/Stock/PriceList.csv";
                    try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
                        // Write header
                        if (new File(path).length() == 0) {
                            writer.println("ItemName, ItemPrice");
                        }
                        // Write data
                        if (!priceList.isEmpty()) {
                            for (Map.Entry<String, Integer> list : priceList.entrySet()) {
                                writer.println(list.getKey() + ", " + list.getValue());
                            }
                        }
                    } catch (IOException io){
                        throw new OperationFailedException("Unable to create priceList.", io);
                    }
                    break;
            }

    }

    private boolean priceListIsEmpty(String bakeryName) {

        String path = "./src/Bakeries/" + bakeryName + "/Stock/PriceList.csv";

        File file = new File(path);

        return file.length() == 0;
    }
    public void add(String path,String itemName, int price) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path, true))) {
            // Write header
            if(new File(path).length() == 0){
                writer.println("ItemName, ItemPrice");
            }
            // Write data
            writer.println(itemName + ", " + price);

        } catch (IOException io){
            throw new OperationFailedException("Unable to add!", io);
        }
    }
    public void add(String path, String itemName, String category, String status ,int price) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path, true))) {
            // Write header
            if(new File(path).length() == 0){
                writer.println("ItemName, Category, Status, ItemPrice");
            }
            // Write data
            writer.println(itemName + ", " + category + ", " + status + ", " + price);

        } catch (IOException io){
            throw new OperationFailedException("Unable to add!", io);
        }
    }
    public void remove(String path,String itemName) throws IOException{
        WriteControl.remove(path,itemName);
    }

    public void removeList(String path){
        try {
            FileWriter writer = new FileWriter(path);
            writer.close();
        } catch (IOException ie){
            throw new OperationFailedException("Unable to remove old priceList",ie);
        }
    }

    public void createPriceList(String bakeryName) {
        String path = "./src/Bakeries/" + bakeryName + "/Stock/PriceList.csv";
        try {
            FileWriter writer = new FileWriter(path);
            writer.close();
        } catch (IOException ie){
            throw new OperationFailedException("Unable to remove old priceList",ie);
        }
        Scanner scanner = new Scanner(System.in);
        HashMap<String, Integer> priceList = new HashMap<>();
        String value;
        int price;
        while (true){
            System.out.println("Enter item name(or exit to stop):");
            value = scanner.nextLine().trim();

            if(value.equalsIgnoreCase("exit")) {
                this.priceList = priceList;

                save(bakeryName, "priceList");
                break;
            }

            System.out.println("Enter " + value + " price:");
            price = scanner.nextInt();
            if(!isItemFound(path, value)){
                priceList.put(value, price);
            } else {
                System.out.println(value + " is already exists.");
            }

            scanner.nextLine();
        }
    }

    public void displayStocks(String bakeryName){
        String path = "./src/Bakeries/" + bakeryName + "/Stock/StockList.csv";
        if(new File(path).length() == 0){
            throw new OperationFailedException("No Entries.");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            reader.readLine();
            String headerLine;
            String[] headers;
            String prevCategory = "";
            while ( (headerLine = reader.readLine()) != null) {
                // Split the header line into columns
                headers = headerLine.split(",");

                if(!headers[1].trim().equalsIgnoreCase(prevCategory)) {
                    displayCategories(bakeryName, headers[1].trim());
                    prevCategory = headers[1].trim();
                }

            }
        }catch (IOException ie){
            throw new OperationFailedException("Unable to display Stocks.");
        }
    }

    public void UpdateStockList(String path,String oldValue,String value,String type) throws IOException {
        WriteControl.update(path,oldValue,value, type);
    }

    private void displayCategories(String bakeryName, String category) {
        String path = "./src/Bakeries/" + bakeryName + "/Stock/StockList.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            reader.readLine();
            String headerLine;
            String[] headers;
            System.out.println("\n\t\t\t\t\t\t\t" + category.toUpperCase());
            System.out.println("\n\tName\t\t\t\t\tAvailability\t\t\t Price\t\t MRP");
            while ( (headerLine = reader.readLine()) != null) {
                // Split the header line into columns
                headers = headerLine.split(",");

                if(headers[1].trim().equalsIgnoreCase(category)){
                    System.out.print(headers[0]);
                    if(headers[2].trim().equalsIgnoreCase("no") || headers[2].trim().equalsIgnoreCase("nil") ) {
                        System.out.print("\t\t\tCurrently unavailable.\t\t\t");

                    } else {
                        System.out.print("\t\t\tIn Stock\t\t\t\t\t\t");
                    }
                    System.out.println("Rs." + headers[3].trim() + " /-\t Rs."+getMRP("./src/Bakeries/"+bakeryName+"/Stock/PriceList.csv", headers[0].trim()) + " /-.");
                }
            }
        }catch (IOException ie){
            throw new OperationFailedException("Unable to display Category.");
        }

    }


    public void changeItemStatus(String path, ArrayList<String> itemNames) throws IOException {
        for (String itemName : itemNames) {
            WriteControl.update(path, itemName, "YES", "dept");
        }
    }

    public String getStatus(String path, String itemName) {
        String line;
        String name;
        String[] parts;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            // Skip header
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                parts = line.split(",");
                name = parts[0].trim();
                if (name.equalsIgnoreCase(itemName)) {
                    return parts[2].trim();
                }
            }
        } catch (IOException e) {
            throw new OperationFailedException("Unable to read priceList", e);
        }
        return null;
    }

    public static void displayCart(){
        if(!Stocks.cart.isEmpty()) {
            System.out.println("\n\t\t\t\t\t\tCART");
            System.out.println("\tName\t\t\t\tItemCount\t\tPrice\t\tTotal");

            for (ArrayList<String> list : Stocks.cart) {
                System.out.println(list.get(0) + "\t\t\t\t\t " + list.get(1) + "\t\t\t" + list.get(2) + "\t\t\t" +
                        calculateTotal(Integer.parseInt(list.get(2)), Integer.parseInt(list.get(1))));
            }

            System.out.println("\nTotal Amount: " + getTotalAmount());
        } else {
            System.out.println("\nCart is empty..");
        }
    }

    public static int getTotalAmount(){
        int totalAmount=0;
        for (ArrayList<String> list : Stocks.cart ) {
            totalAmount += Integer.parseInt(list.get(3).trim());
        }
        return totalAmount;
    }

    public static int calculateTotal(int itemPrice, int itemCount) {
        return itemPrice * itemCount;
    }

    public static ArrayList<ArrayList<String>> getItemList() {
            ArrayList<ArrayList<String>> itemsList = new ArrayList<>();
            ArrayList<String> list;
            for(ArrayList<String> items : cart){
                list = new ArrayList<>();
                list.add(items.get(0));
                list.add(items.get(1));

                itemsList.add(list);
            }
            itemsList.trimToSize();
            return itemsList;
    }
}
