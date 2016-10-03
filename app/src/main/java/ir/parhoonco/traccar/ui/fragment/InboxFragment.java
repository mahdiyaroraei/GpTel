package ir.parhoonco.traccar.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.SQLException;
import java.util.ArrayList;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.DatabaseHelper;
import ir.parhoonco.traccar.core.SocketController;
import ir.parhoonco.traccar.core.model.InboxItem;
import ir.parhoonco.traccar.core.model.PaymentItem;
import ir.parhoonco.traccar.ui.adapter.InboxAdapter;
import ir.parhoonco.traccar.ui.adapter.PaymentAdapter;

/**
 * Created by mao on 8/1/2016.
 */
public class InboxFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox , null);

        RecyclerView inboxList = (RecyclerView) view.findViewById(R.id.inboxList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        inboxList.setLayoutManager(layoutManager);
//            InboxAdapter adapter = new InboxAdapter((ArrayList<InboxItem>) DatabaseHelper.getHelper(getContext()).getInboxDao().queryBuilder().limit(10L).query(), getContext());
//            inboxList.setAdapter(adapter);


        return view;
    }
}
