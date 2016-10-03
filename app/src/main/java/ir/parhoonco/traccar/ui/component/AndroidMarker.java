package ir.parhoonco.traccar.ui.component;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.overlay.Marker;

import ir.parhoonco.traccar.core.model.api.Position;

/**
 * Created by mao on 9/17/2016.
 */
public class AndroidMarker extends Marker {
    private Position position;
    private boolean isBubbleVisible = false;
    private OnMarkerTap tap;

    /**
     * @param latLong          the initial geographical coordinates of this marker (may be null).
     * @param bitmap           the initial {@code Bitmap} of this marker (may be null).
     * @param horizontalOffset the horizontal marker offset.
     * @param verticalOffset   the vertical marker offset.
     */
    public AndroidMarker(Position position, Bitmap bitmap, int horizontalOffset, int verticalOffset) {
        super(new LatLong(position.getLat(), position.getLon()), bitmap, horizontalOffset, verticalOffset);
        this.position = position;
    }

    public void setOnTap(OnMarkerTap tap) {
        this.tap = tap;
    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        double centerX = layerXY.x + getHorizontalOffset();
        double centerY = layerXY.y + getVerticalOffset();

        double radiusX = (getBitmap().getWidth() / 2) *1.1;
        double radiusY = (getBitmap().getHeight() / 2) *1.1;


        double distX = Math.abs(centerX - tapXY.x);
        double distY = Math.abs(centerY - tapXY.y);


        if( distX < radiusX && distY < radiusY){

            if(tap != null){
                tap.onTap();
                return true;
            }
        }

        return false;
    }

    public boolean isBubbleVisible() {
        return isBubbleVisible;
    }

    public void setBubbleVisible(boolean bubbleVisible) {
        isBubbleVisible = bubbleVisible;
    }

    public interface OnMarkerTap {
        void onTap();
    }
}
