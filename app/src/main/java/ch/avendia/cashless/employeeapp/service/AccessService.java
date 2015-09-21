package ch.avendia.cashless.employeeapp.service;

import java.util.ArrayList;
import java.util.List;

import ch.avendia.cashless.employeeapp.nfc.Access;

/**
 * Created by Markus on 19.09.2015.
 */
public class AccessService {

    public List<Access> getAccessList() {

        List<Access> accessList = new ArrayList<>();

        accessList.add(getMainArea());
        accessList.add(getBackstage());
        accessList.add(getMaintenance());


        return accessList;
    }


    public static Access getMainArea() {
        return new Access(1, "Main Area");
    }

    public static Access getBackstage() {
        return new Access(2, "Backstage");
    }

    public static Access getMaintenance() {
        return new Access(3, "Maintenance");
    }


}
