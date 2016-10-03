package ir.parhoonco.traccar.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.ApplicationLoader;
import ir.parhoonco.traccar.core.model.api.Event;
import ir.parhoonco.traccar.ui.adapter.EventAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mao on 9/14/2016.
 */
public class EventFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_event, null);


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
        /*Loading Views*/
                LinearLayout loadingLayout = (LinearLayout) fragmentView.findViewById(R.id.loading_layout);
                final RecyclerView events = (RecyclerView) fragmentView.findViewById(R.id.events);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                events.setLayoutManager(layoutManager);

                final List<Event> eventList = CarFragment.defaultDevice.getEvents();
                if (eventList.size() > 0) {
                    ((ViewGroup) loadingLayout.getParent()).removeView(loadingLayout);
                    loadingLayout = null;
                }
                final EventAdapter adapter = new EventAdapter(getContext(), eventList);
                events.setAdapter(adapter);

                long sTime;
                if (eventList.size() > 0) {
                    sTime = eventList.get(eventList.size() - 1).getTime();
                } else {
                    sTime = 0;
                }
                Call<List<Event>> eventCall = ApplicationLoader.api.getEvents(CarFragment.defaultImei, sTime);
                final LinearLayout finalLoadingLayout = loadingLayout;
                eventCall.enqueue(new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, final Response<List<Event>> response) {
                        if (response.code() == 200) {
                            if (finalLoadingLayout != null) {
                                ((ViewGroup) finalLoadingLayout.getParent()).removeView(finalLoadingLayout);
                            }
                            eventList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (Event event :
                                            response.body()) {
                                        event.setDevice(CarFragment.defaultDevice);
                                        event.save();
                                    }
                                }
                            });
                            thread.start();

                        }
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        if (finalLoadingLayout != null) {
                            ((ViewGroup) finalLoadingLayout.getParent()).removeView(finalLoadingLayout);
                        }
                    }
                });
            }
        });

        return fragmentView;
    }
}
