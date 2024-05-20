package com.bakery;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class WriteControl {

        private static boolean checkExistingDirectory(String directoryPath) {

            // Create a Path object representing the specified directory path
            Path path = Paths.get(directoryPath);

            // Check if the directory exists
            return Files.exists(path) && Files.isDirectory(path);
        }
        public static void createDirectory(String path, String name){

            if(!checkExistingDirectory(path + name)){
                // Specify the path for the new directory
                Path fullPath;

                try {
                    fullPath = Paths.get(path, name);
                    Files.createDirectory(fullPath);

                    fullPath = Paths.get(fullPath.toString(), "Admin");
                    Files.createDirectory(fullPath);

                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fullPath + "/Admin.csv"));

                    fullPath = Paths.get(path + name  , "Employee");
                    Files.createDirectory(fullPath);

                    bufferedWriter = new BufferedWriter(new FileWriter(fullPath + "/Employee.csv"));

                    fullPath = Paths.get(path + name  , "CustomerTransaction");
                    Files.createDirectory(fullPath);

                    bufferedWriter = new BufferedWriter(new FileWriter(fullPath + "/CustomerTransaction.csv"));

                    fullPath = Paths.get(path + name  , "Stock");
                    Files.createDirectory(fullPath);

                    bufferedWriter = new BufferedWriter(new FileWriter(fullPath + "/StockList.csv"));
                    bufferedWriter = new BufferedWriter(new FileWriter(fullPath + "/PriceList.csv"));

                    bufferedWriter.close();
                } catch (IOException e) {
                    throw new OperationFailedException("Unable to createStockList directory", e);
                }
            }
        }

    public static void update(String path, String oldValue, String value, String type) throws IOException {
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            // Read all lines from the CSV file into a list
            ArrayList<String> lines = new ArrayList<>();
            reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            lines.trimToSize();

            String[] fields;
            // Update the ID in each line
            switch (type) {
                case "id":
                    for (int i = 0; i < lines.size(); i++) {
                        fields = lines.get(i).split(",");

                        if (fields[0].trim().equalsIgnoreCase(oldValue)) {

                            fields[0] = value;
                            lines.set(i, String.join(", ", fields));

                            break;
                        }
                    }
                    break;
                case "name":
                case "password":
                    for (int i = 0; i < lines.size(); i++) {
                        fields = lines.get(i).split(",");

                        if (fields[0].trim().equalsIgnoreCase(oldValue)) {

                            fields[1] = value;
                            lines.set(i, String.join(", ", fields));

                            break;
                        }
                    }
                    break;

                case "dept":
                    for (int i = 0; i < lines.size(); i++) {
                        fields = lines.get(i).split(",");

                        if (fields[0].trim().equals(oldValue)) {

                            fields[2] = value;
                            lines.set(i, String.join(", ", fields));

                            break;
                        }
                    }
                    break;
                case "cusName":
                    for (int i = 0; i < lines.size(); i++) {
                        fields = lines.get(i).split(",");

                        if (fields[0].trim().equalsIgnoreCase(oldValue)) {

                            fields[2] = value;
                            lines.set(i, String.join(", ", fields));

                            break;
                        }
                    }
                    break;
                case "price":
                    for (int i = 0; i < lines.size(); i++) {
                        fields = lines.get(i).split(",");

                        if (fields[0].trim().equalsIgnoreCase(oldValue)) {

                            fields[3] = value;
                            lines.set(i, String.join(", ", fields));

                            break;
                        }
                    }
                    break;
            }

            // Write the updated lines back to the CSV file
            writer = new BufferedWriter(new FileWriter(path));
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }

        } finally {
            assert reader != null;
            reader.close();
            assert writer != null;
            writer.close();
        }
    }

    public static void remove(String path, String ID) throws IOException {
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            // Read all lines from the CSV file into a list
            ArrayList<String> lines = new ArrayList<>();
            reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            lines.trimToSize();

            String[] fields;

            // Remove the record with the specified ID
            ArrayList<String> updatedLines = new ArrayList<>();
            for (String record : lines) {
                fields = record.split(",");
                if (!fields[0].trim().equalsIgnoreCase(ID)) {
                    updatedLines.add(record);
                }
            }

            updatedLines.trimToSize();

            // Write the updated lines back to the CSV file
            writer = new BufferedWriter(new FileWriter(path));
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }


        } finally {
            assert reader != null;
            reader.close();
            assert writer != null;
            writer.close();
        }
    }
}

