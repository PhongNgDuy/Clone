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


    public Map<Bus,LinkedList<Station>> getOptimalJourney() {
        Map<Bus,LinkedList<Station>> optimalOption=new HashMap<>();
        for (Bus bus1 : getJourneys().keySet()) {
            optimalOption.put(bus1,getJourneys().get(bus1));
            Bus tmpBus=bus1;
            for (Bus bus2 : getJourneys().keySet()) {
                if (getJourneys().get(bus2).containsAll(optimalOption.get(tmpBus))&&!bus1.equals(bus2)) {
                    optimalOption.remove(tmpBus);
                    getJourneys().remove(tmpBus);
                    optimalOption.put(bus2,getJourneys().get(bus2));
                    tmpBus=bus2;
                }
            }
        }
        return optimalOption;
    }
}
