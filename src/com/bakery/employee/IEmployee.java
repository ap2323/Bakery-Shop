package com.bakery.employee;

import java.io.IOException;

interface IEmployee {

    boolean addEmployee(String empID, String empName, String empDept,String recruitedBy ,String company) throws IOException;
    boolean removeEmployee(String path, String empID, String company) throws IOException;
}
