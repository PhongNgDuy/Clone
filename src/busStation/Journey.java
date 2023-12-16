package busStation;

import graph.Node;

import java.util.*;

public class Journey extends StationManage {
    Node<Station> start;
    Node<Station> end;
    List<Station> path;
    double totalPrice;

    public Journey(Node<Station> start, Node<Station> end) {
        this.start = start;
        this.end = end;
        path = shortestPath(start, end);
    }

    public List<Station> getPath() {
        return path;
    }


    public Map<Bus, LinkedList<Station>> getJourneys() {
        Map<Bus, LinkedList<Station>> journey = new HashMap<>();
        for (Station station : path) {
            for (Bus bus : station.getBus()) {
                if (!journey.containsKey(bus)) journey.put(bus, new LinkedList<>());
                journey.get(bus).add(station);
            }
        }
        return journey;
    }

    public LinkedList<Map<Bus, LinkedList<Station>>> getOptimalJourney() {
        LinkedList<Map<Bus, LinkedList<Station>>> optimalJourney = new LinkedList<>();
        int size = path.size();
        while (size > 0) {
            for (Bus i : getJourneys().keySet()) {
                LinkedList<Station> tmp = getJourneys().get(i);
                if (tmp.get(0).equals(path.get(0)) && tmp.get(tmp.size() - 1).equals(path.get(size - 1))) {
                    Map<Bus, LinkedList<Station>> map = new HashMap<>();
                    map.put(i, tmp);

                    if (size < path.size()) {
                        for (Bus j : getJourneys().keySet()) {
                            LinkedList<Station> tmp2 = getJourneys().get(j);
                            if (tmp2.get(tmp2.size() - 1).equals(path.get(path.size() - 1)) && tmp2.size() >= path.size()-size) {
                                LinkedList<Station> stations = new LinkedList<>();
                                stations.add(path.get(size - 1));
                                stations.add(path.get(path.size() - 1));
                                map.put(j, stations);
                            }
                        }
                    }

                    optimalJourney.add(map);
                }
            }
            size--;
        }
        return optimalJourney;
    }
}
