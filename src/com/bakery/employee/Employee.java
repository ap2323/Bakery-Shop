package com.bakery.employee;

import com.bakery.OperationFailedException;
import com.bakery.UserAlreadyFoundException;
import com.bakery.UserNotFoundException;

import java.io.IOException;
import java.util.HashMap;

public class Employee extends AbstractEmployee {

    public void displayEmployeeDetails(String empID, String bakeryName) throws IOException {
            String path;
            if(empID.endsWith("@Employee")) {
                try {
                    isValidEmployee(empID, bakeryName);
                    throw new UserNotFoundException("Employee not Found");
                }catch (UserAlreadyFoundException ue){
                    path = "./src/Bakeries/" + bakeryName + "/Employee/Employee.csv";
                    EmployeeHelper.displayEmployee(path, empID);

                }


            } else {
                throw new IllegalArgumentException("Invalid Employee ID. Must end with '@Employee");
            }
    }



    public HashMap<String, String> getEmployees(String bakeryName){
        String path;

        path = "./src/Bakeries/" + bakeryName + "/Employee/";

        try {
            return EmployeeHelper.getEmployeeFromFolder(path);
        }catch (OperationFailedException oe){
            System.out.println(oe.getMessage());
        }
        return null;
    }


}
