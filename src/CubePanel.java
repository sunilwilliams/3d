import javax.swing.*;
import java.awt.*;

public class CubePanel extends JPanel {
    int mouseX;
    int mouseY;

    int[][][] points;

    public CubePanel(int[][][] in) {
        points = in;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);

        int rColor = 0;
        int gColor = 0;
        int bColor = 0;

        for (int x = 0; x < points[0].length; x++) {
            for (int y = 0; y < points.length; y++) {
                if (x > 50)
                    g.setColor(Color.BLACK);
                else if (y > 50)
                    g.setColor(Color.BLUE);
                else
                    g.setColor(Color.RED);
                g.fillRect(points[y][x][0] * 1 + 200, points[y][x][1] * 1 + 200, 2, 2);
            }
        }
    }
}
