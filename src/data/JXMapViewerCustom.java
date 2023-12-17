package data;

import busStation.Bus;
import com.graphhopper.util.shapes.GHPoint3D;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class JXMapViewerCustom extends JXMapViewer {

    public List<RoutingData> getRoutingData() {
        return routingData;
    }

    public void setRoutingData(List<RoutingData> routingData, Bus bus) {
//        this.routingData = routingData;
        if (!routingDataList.contains(routingData)) routingDataList.add(routingData);
        repaint();
        if (buses.contains(bus)) colors.add(color);
        else {
            buses.add(bus);
            color=new Color((color1 +=40) % 160, (color2 += 50) % 120, (color3 += 40) % 100);
            colors.add(color);
        }
    }

    int color1 = 64;
    int color2 = 112;
    int color3 = 152;
    private Color color = new Color(color1, color2, color3);
//    private Bus bus;
//    private Map<Bus, Integer> busColorMap = new HashMap<>();
    private List<RoutingData> routingData;
    //    private Map<Bus,List<RoutingData>> routingDataMap =new HashMap<>();
    private List<List<RoutingData>> routingDataList = new ArrayList<>();
    private List<Color> colors = new ArrayList<>();
    private List<Bus> buses = new ArrayList<>();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i=0;i<routingDataList.size();i++) {
            List<RoutingData> list = routingDataList.get(i);
            if (list != null && !list.isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Path2D p2 = new Path2D.Double();
                first = true;
                for (RoutingData d : list) {
                    draw(p2, d);
                }
                g2.setColor(colors.get(i));
//                g2.setColor(new Color(64, 112, 152));
                g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(p2);
                g2.dispose();
            }
        }
    }

    public void clear() {
        routingDataList.clear();
    }
    private boolean first = true;

    private void draw(Path2D p2, RoutingData d) {
        d.getPointList().forEach(new Consumer<GHPoint3D>() {
            @Override
            public void accept(GHPoint3D t) {
                Point2D point = convertGeoPositionToPoint(new GeoPosition(t.getLat(), t.getLon()));
                if (first) {
                    first = false;
                    p2.moveTo(point.getX(), point.getY());
                } else {
                    p2.lineTo(point.getX(), point.getY());
                }
            }
        });
    }
}
