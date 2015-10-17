package ch.avendia.cashless.employeeapp.view;

import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.avendia.cashless.employeeapp.domain.Ticket;
import ch.avendia.cashless.employeeapp.domain.TicketCategory;
import ch.avendia.cashless.employeeapp.nfc.AccessCalculator;
import ch.avendia.cashless.employeeapp.nfc.dispatcher.CardHandler;
import ch.avendia.cashless.employeeapp.service.TicketService;

/**
 * A placeholder fragment containing a simple view.
 */
public class EntranceBoxOfficeFragment extends CashlessNfcCardFragment implements View.OnClickListener {

    private ProgressDialog dialog;
    private AsyncTask<Tag, Void, Boolean> currentAsyncTask;
    private RecyclerView recyclerView;
    private List<TicketCategory> categories;
    private Ticket currentTicket;

    public EntranceBoxOfficeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_entrance_boxoffice, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview);
        //recyclerView.addItemDecoration(new MarginDecoration(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        TicketService ticketService = new TicketService();

        categories = ticketService.getCategories();
        recyclerView.setAdapter(new TicketCategoryAdapter(categories, this));

        return rootView;
    }



    private void showWaitingDialog() {

        dialog = ProgressDialog.show(getActivity(), "Please wait...",
                "Tab NFC Tag", true);

    }

    @Override
    public void onClick(View v) {
        int index = recyclerView.getChildAdapterPosition(v);
        TicketCategory ticketCategory = categories.get(index);
        TicketService ticketService = new TicketService();
        currentTicket = ticketService.bookTicket(ticketCategory);
        currentAsyncTask = new TicketWriter();
        showWaitingDialog();
    }


    private class TicketWriter extends AsyncTask<Tag, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Writing to Tag...");

        }

        @Override
        protected Boolean doInBackground(Tag... params) {
            Tag tag = params[0];

            try {
                CardHandler cardHandler = new CardHandler(tag);
                int access = AccessCalculator.getAccessValue(currentTicket.getTicketCategory().getAccessList());
                cardHandler.writeClientTicket(currentTicket.getUid(), 0, access);

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }


            return true;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            dialog.dismiss();
        }

    }


    @Override
    public void cashlessCardDetected(Tag tag) {
        if (currentAsyncTask != null) {
            currentAsyncTask.execute(tag);
        }
    }


}
