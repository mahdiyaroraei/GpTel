package ir.parhoonco.traccar.ui.component;

import android.content.Context;
import android.os.CountDownTimer;

import ir.parhoonco.traccar.ui.FragmentHelper;
import ir.parhoonco.traccar.ui.fragment.PhoneVerifyFragment;
import ir.parhoonco.traccar.ui.fragment.VerificationFragment;

/**
 * Created by mao on 9/24/2016.
 */
public class Timer extends CountDownTimer {
    private Context context;
    public Timer(long millisInFuture, long countDownInterval , Context context) {
        super(millisInFuture, countDownInterval);
        this.context = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        long millis = millisUntilFinished;

        int seconds = (int) (millis / 1000) % 60;
        int minutes = (int) ((millis / (1000 * 60)) % 60);

        String ms = String
                .format("%02d:%02d", minutes, seconds);
        VerificationFragment.timerTextView.setText(ms);
    }

    @Override
    public void onFinish() {
        FragmentHelper.getInstance(context).addToStack(new PhoneVerifyFragment());
    }
}
