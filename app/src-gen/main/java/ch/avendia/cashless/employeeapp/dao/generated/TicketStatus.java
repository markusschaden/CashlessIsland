package ch.avendia.cashless.employeeapp.dao.generated;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "TICKET_STATUS".
 */
public class TicketStatus {

    private Long id;
    private String desc;

    public TicketStatus() {
    }

    public TicketStatus(Long id) {
        this.id = id;
    }

    public TicketStatus(Long id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
