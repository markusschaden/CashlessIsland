package ch.avendia.cashless.employeeapp.domain;

import java.io.Serializable;
import java.util.List;

import ch.avendia.cashless.employeeapp.nfc.Access;
import lombok.Data;
import lombok.ToString;

/**
 * Created by Markus on 17.09.2015.
 */
@Data
@ToString(callSuper = true)
public class Role implements Serializable{

    private RoleName name;
    private List<Access> accessList;

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    public List<Access> getAccessList() {
        return accessList;
    }

    public void setAccessList(List<Access> accessList) {
        this.accessList = accessList;
    }
}
