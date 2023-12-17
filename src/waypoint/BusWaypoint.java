package waypoint;

import javax.swing.*;
import java.awt.*;

public class BusWaypoint extends JButton {
    public BusWaypoint() {
        setContentAreaFilled(false);
        setIcon(new ImageIcon(getClass().getResource("/icon/bus2.png")));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setSize(new Dimension(24, 24));
    }
}
