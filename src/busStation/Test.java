package busStation;

import graph.Node;

public class Test {
    public static void main(String[] args) {
        StationManage stationManage = new StationManage();
        Journey journey = new Journey(stationManage.stationMap.get("Ben xe Nuoc Ngam"),stationManage.stationMap.get("Ben xe Gia Lam"));
//        for (Station i : journey.getPath()) {
//            System.out.println(i.address);
//        }
//
//        for (Bus bus : journey.getJourneys().keySet()) {
//            System.out.println(bus.id);
//            for (Station station : journey.getJourneys().get(bus)) {
//                System.out.println(station.address);
//            }
//            System.out.println();
//        }
//
//        for (Bus bus : journey.getOptimalJourney().keySet()) {
//            System.out.println(bus.id);
//            for (Station station : journey.getOptimalJourney().get(bus)) {
//                System.out.println(station.address);
//            }
//        }
        for (Node<Station> i : stationManage.stationMap.get("Ben xe Gia Lam").getAdjacentNodes().keySet()){
            System.out.println(stationManage.stationMap.get("Ben xe Gia Lam").getAdjacentNodes().get(i));
        };
    }
}
