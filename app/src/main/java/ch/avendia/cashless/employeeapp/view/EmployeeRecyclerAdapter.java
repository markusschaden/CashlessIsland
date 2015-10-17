package ch.avendia.cashless.employeeapp.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.avendia.cashless.employeeapp.domain.Employee;
import ch.avendia.cashless.employeeapp.domain.Role;
import ch.avendia.cashless.employeeapp.nfc.AccessCalculator;
import ch.avendia.cashless.employeeapp.service.RoleService;

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
        String item = mItems.get(i).getName() + " " + mItems.get(i).getFirstName();
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(Role role : mItems.get(i).getRoles()) {
            if(first) {
                first = false;
                sb.append(role.getName().toString());
            } else {
                sb.append(", ").append(role.getName().toString());
            }
        }

        int access = AccessCalculator.getAccessValue(mItems.get(i).getAcccess());

        sb.append(" (" + access + ")");
        viewHolder.rolesList.setText(sb.toString());
        viewHolder.mTextView.setText(item);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTextView;
        private final TextView rolesList;
        private View.OnClickListener listener;

        ViewHolder(View v, View.OnClickListener listener) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.list_item);
            rolesList = (TextView) v.findViewById(R.id.roles_list_item);
            v.setOnClickListener(this);
            this.listener = listener;
        }


        @Override
        public void onClick(View v) {
            listener.onClick(v);
        }
    }

}