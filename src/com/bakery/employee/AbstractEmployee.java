package com.bakery.employee;

import com.bakery.OperationFailedException;
import com.bakery.WriteControl;
import com.bakery.UserAlreadyFoundException;
import com.bakery.UserNotFoundException;

import java.io.*;

public abstract class AbstractEmployee extends EmployeeHelper implements IEmployee {

    @Override
    public boolean addEmployee(String empID, String empName, String empDept, String recruitedBy,String bakeryName ) throws UserAlreadyFoundException, OperationFailedException,IOException {
            if(empID.endsWith("@Employee")) {
                if (isValidEmployee(empID, bakeryName)) {
                    saveEmployee(empID, empName, empDept,recruitedBy ,bakeryName);
                }
            } else {
                throw  new IllegalArgumentException("Employee ID invalid. Must end with '@Employee'.");
            }

        return true;
    }

    @Override
    public boolean removeEmployee(String path, String empID, String bakeryName) throws IOException {
        try {
            if(!empID.contains("@Employee")) throw new IllegalArgumentException("Employee id must end with '@Employee'");
            isValidEmployee(empID,bakeryName);
            throw new UserNotFoundException("Employee not found!");
        }catch (UserAlreadyFoundException ue) {
            WriteControl.remove(path, empID);
        }
        return true;
    }

    public abstract void displayEmployeeDetails(String empID, String bakeryName) throws IOException;
}
