import com.bakery.*;

import com.bakery.admin.Admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Bakery {

    private String bakeryName;
    private final Admin admin = new Admin();
    ArrayList<String> admins = new ArrayList<>();

    Bakery() {

    }
    Bakery(String bakeryName){

        this.bakeryName = bakeryName;
        createBakery();
    }

    public String getAdmin(){
       return admin.getAdminID();
    }

    public String getBakeryName(){
        return bakeryName;
    }

    private void createBakery() {
        try {
            WriteControl.createDirectory("./src/Bakeries/",this.bakeryName);
            System.out.println(this.bakeryName + " created!");
        } catch (OperationFailedException oe){
            System.out.println(oe.getMessage());
        }

    }
    private String[] read(String id, String bakeryName) throws IOException {
        File fileName = new File("./src/Bakeries/" + bakeryName + "/Admin/Admin.csv");
        //String path = fileName.getAbsolutePath();
        String line;
        String[] parts;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {

            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {

                parts = line.split(",");
                if(parts[0].equals(id)) return new String[]{parts[0].trim(), parts[1].trim()};

            }
        }
        return new String[0];
    }
    private void adminLogin(Scanner scanner) {
        scanner.nextLine();
        System.out.println("\nEnter Admin ID:");
        String adminID = scanner.nextLine();

        System.out.println("Enter Bakery Name: ");
        String bakeryName = scanner.nextLine();

        if(isBakeryFound(bakeryName)) {
            this.bakeryName = bakeryName;
            try {

                String[] values = read(adminID, this.bakeryName);

                if (values.length == 0) {
                    throw new UserNotFoundException("Admin not found!");
                }
                Admin admin1 = new Admin(values[0], values[1], this.bakeryName);

                admin1.mainMenu();
            } catch (UserNotFoundException | IOException ue) {
                System.out.println(ue.getMessage());
            }
        } else {
           throw new OperationFailedException("No such bakery found!");
        }


    }
    private void input() {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");

            System.out.println("\nEnter choice:");
            int choice = scanner.nextInt();

            switch (choice){
                case 1:
                    try {
                        adminLogin(scanner);
                    }catch (UserNotFoundException | OperationFailedException ex){
                        System.err.println(ex.getMessage());
                    }
                    break;
                case 2:
                    adminRegister(scanner);
                    break;
                case 3:
                    System.exit(0);
            }
        }

    }

        /*Bakery bakery1 = new Bakery("Chellas");
        Admin admin1 = new Admin("1010", "Arun", bakery1.getBakeryName());
        admin1.addEmployee("1011@Employee","Prakash","Sales");
        admin1.displayEmployee("1011@Employee");
        admin1.DisplayAllEmployees();

        Bakery bakery2 = new Bakery("Velayutha Nadar");
        Admin admin2 = new Admin("1020","Venna",bakery2.getBakeryName());
        admin2.addEmployee("1010@Employee", "Brain","Sales");
        admin2.DisplayAllEmployees();*/

    private void adminRegister(Scanner scanner) {
        scanner.nextLine();
        System.out.println("Enter Bakery Name: ");
        String bakeryName = scanner.nextLine();

        this.bakeryName = bakeryName;

        if(!isBakeryFound(bakeryName)){

            Bakery bakery = new Bakery(bakeryName);
            System.out.println("Enter Admin ID: ");
            String adminID = scanner.nextLine();

            System.out.println("Enter Admin Name: ");
            String adminName = scanner.nextLine();
            try {
                admin.addAdmin(adminID, adminName, bakeryName);
            } catch (UserAlreadyFoundException | IOException | OperationFailedException ie) {
                System.out.println(ie.getMessage());
            }
        } else {
            System.out.println("Enter Admin ID: ");
            String adminID = scanner.nextLine();

            System.out.println("Enter Admin Name: ");
            String adminName = scanner.nextLine();
            try {

                admin.addAdmin(adminID, adminName, bakeryName);
            } catch (UserAlreadyFoundException | IOException | OperationFailedException ie) {
                System.out.println(ie.getMessage());
            }
        }

    }

    private boolean isBakeryFound(String bakeryName) {
        // Specify the specified path where you want to check for the directory
        String specifiedPath = "./src/Bakeries/";

        // Construct the absolute path of the directory
        String directoryPath = specifiedPath + File.separator + bakeryName;

        // Create a File object representing the specified directory path
        File directory = new File(directoryPath);

        return directory.exists() && directory.isDirectory();
    }

    public static void main(String[] args) {
        Bakery bakery = new Bakery();
        bakery.input();
    }
}
