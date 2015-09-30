package ch.avendia.cashless.employeeapp.view;

import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.avendia.cashless.employeeapp.nfc.dispatcher.CardHandler;

/**
 * A placeholder fragment containing a simple view.
 */
public class PayInFragment extends CashlessNfcCardFragment {

    private ProgressDialog dialog;
    private AsyncTask<Tag, Void, Boolean> currentAsyncTask;
    private Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    private Button buttonBack, buttonPoint, buttonCharge, buttonCancel;
    private TextView resultView;
    List<Integer> numberList = new ArrayList<>();
    private int newCash = 0;

    public PayInFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pay_in, container, false);

        setupButtons(rootView);
        resultView = (TextView) rootView.findViewById(R.id.sum);

        return rootView;
    }


    private void addNumber(int number) {
        if (numberList.size() == 1 && numberList.get(0) == 0) {
            numberList.clear();
        }
        numberList.add(number);
        updateTextView();
    }

    private void back() {
        if (numberList.size() > 1) {
            numberList.remove(numberList.size() - 1);


        } else {
            numberList.clear();
            numberList.add(0);
        }

        updateTextView();
    }

    private void updateTextView() {
        StringBuilder sb = new StringBuilder();
        for (Integer i : numberList) {
            sb.append(i);
        }
        newCash = Integer.parseInt(sb.toString());
        resultView.setText(sb.toString());
    }

    private void charge() {
        currentAsyncTask = new CashWriter();
        showWaitingDialog();
    }


    private void showWaitingDialog() {

        dialog = ProgressDialog.show(getActivity(), "Please wait...",
                "Tab NFC Tag", true);

    }


    private class CashWriter extends AsyncTask<Tag, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Writing to Tag...");

        }

        @Override
        protected Boolean doInBackground(Tag... params) {
            Tag tag = params[0];

            try {
                CardHandler cardHandler = new CardHandler(tag);

                int cash = cardHandler.getCash();
                Log.i("Cash Pay", "Old cash:" + cash + ", newCash: " + newCash + ", Totalcash: " + (cash + newCash));
                cash += newCash;
                cardHandler.writeCash(cash);

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


    private void setupButtons(View rootView) {

        button0 = (Button) rootView.findViewById(R.id.button0);
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumber(0);
            }
        });
        button1 = (Button) rootView.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumber(1);
            }
        });
        button2 = (Button) rootView.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumber(2);
            }
        });
        button3 = (Button) rootView.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumber(3);
            }
        });
        button4 = (Button) rootView.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumber(4);
            }
        });
        button5 = (Button) rootView.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumber(5);
            }
        });
        button6 = (Button) rootView.findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumber(6);
            }
        });
        button7 = (Button) rootView.findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumber(7);
            }
        });
        button8 = (Button) rootView.findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumber(8);
            }
        });
        button9 = (Button) rootView.findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumber(9);
            }
        });
        buttonBack = (Button) rootView.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        buttonCharge = (Button) rootView.findViewById(R.id.buttonCharge);
        buttonCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                charge();
            }
        });
    }

}
