package Localization;

/**
 * Created by Heena on 2/20/2018.
 */
public class Location {
    int co_ords;
    int time;

    public Location(int co_ords, int time) {
        this.co_ords = co_ords;
        this.time = time;
    }

    public int getCo_ords() {
        return co_ords;
    }

    public void setCo_ords(int co_ords) {
        this.co_ords = co_ords;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
