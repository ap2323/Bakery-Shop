package com.bakery.employee;

import java.io.*;
import java.util.HashMap;

import com.bakery.OperationFailedException;
import com.bakery.WriteControl;
import com.bakery.UserAlreadyFoundException;

class EmployeeHelper {

    protected boolean isValidEmployee(String empID, String bakeryName) throws IOException {
        File file = new File("./src/Bakeries/" + bakeryName + "/Employee/Employee.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {


            reader.readLine();
            String headerLine;
            while ( (headerLine = reader.readLine()) != null) {
                // Split the header line into columns
                String[] headers = headerLine.split(",");

                // Get the name of the first column
                String id = headers[0];

                if(id.equals(empID)) throw new UserAlreadyFoundException("Employee Already exists!");

            }

        }
        return true;
    }



    protected void saveEmployee(String empID, String empName, String empDept ,String recruitedBy,String bakeryName) {
        String filePath = "./src/Bakeries/"+ bakeryName + "/Employee/Employee.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            // Write header
            if(new File(filePath).length() == 0) {
                writer.println("ID, Name, Dept, Recruited By");
            }
            // Write data
            writer.println(empID + ", " + empName + ", " + empDept + ", " + recruitedBy);
        } catch (IOException io){
            throw new OperationFailedException("Unable to add Employee!", io);
        }
    }


    public static void displayEmployee(String path, String empID){

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            // Skip header
            reader.readLine();
            // Read data
            String[] parts;
            String id;
            String name;
            String dept;
            String recruitedBy;
            System.out.println("\n\tID\t\t\t\t Name\t\t Department\t\tRecruited By");
            while ((line = reader.readLine()) != null) {
                parts = line.split(",");
                id = parts[0];
                if(id.equals(empID)) {
                    name = parts[1];
                    dept = parts[2];
                    recruitedBy = parts[3];
                    System.out.println("\t" + id + "\t" + name + "\t\t" + dept + "\t\t\t" + recruitedBy);
                }
            }
        } catch (IOException e) {
           throw new OperationFailedException("Unable to read", e);
        }
    }

    protected static HashMap<String, String> getEmployeeFromFolder(String path){
        // Create a HashMap to store the data from CSV files
        HashMap<String, String> dataMap = new HashMap<>();

        // Create a File object representing the specified directory
        File directory = new File(path);

            // Get a list of all files in the directory
            File[] files = directory.listFiles();
            // Iterate over the files
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".csv")) {
                        // Read data from the CSV file and store it in the HashMap
                        readCSVFileAndStoreData(file, dataMap);
                    }
                }
            } else {
                return null;
            }
            return dataMap;
    }

    private static void readCSVFileAndStoreData(File file, HashMap<String, String> dataMap) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Skip header line
            reader.readLine();
            // Read data from the CSV file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Assuming first column is the key and second column is the value
                if (parts.length >= 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    dataMap.put(key, value);
                }
            }
        } catch (IOException e) {
            throw new OperationFailedException("Unable read data from" + file.getName(), e);
        }
    }

    //public abstract boolean addEmployee(String empID, String empName, String empDept, String recuritedBy, String bakeryName) throws IOException;
}
