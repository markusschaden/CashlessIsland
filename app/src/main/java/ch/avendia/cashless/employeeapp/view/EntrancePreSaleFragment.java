package ch.avendia.cashless.employeeapp.view;

import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.avendia.cashless.employeeapp.domain.Ticket;
import ch.avendia.cashless.employeeapp.nfc.dispatcher.CardHandler;
import ch.avendia.cashless.employeeapp.service.TicketService;

/**
 * A placeholder fragment containing a simple view.
 */
public class EntrancePreSaleFragment extends CashlessNfcCardFragment implements SimpleScannerFragment.BarcodeNotifier {

    private ProgressDialog dialog;
    private AsyncTask<Tag, Void, Boolean> currentAsyncTask;

    private TextView touchToScan;
    private TextView ticketStatus;
    private LinearLayout scannerView;
    private SimpleScannerFragment simpleScannerFragment;
    private LinearLayout touchToScanLayout;
    private RelativeLayout ticketInfos;
    Ticket activeTicket;

    public EntrancePreSaleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_entrance_presale, container, false);

        scannerView = (LinearLayout)rootView.findViewById(R.id.scannerView);

        ticketStatus = (TextView) rootView.findViewById(R.id.ticketStatus);
        touchToScan = (TextView) rootView.findViewById(R.id.touchToScan);
        ticketInfos = (RelativeLayout)rootView.findViewById(R.id.ticketInfos);
        touchToScanLayout = (LinearLayout) rootView.findViewById(R.id.touchToScanLayout);
        touchToScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleScannerFragment = new SimpleScannerFragment();
                simpleScannerFragment.setBarcodeNotifier(EntrancePreSaleFragment.this);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.scannerView, simpleScannerFragment).commit();
                ticketInfos.setVisibility(View.GONE);
                touchToScan.setVisibility(View.GONE);
                touchToScanLayout.setBackgroundColor(0x00000000);

            }
        });

        return rootView;
    }


    private void showWaitingDialog() {

        dialog = ProgressDialog.show(getActivity(), "Please wait...",
                "Tab NFC Tag", true);

    }

    @Override
    public void onBarcode(String value) {
        touchToScan.setVisibility(View.VISIBLE);
        ticketInfos.setVisibility(View.VISIBLE);
        touchToScanLayout.setBackgroundColor(0xffcccccc);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(simpleScannerFragment).commit();

        TicketService ticketService = new TicketService();
        Ticket ticket = ticketService.validateQRTicket(value);
        if(ticket.isActive()) {
            ticketStatus.setText("Status: active" + "\n" + "Category: " + ticket.getTicketCategory().getName() + "\nUID: " + ticket.getUid());
            ticketInfos.setBackgroundColor(0xFF689F38);
        } else {
            ticketStatus.setText("Status: inactive" + "\n" + "Category: " + ticket.getTicketCategory().getName() + "\nUID: " + ticket.getUid());
            ticketInfos.setBackgroundColor(0xFFFF5722);
        }
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

                //cardHandler.writeClientTicket(uid, 0, access);

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
