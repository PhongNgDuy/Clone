package data;

import com.graphhopper.util.shapes.GHPoint3D;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JXMapViewerCustom extends JXMapViewer {

    public List<RoutingData> getRoutingData() {
        return routingData;
    }

    public void setRoutingData(List<RoutingData> routingData) {
        this.routingData = routingData;
        if (!routingDataList.contains(routingData)) routingDataList.add(routingData);
        repaint();
    }

    private List<RoutingData> routingData;
    private List<List<RoutingData>> routingDataList=new ArrayList<>();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int color1=34;
        int color2=57;
        for (List<RoutingData> list : routingDataList) {
            if (list != null && !list.isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Path2D p2 = new Path2D.Double();
                first = true;
                for (RoutingData d : list) {
                    draw(p2, d);
                }
                g2.setColor(new Color((color1+=110)%162,(color2+=50)%162 , 162));
//                g2.setColor(new Color(57, 162, 66));
                g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(p2);
                g2.dispose();
            }
        }
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
