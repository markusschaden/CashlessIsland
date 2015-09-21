package ch.avendia.cashless.employeeapp.nfc;

import java.io.Serializable;

/**
 * Created by Markus on 16.09.2015.
 */
public class Access implements Serializable {

    private int accessId;
    private String accessName;

    public Access() {

    }

    public Access(int accessId, String accessName) {
        this.accessId = accessId;
        this.accessName = accessName;
    }

    public int getAccessId() {
        return accessId;
    }

    public void setAccessId(int accessId) {
        this.accessId = accessId;
    }

    public String getAccessName() {
        return accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }
}
