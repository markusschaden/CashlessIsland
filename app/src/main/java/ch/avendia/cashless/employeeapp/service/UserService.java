package ch.avendia.cashless.employeeapp.service;

import java.util.ArrayList;
import java.util.List;

import ch.avendia.cashless.employeeapp.domain.CashlessSettings;
import ch.avendia.cashless.employeeapp.domain.Employee;
import ch.avendia.cashless.employeeapp.domain.Role;
import ch.avendia.cashless.employeeapp.domain.RoleName;
import ch.avendia.cashless.employeeapp.nfc.Access;

/**
 * Created by Markus on 17.09.2015.
 */
public class UserService {

    public CashlessSettings authenticate(String username, String password) {





        return null;
    }

    public CashlessSettings authenticate(String uid) {

        CashlessSettings cashlessSettings = new CashlessSettings();
        cashlessSettings.setNfcKey("000000000");
        cashlessSettings.setSession("123456789");

        /*Employee employee = new Employee();
        employee.setName("Test");
        employee.setFirstName("Rudolf");
        Role role = new Role();
        role.setName(RoleName.ADMIN);

        List<Role> roles = new ArrayList<>();
        roles.add(role);

        employee.setRoles(roles);*/
        EmployeeService employeeService = new EmployeeService();
        Employee employee = employeeService.getEmployee(uid);

        cashlessSettings.setEmployee(employee);

        return cashlessSettings;
    }






}
