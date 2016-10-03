package ir.parhoonco.traccar.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import ir.parhoonco.traccar.R;
import ir.parhoonco.traccar.core.SocketController;

/**
 * Created by mao on 8/1/2016.
 */
public class DeviceGroupFragment extends Fragment {

    private ViewGroup layout;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private void init(ViewGroup layout , Map params){
        ArrayList<Map> users = (ArrayList<Map>) params.get("group");
        for (Map user :
                users) {
            TextView textView = new TextView(getContext());
            textView.setText((String)user.get("name") + ":" + (int)user.get("id"));
            layout.addView(textView);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (ViewGroup) inflater.inflate(R.layout.fragment_device_group , null);

        SocketController.getInstance().getDeviceGroup(CarFragment.defaultImei, new SocketController.CallBackResponse() {
            @Override
            public void didReceiveResponse(boolean success, Map<String, Object> params) {
                if (success) {
                    init(layout , params);
                }else {

                }
            }
        });

        return layout;
    }
}
