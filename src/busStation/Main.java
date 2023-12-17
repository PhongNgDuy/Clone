package busStation;

import com.graphhopper.util.shapes.GHPoint3D;
import data.JXMapViewerCustom;
import data.RoutingData;
import data.RoutingService;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointPainter;
import waypoint.EventWaypoint;
import waypoint.MyWaypoint;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import waypoint.WaypointRender;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

public class Main extends JFrame {
    private EventWaypoint event;
    static JButton cmdAdd = new JButton();
    private JComboBox<String> comboMapType;
    private JXMapViewerCustom jXMapViewer;
    private final Set<MyWaypoint> waypoints = new HashSet<>();
    private final Set<MyWaypoint> waypointsClone = new HashSet<>();
    private final Set<MyWaypoint> busWaypoints = new HashSet<>();
    private List<RoutingData> routingData = new ArrayList<>();
    private int busCheck = 0;
    private Bus bus;
    JLabel label = new JLabel("Gia tien");
    private int total = 0;

    private String start;
    private String end;

    JComboBox<String> selectStartStation ;
    JComboBox<String> selectEndStation ;

    private void init() {
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        jXMapViewer.setTileFactory(tileFactory);
        GeoPosition geo = new GeoPosition(21.027763, 105.834160);
        jXMapViewer.setAddressLocation(geo);
        jXMapViewer.setZoom(8);

        //  Create event mouse move
        MouseInputListener mm = new PanMouseInputListener(jXMapViewer);
        jXMapViewer.addMouseListener(mm);
        jXMapViewer.addMouseMotionListener(mm);
        jXMapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(jXMapViewer));
        event = getEvent();
    }

    private void initPoints() {
        WaypointPainter<MyWaypoint> wp = new WaypointRender();
        wp.setWaypoints(waypointsClone);
        jXMapViewer.setOverlayPainter(wp);
        for (MyWaypoint d : waypointsClone) {
            jXMapViewer.add(d.getButton());
        }

    }

    private int size = 0;

    private void initWaypoint() {
        if (/*waypoints.size()*/size == 2) {
            GeoPosition start = null;
            GeoPosition end = null;
            for (MyWaypoint w : waypoints) {
                if (w.getPointType() == MyWaypoint.PointType.START) {
                    start = w.getPosition();
                } else if (w.getPointType() == MyWaypoint.PointType.END) {
                    end = w.getPosition();
                }
            }
            if (start != null && end != null) {
                routingData = RoutingService.getInstance().routing(start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude());
                size = 0;
                if (busCheck == 1) {
                    GHPoint3D busPoint = routingData.get(routingData.size() / 3).getPointList().get(routingData.get(routingData.size() / 3).getPointList().size() / 2);
                    waypointsClone.add(new MyWaypoint(bus, event, new GeoPosition(busPoint.lat, busPoint.lon)));
                }
            } else {
                routingData.clear();
            }
            jXMapViewer.setRoutingData(routingData, bus);
        }
    }

    private void addWaypoint(MyWaypoint waypoint) {
//        for (MyWaypoint d : waypoints) {
//            jXMapViewer.remove(d.getButton());
//        }
        Iterator<MyWaypoint> iter = waypoints.iterator();
        while (iter.hasNext()) {
            if (iter.next().getPointType() == waypoint.getPointType()) {
                iter.remove();
            }
        }

        waypoints.add(waypoint);
//        waypointsClone.add(waypoint);
        initWaypoint();
    }

    private void clearWaypoint() {
        for (MyWaypoint d : waypointsClone) {
            jXMapViewer.remove(d.getButton());
        }
        routingData.clear();
        waypointsClone.clear();
        jXMapViewer.clear();
        initWaypoint();
        initPoints();
    }

    private EventWaypoint getEvent() {
        return new EventWaypoint() {
            @Override
            public void selected(MyWaypoint waypoint) {
                JOptionPane.showMessageDialog(Main.this, waypoint.getName());
            }
        };
    }

    private void initComponents() {

        jXMapViewer = new JXMapViewerCustom();
        comboMapType = new JComboBox<>();
        cmdAdd = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        StationManage stationManage = new StationManage();
        List<String> stations = new ArrayList<>(stationManage.stationMap.keySet());
        String[] startStation = new String[stations.size()+1];
        String[] endStation = new String[stations.size()+1];
        startStation[0]="Start";
        endStation[0]="End";
        for (int i = 0; i < stations.size(); i++) {
            startStation[i+1]=stations.get(i);
            endStation[i+1]=stations.get(i);
        }
        selectStartStation=new JComboBox<>(startStation);
        selectEndStation=new JComboBox<>(endStation);

        selectStartStation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectStartStation(e);
            }
        });

        selectEndStation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                selectEndStation(ae);
            }
        });

        comboMapType.setModel(new DefaultComboBoxModel<>(new String[]{"Open Stree", "Virtual Earth", "Hybrid", "Satellite"}));
        comboMapType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                comboMapTypeActionPerformed(evt);
            }
        });
        JPanel jPanel = new JPanel();
        jPanel.add(label);
        GroupLayout jXMapViewerLayout = new GroupLayout(jXMapViewer);
        jXMapViewer.setLayout(jXMapViewerLayout);
        jXMapViewerLayout.setHorizontalGroup(
                jXMapViewerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jXMapViewerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(cmdAdd)
                                .addComponent(jPanel, 120, 140, 200)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 767, Short.MAX_VALUE)
                                .addComponent(comboMapType, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
                                .addComponent(selectStartStation, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
                                .addComponent(selectEndStation, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())

        );
        jXMapViewerLayout.setVerticalGroup(
                jXMapViewerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jXMapViewerLayout.createSequentialGroup()
                                .addContainerGap()

                                .addGroup(jXMapViewerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(comboMapType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(selectStartStation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(selectEndStation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmdAdd))
                                .addComponent(jPanel, 20, 40, 40)
                                .addContainerGap(632, Short.MAX_VALUE))

        );
        GroupLayout layout = new GroupLayout(getContentPane());

        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(jXMapViewer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(jXMapViewer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0))
        );

        cmdAdd.setText("Add Waypoint");
        cmdAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cmdClearActionPerformed(evt);
                cmdAddActionPerformed(evt);
            }
        });
        pack();
        setLocationRelativeTo(null);
    }

    private void comboMapTypeActionPerformed(ActionEvent evt) {//GEN-FIRST:event_comboMapTypeActionPerformed
        TileFactoryInfo info;
        int index = comboMapType.getSelectedIndex();
        System.out.println(index);
        if (index == 0) {
            info = new OSMTileFactoryInfo();
        } else if (index == 1) {
            info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
        } else if (index == 2) {
            info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.HYBRID);
        } else {
            info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.SATELLITE);
        }
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        jXMapViewer.setTileFactory(tileFactory);
    }

    private void selectStartStation(ActionEvent event) {
        int index=selectStartStation.getSelectedIndex();
        StationManage stationManage = new StationManage();
        List<String> stations = new ArrayList<>(stationManage.stationMap.keySet());
        for (int i = 0; i < stations.size(); i++) {
            if(index==i+1) start=stations.get(i);
        }
    }

    private void selectEndStation(ActionEvent event) {
        int index=selectEndStation.getSelectedIndex();
        StationManage stationManage = new StationManage();
        List<String> stations = new ArrayList<>(stationManage.stationMap.keySet());
        for (int i = 0; i < stations.size(); i++) {
            if(index==i+1) {
                end=stations.get(i);
            }
        }
    }

    private void cmdAddActionPerformed(ActionEvent evt) {
        routingData.clear();
        StationManage stationManage = new StationManage();
        Journey journey = new Journey(stationManage.stationMap.get(start), stationManage.stationMap.get(end));
        for (Station station : journey.path) {
            waypointsClone.add(new MyWaypoint(station.address, event, station.location));
        }
        for (Bus bus : journey.getOptimalJourney().keySet()) {
            busCheck = 1;
            this.bus = bus;
            LinkedList<Station> stations = journey.getOptimalJourney().get(bus);
            total += bus.price;
            System.out.println(bus.id);
            for (Station i : stations) {
                System.out.println(i.address);
            }
            for (int i = 0; i < stations.size() - 1; i++) {
                addWaypoint(new MyWaypoint(stations.get(i).address, MyWaypoint.PointType.START, event, stations.get(i).location));
                size = 2;
                addWaypoint(new MyWaypoint(stations.get(i).address, MyWaypoint.PointType.END, event, stations.get(i + 1).location));
                busCheck = 0;
            }
//            size=0;
//            addWaypoint(new MyWaypoint(stations.get(0).address,MyWaypoint.PointType.START, event, stations.get(0).location ));
//            size=2;
//            addWaypoint(new MyWaypoint(stations.get(stations.size()-1).address,MyWaypoint.PointType.END, event, stations.get(stations.size()-1).location ));

        }
        label.setText("Thanh tien: " + String.format("%d.000 vnd", total));
        total=0;
        initPoints();
    }
    private void cmdClearActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cmdClearActionPerformed
        clearWaypoint();
    }

    public Main() {
        initComponents();
        init();
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.setVisible(true);
    }
}
