package ir.parhoonco.traccar.ui.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import ir.parhoonco.traccar.core.AndroidUtil;

/**
 * Created by mao on 9/22/2016.
 */
public class DrawView extends View {
    Paint paint = new Paint();
    private float progress = 1;
    private String color = "#4EC443";

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setStrokeWidth(0);
        paint.setColor(Color.parseColor(color));
        canvas.drawRect(dpToPx(13), dpToPx((int) (25 / progress)), dpToPx(87), dpToPx(70), paint);
    }

    public void setProgress(float value) {
        this.progress = value;
        if (progress < 0.25) {
            color = "#c44343";
        } else if (progress >= 0.25 && progress < 0.5) {
            color = "#e18518";
        } else if (progress >= 0.5 && progress < 0.75) {
            color = "#beb749";
        } else if (progress >= 0.75 && progress <= 1) {
            color = "#4EC443";
        }
        invalidate();
    }

    private int dpToPx(int dp) {
        return AndroidUtil.dpToPx(dp, getResources());
    }
}
