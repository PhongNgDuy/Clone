package src.busStation;

import java.util.LinkedList;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
public class Station extends JXMapViewer {
    String address;
    GeoPosition location;

    public Station(double latitude, double longitude) {
        this.location = new GeoPosition(latitude,longitude);
    }

    LinkedList<Bus> bus = new LinkedList<>();

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public LinkedList<Bus> getBus() {
        return bus;
    }
}
