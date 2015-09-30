package ch.avendia.cashless.employeeapp.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.avendia.cashless.employeeapp.nfc.Access;

/**
 * Created by Markus on 19.09.2015.
 */
public class AccessRecyclerAdapter extends RecyclerView.Adapter<AccessRecyclerAdapter.ViewHolder> {

    private List<Access> mItems;
    private View.OnClickListener listener;

    AccessRecyclerAdapter(List<Access> items, View.OnClickListener listener) {
        mItems = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.security_access_row, viewGroup, false);

        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String item = mItems.get(i).getAccessName();
        viewHolder.mTextView.setText(item);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTextView;
        private View.OnClickListener listener;

        ViewHolder(View v, View.OnClickListener listener) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.info_text);
            v.setOnClickListener(this);
            this.listener = listener;
        }


        @Override
        public void onClick(View v) {
            listener.onClick(v);
        }
    }

}