package ch.avendia.cashless.employeeapp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.avendia.cashless.employeeapp.domain.Employee;

/**
 * Created by Markus on 17.09.2015.
 */
public class EmployeeService {


    public Employee getEmployee(String uid) {
        if(uid == null) {
            return null;
        }
        for(Employee employee : getEmployees()) {
            if(uid.equals(employee.getUserId())) {
                return employee;
            }
        }
        return null;
    }

    public List<Employee> getEmployees() {

        List<Employee>employees = new ArrayList<>();

        Employee employee1 = new Employee();
        employee1.setFirstName("Admin");
        employee1.setName("Welt");
        employee1.setRoles(Arrays.asList(RoleService.getAdminRole()));
        employee1.setUserId("190bf7c4-f729-4b3c-8c22-eecc57a03b68");
        employees.add(employee1);

        Employee employee2 = new Employee();
        employee2.setFirstName("Security");
        employee2.setName("Welt");
        employee2.setRoles(Arrays.asList(RoleService.getSecurityRole()));
        employee2.setUserId("53b167f6-9bc4-47af-869c-446bdbabaa40");
        employees.add(employee2);

        Employee employee3 = new Employee();
        employee3.setFirstName("Entrance");
        employee3.setName("Welt");
        employee3.setRoles(Arrays.asList(RoleService.getEntranceRole()));
        employee3.setUserId("8be36294-dbf8-4bbd-be2c-8abcfe384173");
        employees.add(employee3);

        return employees;
    }
}
