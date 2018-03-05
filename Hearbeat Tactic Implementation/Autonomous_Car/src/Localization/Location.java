package Localization;

/**
 * Created by Heena on 2/20/2018.
 */
public class Location {
    String co_ords;
    int time;

    public Location(String co_ords, int time) {
        this.co_ords = co_ords;
        this.time = time;
    }

    public String getCo_ords() {
        return co_ords;
    }

    public void setCo_ords(String co_ords) {
        this.co_ords = co_ords;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
