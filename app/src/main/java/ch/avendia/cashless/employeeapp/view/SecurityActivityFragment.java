package ch.avendia.cashless.employeeapp.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ch.avendia.cashless.employeeapp.nfc.Access;
import ch.avendia.cashless.employeeapp.service.AccessService;

/**
 * A placeholder fragment containing a simple view.
 */
public class SecurityActivityFragment extends Fragment {

    private List<Access> items;
    private Access selectedAccess;
    private OnAccessSelected mCallback;

    public SecurityActivityFragment() {

    }


    // Container Activity must implement this interface
    public interface OnAccessSelected {
        void onAccessSelected(Access acccess);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnAccessSelected) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_security, container, false);

        AccessService accessService = new AccessService();
        items = accessService.getAccessList();

        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new AccessRecyclerAdapter(items, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Item click nr: " + recyclerView.getChildAdapterPosition(v), Toast.LENGTH_SHORT).show();
                selectedAccess = items.get(recyclerView.getChildAdapterPosition(v));
                mCallback.onAccessSelected(selectedAccess);
            }
        }));


        return rootView;
    }
}
