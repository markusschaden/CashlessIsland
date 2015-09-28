package ch.avendia.cashless.employeeapp.dao;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;


public class CashlessDatabaseGenerator {

    public static void main(String args[]) throws Exception {

        Schema schema = new Schema(3, "ch.avendia.cashless.employeeapp.dao.generated");
        Entity ageCategorie = schema.addEntity("AgeCategorie");
        ageCategorie.addIdProperty();
        ageCategorie.addStringProperty("desc");

        Entity ticketStatus = schema.addEntity("TicketStatus");
        Property ticketStatusIdProperty = ticketStatus.addIdProperty().getProperty();
        ticketStatus.addStringProperty("desc");

        Entity ticketCategorie = schema.addEntity("TicketCategorie");
        ticketCategorie.addIdProperty();
        ticketCategorie.addStringProperty("name");
        ticketCategorie.addLongProperty("price");

        Entity ticket = schema.addEntity("Ticket");
        ticket.addIdProperty();
        ticket.addStringProperty("uid");
        //ticket.addToOne(ticketStatus, ticketStatusIdProperty);

        Entity nfcTag = schema.addEntity("NFCTag");
        nfcTag.addIdProperty();
        nfcTag.addStringProperty("uid");

        new DaoGenerator().generateAll(schema, "app/src-gen/main/java");

    }

}
