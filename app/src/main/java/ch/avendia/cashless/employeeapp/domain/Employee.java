package ch.avendia.cashless.employeeapp.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.avendia.cashless.employeeapp.nfc.Access;
import lombok.Data;
import lombok.ToString;

/**
 * Created by Markus on 17.09.2015.
 */
@Data
@ToString(callSuper = true)
public class Employee implements Serializable {

    private String name;
    private String firstName;
    private List<Role> roles;
    private String userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public List<Access> getAcccess() {
        List<Access> accesses = new ArrayList<>();
        for(Role role : roles) {
            accesses.addAll(role.getAccessList());
        }

        return accesses;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", roles=" + roles +
                ", userId='" + userId + '\'' +
                '}';
    }
}
