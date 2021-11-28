import javax.swing.*;
import java.awt.*;

public class RasterizePanel extends JPanel {
    int[][] points;

    final int X = 0;
    final int Y = 1;
    final int Z = 2;

    final int POINTS = 0;
    final int COLOR = 1;
    final int POINT_1 = 2;
    final int POINT_2 = 3;
    final int POINT_3 = 4;
    final int POINT_4 = 5;
    final int POINT_5 = 6;

    int frameWidth = 500;
    int frameHeight = 500;

    int[][][] screenPolys;

    int opacity = 50;

    Color[] colors = {
            new Color(255, 255, 255, opacity),
            new Color(255, 125, 0, opacity),
            new Color(255, 255, 0, opacity),
            new Color(0, 255, 0, opacity),
            new Color(0, 0, 255, opacity),
            new Color(255, 0, 255, opacity),
    };

    public RasterizePanel(int[][][] in) {
        screenPolys = in;

    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);

        for (int i = 0; i < screenPolys.length; i++) {

        }


        for (int i = 0; i < screenPolys.length; i++) {
            int[] xValues = new int[100];
            int[] yValues = new int[100];
            for (int j = 0; j < screenPolys[i][POINTS][0]; j++) {
                //g.fillOval(screenPolys[i][j + 2][X], screenPolys[i][j + 2][Y], 10, 10);


                xValues[j] = screenPolys[i][j + 2][X];
                yValues[j] = frameHeight - screenPolys[i][j + 2][Y];
            }
            g.setColor(colors[screenPolys[i][COLOR][0]]);
            g.fillPolygon(xValues, yValues, screenPolys[i][POINTS][0]);
        }



    }
}
