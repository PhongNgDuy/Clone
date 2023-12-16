package src.busStation;

import data.JXMapViewerCustom;
import data.RoutingData;
import data.RoutingService;
import org.jxmapviewer.viewer.WaypointPainter;
import waypoint.EventWaypoint;
import waypoint.MyWaypoint;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

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
    private data.JXMapViewerCustom jXMapViewer;
    private final Set<MyWaypoint> waypoints = new HashSet<>();
    private List<RoutingData> routingData = new ArrayList<>();

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
        wp.setWaypoints(waypoints);
        jXMapViewer.setOverlayPainter(wp);
        for (MyWaypoint d : waypoints) {
            jXMapViewer.add(d.getButton());
        }
    }
    private int size=0;
    private void initWaypoint() {
//        WaypointPainter<MyWaypoint> wp = new WaypointRender();
//        wp.setWaypoints(waypoints);
//        jXMapViewer.setOverlayPainter(wp);
//        for (MyWaypoint d : waypoints) {
//            jXMapViewer.add(d.getButton());
//        }

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
                System.out.println(waypoints.size());
                routingData = RoutingService.getInstance().routing(start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude());
//                waypoints.clear();
                size=0;
            }
            else {
                routingData.clear();
            }

            jXMapViewer.setRoutingData(routingData);
        }
    }
    private void addWaypoint(MyWaypoint waypoint) {
//        for (MyWaypoint d : waypoints) {
//            jXMapViewer.remove(d.getButton());
//        }
//        Iterator<MyWaypoint> iter = waypoints.iterator();
//        while (iter.hasNext()) {
//            if (iter.next().getPointType() == waypoint.getPointType()) {
//                iter.remove();
//            }
//        }

        waypoints.add(waypoint);
        initWaypoint();
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

        comboMapType.setModel(new DefaultComboBoxModel<>(new String[]{"Open Stree", "Virtual Earth", "Hybrid", "Satellite"}));
        comboMapType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                comboMapTypeActionPerformed(evt);
            }
        });

        cmdAdd.setText("Add Waypoint");
        cmdAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });

        GroupLayout jXMapViewerLayout = new GroupLayout(jXMapViewer);
        jXMapViewer.setLayout(jXMapViewerLayout);
        jXMapViewerLayout.setHorizontalGroup(
                jXMapViewerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jXMapViewerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(cmdAdd)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 767, Short.MAX_VALUE)
                                .addComponent(comboMapType, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        jXMapViewerLayout.setVerticalGroup(
                jXMapViewerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jXMapViewerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jXMapViewerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(comboMapType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmdAdd))
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

        pack();
        setLocationRelativeTo(null);
    }

    private void comboMapTypeActionPerformed(ActionEvent evt) {//GEN-FIRST:event_comboMapTypeActionPerformed
        TileFactoryInfo info;
        int index = comboMapType.getSelectedIndex();
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

    private void cmdAddActionPerformed(ActionEvent evt) {
        StationManage stationManage = new StationManage();
        Journey journey = new Journey(stationManage.stationMap.get("Ben xe Gia Lam"),stationManage.stationMap.get("Ben xe Yen Nghia"));

        for (Bus bus : journey.getOptimalJourney().get(0).keySet()) {
            LinkedList<Station> stations = journey.getOptimalJourney().get(0).get(bus);
            System.out.println(bus.id);
            for (Station i : stations) {
                System.out.println(i.address);
            }
            addWaypoint(new MyWaypoint(stations.get(0).address,MyWaypoint.PointType.START, event, stations.get(0).location ));
            size=2;
            addWaypoint(new MyWaypoint(stations.get(stations.size()-1).address,MyWaypoint.PointType.END, event, stations.get(stations.size()-1).location ));
        }
        initPoints();
//        addWaypoint(new MyWaypoint(journey.getPath().get(0).address,MyWaypoint.PointType.START, event, journey.getPath().get(0).location ));
//        addWaypoint(new MyWaypoint(journey.getPath().get(1).address,MyWaypoint.PointType.END, event, journey.getPath().get(1).location ));
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
