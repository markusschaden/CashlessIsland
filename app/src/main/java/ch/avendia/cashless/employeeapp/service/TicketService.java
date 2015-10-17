package ch.avendia.cashless.employeeapp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.avendia.cashless.employeeapp.domain.Ticket;
import ch.avendia.cashless.employeeapp.domain.TicketCategory;

/**
 * Created by Markus on 17.09.2015.
 */
public class TicketService {

    public Ticket validateQRTicket(String qrcode) {

        Ticket ticket = new Ticket();
        TicketCategory ticketCategory = new TicketCategory("Freitag", 60, Arrays.asList(AccessService.getMainArea()));
        ticket.setActive(true);
        ticket.setUid("2a753c8e-00b5-4983-adc7-b078a28e143c");
        ticket.setTicketCategory(ticketCategory);

        return ticket;
    }

    public void bookTicket(String uid) {

    }

    public Ticket bookTicket(TicketCategory ticketCategory) {

        Ticket ticket = new Ticket();
        ticket.setActive(true);
        ticket.setUid("2a753c8e-00b5-4983-adc7-b078a28e143c");
        ticket.setTicketCategory(ticketCategory);

        return ticket;
    }


    public List<TicketCategory> getTickets() {



        return null;
    }


    public List<TicketCategory> getCategories() {

        List<TicketCategory> categories = new ArrayList<>();
        categories.add(new TicketCategory("Freitag", 60, Arrays.asList(AccessService.getMainArea())));
        categories.add(new TicketCategory("Samstag", 70, Arrays.asList(AccessService.getMainArea())));
        categories.add(new TicketCategory("Sonntag", 50, Arrays.asList(AccessService.getMainArea())));
        categories.add(new TicketCategory("Freitag+Samstag", 110, Arrays.asList(AccessService.getMainArea())));
        categories.add(new TicketCategory("Samstag+Sonntag", 100, Arrays.asList(AccessService.getMainArea())));
        categories.add(new TicketCategory("3-Tages Pass", 130, Arrays.asList(AccessService.getMainArea())));
        return categories;
    }
}
