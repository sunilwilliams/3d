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

        for (int j = 0; j < 6; j++) {
            int a = 1;
            int b = 0;

            if (j == 0) {a = 0; b = 1;}
            if (j == 1) {a = 1; b = 2;}
            if (j == 2) {a = 2; b = 0;}

            for (int x = 0; x < 500; x++) {
                if (points[a][X] < points[b][X]) {
                    double m = ((points[a][Y] - points[b][Y]) / (points[a][X] - points[b][X]));
                    if (x >= points[a][X] && x <= points[b][X]) {
                        g.fillOval(x, (int) (m * (x - points[b][X]) + points[b][Y]), 10, 10);
                    }
                }

                if (points[b][X] < points[a][X]) {
                    double m = ((points[b][Y] - points[a][Y]) / (points[b][X] - points[a][X]));
                    if (x >= points[b][X] && x <= points[a][X]) {
                        g.fillOval(x, (int) (m * (x - points[a][X]) + points[a][Y]), 10, 10);
                    }
                }


            }
        }



        for (int i = 0; i < points.length; i++) {
            g.fillOval((int)points[i][X], (int)points[i][Y], 10, 10);
        }

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
