package ir.parhoonco.traccar.core.model.api;

import java.util.List;

/**
 * Created by mao on 9/24/2016.
 */
public class Positions {
    private int positionscount;
    private List<Position> positions;

    public Positions(int positionscount, List<Position> positions) {
        this.positionscount = positionscount;
        this.positions = positions;
    }

    public int getPositionscount() {
        return positionscount;
    }

    public void setPositionscount(int positionscount) {
        this.positionscount = positionscount;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
}
