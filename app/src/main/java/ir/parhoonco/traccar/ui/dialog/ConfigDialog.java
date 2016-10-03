package ir.parhoonco.traccar.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.parhoonco.traccar.R;

/**
 * Created by mao on 9/3/2016.
 */
public class ConfigDialog {
    public TextView dialogButton;
    private Dialog dialog;
    public LinearLayout dialogLayout;

    public void showDialog(Activity activity, String msg) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_config);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        dialogLayout = (LinearLayout) dialog.findViewById(R.id.dialog_layout);

        dialogButton = (TextView) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void dimmisDialog(){
        dialog.dismiss();
    }

    public void addView(View view){
        dialogLayout.addView(view , new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
