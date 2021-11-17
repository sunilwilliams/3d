import javax.swing.*;
import java.awt.*;

public class PointsPanel extends JPanel {
    int mouseX;
    int mouseY;

    int[][] points;

    public PointsPanel(int[][] in) {
        points = in;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);

        int rColor = 0;
        int gColor = 0;
        int bColor = 0;

            for (int y = 0; y < points.length; y++) {

                g.fillRect(points[y][0] * 1 + 200, points[y][1] * 1 + 200, 10, 10);
            }
    }
}
