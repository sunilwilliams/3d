import javax.swing.*;
import java.awt.*;

public class FillPanel extends JPanel{
    double[][] points;

    final int X = 0;
    final int Y = 1;
    final int Z = 2;

    public FillPanel(double[][] in) {
        points = in;

    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        for (int x = 0; x < 500; x++) {
            for (int y = 0; y < 500; y++) {
                //if (isInside(x, y))
                    //g.fillRect(x + 250, y + 250, 5, 5);
            }
        }

        int[][] values = new int[2][3];

        for (int a = 0; a < 2; a++) {
            for (int b = 0; b < 3; b++) {
                values[a][b] = (int)points[a][b];
            }
        }


        int[] xValues = {(int)points[0][X] + 250, (int)points[1][X] + 250, (int)points[2][X] + 250};
        int[] yValues = {(int)points[0][Y] + 250, (int)points[1][Y] + 250, (int)points[2][Y] + 250};

        g.fillPolygon(xValues, yValues, 3);



    }

    public double area(double x1, double y1, double x2, double y2, double x3, double y3) {
        return Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2);
    }

    public boolean isInside(double x, double y) {
        double A = area(points[0][X], points[0][Y], points[1][X], points[1][Y], points[2][X], points[2][Y]);

        double A1 = area(x, y, points[1][X], points[1][Y], points[2][X], points[2][Y]);

        double A2 = area(points[0][X], points[0][Y], x, y, points[2][X], points[2][Y]);

        double A3 = area(points[0][X], points[0][Y], points[1][X], points[1][Y], x, y);

        return (A == A1 + A2 + A3);
    }

    public void extra(Graphics g) {
        if (points[1][X] < points[0][X]) {
            double m = ((points[0][Y] - points[1][Y]) / (points[0][X] - points[1][X]));

            for (int x = 0; x < 500; x++) {
                if (x >= points[1][X] && x <= points[0][X]) {
                    g.fillRect(x, (int)(m * (x - points[1][X]) + points[1][Y]), 10, 10);
                }
            }
        }
    }


}
