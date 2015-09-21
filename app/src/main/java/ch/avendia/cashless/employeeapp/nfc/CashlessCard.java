package ch.avendia.cashless.employeeapp.nfc;
/**
 * Created by Markus on 16.09.2015.
 */
public class CashlessCard {

    private String uid;
    private int cash;
    private int access;
    private String userId;
    private int eventId;
    private CashlessCardType cashlessCardType;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public CashlessCardType getCashlessCardType() {
        return cashlessCardType;
    }

    public void setCashlessCardType(CashlessCardType cashlessCardType) {
        this.cashlessCardType = cashlessCardType;
    }

    @Override
    public String toString() {
        return "CashlessCard{" +
                "uid='" + uid + '\'' +
                ", cash=" + cash +
                ", access=" + access +
                ", userId='" + userId + '\'' +
                ", eventId=" + eventId +
                ", cashlessCardType=" + cashlessCardType +
                '}';
    }
}
