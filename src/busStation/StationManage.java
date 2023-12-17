package busStation;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.util.PointList;
import graph.Graph;
import graph.Node;

import java.util.*;


public class StationManage {

    Graph<Station> graph = new Graph<>();
    Map<String, Bus> busMap = new HashMap<>();
    Map<String, Node<Station>> stationMap = new HashMap<>();

    public StationManage() {
        insertBus();
        insertStation();
        insertWay();
    }

    public void insertBus() {
        busMap.put("01", new Bus("01","Bus HN"));
        busMap.put("03", new Bus("03","Bus HN"));
        busMap.put("11", new Bus("11","VinBus"));
        busMap.put("15", new Bus("15","VinBus"));
        busMap.put("22", new Bus("22","Bus HN"));
        busMap.put("34", new Bus("34","VinBus"));
        busMap.put("27", new Bus("27","VinBus"));
        busMap.put("28", new Bus("28","Bus HN"));
        busMap.put("29", new Bus("29","VinBus"));
    }

    public void insertStation() {
        List<Station> stations = new ArrayList<>();

        Station station = new Station(21.019550, 105.937141);
        station.setAddress("Ben xe Gia Lam");
        station.getBus().add(busMap.get("28"));
        station.getBus().add(busMap.get("29"));
        stations.add(station);

        station = new Station(20.954930, 105.733580);
        station.setAddress("Ben xe Yen Nghia");
        station.getBus().add(busMap.get("15"));
        station.getBus().add(busMap.get("03"));
        stations.add(station);

        station = new Station(20.985240, 105.844040);
        station.setAddress("Ben xe Giap Bat");
        station.getBus().add(busMap.get("22"));
        station.getBus().add(busMap.get("01"));
        stations.add(station);

        station = new Station(20.96564, 105.84240);
        station.setAddress("Ben xe Nuoc Ngam");
        station.getBus().add(busMap.get("01"));
        station.getBus().add(busMap.get("15"));
        stations.add(station);

        station = new Station(21.009534, 105.866453);
        station.setAddress("Ben xe Luong Yen");
        station.getBus().add(busMap.get("22"));
        station.getBus().add(busMap.get("28"));
        station.getBus().add(busMap.get("34"));
        stations.add(station);

        station = new Station(21.032517, 105.829503);
        station.setAddress("Ben xe Kim Ma");
        station.getBus().add(busMap.get("29"));
        station.getBus().add(busMap.get("11"));
        station.getBus().add(busMap.get("27"));
        stations.add(station);

        station = new Station(21.02793942475455, 105.77885392652553);
        station.setAddress("Ben xe My Dinh");
        station.getBus().add(busMap.get("27"));
        station.getBus().add(busMap.get("03"));
        stations.add(station);

        station = new Station(21.025251, 105.841417);
        station.setAddress("Ga Ha Noi");
        station.getBus().add(busMap.get("34"));
        station.getBus().add(busMap.get("11"));
        stations.add(station);

        for (Station i : stations) {
            Node<Station> stationNode = new Node<>(i);
            stationMap.put(i.getAddress(),stationNode);
            graph.getNodeList().add(stationNode);
        }
    }

    public void insertWay() {
        stationMap.get("Ben xe Yen Nghia").addDestination(stationMap.get("Ben xe Nuoc Ngam"), 1300);
        stationMap.get("Ben xe Yen Nghia").addDestination(stationMap.get("Ben xe My Dinh"),640);
        stationMap.get("Ben xe Nuoc Ngam").addDestination(stationMap.get("Ben xe Giap Bat"),170);
        stationMap.get("Ben xe Giap Bat").addDestination(stationMap.get("Ben xe Luong Yen"),590);
        stationMap.get("Ben xe Luong Yen").addDestination(stationMap.get("Ben xe Gia Lam"),590);
        stationMap.get("Ben xe Luong Yen").addDestination(stationMap.get("Ga Ha Noi"),340);
        stationMap.get("Ben xe Kim Ma").addDestination(stationMap.get("Ben xe Gia Lam"),670);
        stationMap.get("Ben xe Kim Ma").addDestination(stationMap.get("Ga Ha Noi"),640);
        stationMap.get("Ben xe Kim Ma").addDestination(stationMap.get("Ben xe My Dinh"),550);
    }

    public boolean hasWay(Station s1, Station s2) {
        for (Bus bus1 : s1.getBus()) {
            for (Bus bus2 : s2.getBus()) {
                if (bus1.equals(bus2)) return true;
            }
        }
        return false;
    }
    public Graph<Station> getGraph() {
        return graph;
    }

    public List<Station> shortestPath(Node<Station> s1, Node<Station> s2) {
        graph.calculateShortestPathFromSource(s1);

        List<Node<Station>> list = s2.getShortestPath();
        List<Station> stations = new ArrayList<>();
        for (Node<Station> node : list) {
            stations.add(node.getData());
        }
        if (!stations.isEmpty()) stations.add(s2.getData());
        return stations;
    }
}
