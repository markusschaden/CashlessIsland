package ch.avendia.cashless.employeeapp.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.avendia.cashless.employeeapp.domain.TicketCategory;
import ch.avendia.cashless.employeeapp.nfc.Access;
import ch.avendia.cashless.employeeapp.nfc.AccessCalculator;

/**
 * Created by Markus on 19.09.2015.
 */
public class TicketCategoryAdapter extends RecyclerView.Adapter<TicketCategoryAdapter.ViewHolder> {

    private List<TicketCategory> mItems;
    private View.OnClickListener listener;

    TicketCategoryAdapter(List<TicketCategory> items, View.OnClickListener listener) {
        mItems = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ticket_row, viewGroup, false);

        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String item = mItems.get(i).getName();
        String price = String.format("%.2f", mItems.get(i).getPrice());

        viewHolder.title.setText(item);
        viewHolder.price.setText(price);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView title;
        private final TextView price;
        private View.OnClickListener listener;

        ViewHolder(View v, View.OnClickListener listener) {
            super(v);
            title = (TextView) v.findViewById(R.id.info_text);
            price = (TextView) v.findViewById(R.id.price_text);
            v.setOnClickListener(this);
            this.listener = listener;
        }


        @Override
        public void onClick(View v) {
            listener.onClick(v);
        }
    }

}