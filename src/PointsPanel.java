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


        int rColor = 0;
        int gColor = 0;
        int bColor = 0;

            for (int y = 0; y < points.length; y++) {
                int colorCounter = (int)(y / 5);

                if (colorCounter <= 255)
                    rColor = colorCounter;
                if (colorCounter > 255 && colorCounter <= 510)
                    gColor = (colorCounter - 255);
                if (colorCounter > 510 && colorCounter <= 765)
                    bColor = (colorCounter - 510);


                g.setColor(new Color(rColor, gColor, bColor, 50));

                g.fillOval(points[y][0] + 250, points[y][1] + 250, 10, 10);
            }
    }
}
