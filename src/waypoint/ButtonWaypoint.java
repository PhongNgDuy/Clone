package waypoint;

import javax.swing.*;
import java.awt.*;

public class ButtonWaypoint extends JButton {

    public ButtonWaypoint() {
        setContentAreaFilled(false);
        setIcon(new ImageIcon(getClass().getResource("/icon/station4.png")));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setSize(new Dimension(30, 30));
    }
}
