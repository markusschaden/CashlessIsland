package ch.avendia.cashless.employeeapp.view;

import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import ch.avendia.cashless.employeeapp.domain.Employee;
import ch.avendia.cashless.employeeapp.nfc.AccessCalculator;
import ch.avendia.cashless.employeeapp.nfc.dispatcher.CardHandler;
import ch.avendia.cashless.employeeapp.service.CashlessCardService;
import ch.avendia.cashless.employeeapp.service.EmployeeService;

/**
 * Created by Markus on 19.09.2015.
 */
public class EmployeeListFragment extends CashlessNfcCardFragment {

    List<Employee> items;
    Employee selectedEmployee;
    private ProgressDialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_employeelist, container, false);

        EmployeeService employeeService = new EmployeeService();
        items = employeeService.getEmployees();

        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new EmployeeRecyclerAdapter(items, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Item click nr: " + recyclerView.getChildAdapterPosition(v), Toast.LENGTH_SHORT).show();
                selectedEmployee = items.get(recyclerView.getChildAdapterPosition(v));

                dialog = ProgressDialog.show(getActivity(), "Writing Employee Data to Tag",
                        "Tab NFC Tag", true);
            }
        }));

        return rootView;
    }


    @Override
    public void cashlessCardDetected(Tag tag) {
        if (selectedEmployee != null) {
            new MifareEmployeeWriter().execute(tag);
        }

    }


    private class MifareEmployeeWriter extends AsyncTask<Tag, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            dialog.setMessage("Writing to tag");

        }

        @Override
        protected Boolean doInBackground(Tag... params) {
            Tag tag = params[0];

            try {
                CardHandler cardHandler = new CardHandler(tag);
                cardHandler.reset();

                Log.i("Employee writer: ", selectedEmployee.toString());
                int access = AccessCalculator.getAccessValue(selectedEmployee.getAcccess());
                Log.i("Employee access:", "Accesscode:" + access);

                CashlessCardService cashlessCardService = new CashlessCardService();

                cardHandler.writeEmployeeCard(cashlessCardService.generateUID(cardHandler.getCardId()), 0, access, selectedEmployee.getUserId(), 9999);


            } catch (IOException e) {
                return false;
            }


            return true;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            dialog.dismiss();
        }

    }


}
