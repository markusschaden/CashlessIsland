package com.markusschaden.gadgeothek;

import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ch.avendia.cashless.employeeapp.domain.Employee;

/**
 * Created by Markus on 19.09.2015.
 */
public class EmployeeRecyclerAdapter extends RecyclerView.Adapter<EmployeeRecyclerAdapter.ViewHolder> {

    private List<Employee> mItems;
    private View.OnClickListener listener;

    EmployeeRecyclerAdapter(List<Employee> items, View.OnClickListener listener) {
        mItems = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_employeelist_row, viewGroup, false);

        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String item = mItems.get(i).getName() + " " + mItems.get(i).getFirstName() + " " + mItems.get(i).getRoles().toString();
        viewHolder.mTextView.setText(item);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTextView;
        private View.OnClickListener listener;

        ViewHolder(View v, View.OnClickListener listener)  {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.list_item);
            v.setOnClickListener(this);
            this.listener = listener;
        }


        @Override
        public void onClick(View v) {
            listener.onClick(v);
        }
    }

}