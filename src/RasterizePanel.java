import javax.swing.*;
import java.awt.*;

public class RasterizePanel extends JPanel {
    int[][] points;

    final int X = 0;
    final int Y = 1;
    final int Z = 2;

    int frameWidth = 500;
    int frameHeight = 500;

    int[][][] screenTris;

    Color[] colors = {
            Color.RED,
            Color.YELLOW,
            Color.GREEN,
            Color.BLUE
    };

    public RasterizePanel(int[][] in) {
        points = in;

    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);


        for (int i = 0; i < points.length; i++) {
            g.fillOval(points[i][X], points[i][Y], 10, 10);
        }


        int[] xValues = {points[0][X], points[1][X], points[2][X]};
        int[] yValues = {points[0][Y], points[1][Y], points[2][Y]};
        //g.fillPolygon(xValues, yValues, 3);

        int[] xValues1 = {points[3][X], points[4][X], points[5][X]};
        int[] yValues1 = {points[3][Y], points[4][Y], points[5][Y]};
        //g.fillPolygon(xValues1, yValues1, 3);

        //for (int i = 0; i < polys.length; i++) {
            //for (int j = 0; j < (polys.length - 1); j++) {
                //xValues[j] = polys[i][j][X];
                //yValues[j] = polys[i][j][Y];
            //}
            //g.setColor(colors[(int)polys[i][polys[i].length - 1][0]]);
            //g.fillPolygon(xValues, yValues, polys[i].length - 1);
        //}

    }
}
