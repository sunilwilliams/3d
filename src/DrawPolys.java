import javax.swing.*;
import java.awt.*;

public class DrawPolys {

    final int X = 0;
    final int Y = 1;
    final int Z = 2;
    final int W = 3;

    final int POINTS = 0;
    final int Z_AVERAGE = 0;
    final int COLOR = 1;
    final int POINT_1 = 2;
    final int POINT_2 = 3;
    final int POINT_3 = 4;
    final int POINT_4 = 5;
    final int POINT_5 = 6;

    final int R = 0;
    final int G = 1;
    final int B = 2;

    double[][] colors = {
            {255, 0, 0},
            {255, 125, 0},
            {255, 255, 0},
            {0, 255, 0},
            {0, 0, 255},
            {255, 0, 255},

    };

    Color[][] screenPixels;

    public DrawPolys(double[][][] screenPolys, int near, Color blank, Color[][] pixels) {
        screenPixels = pixels;

        for (int x = 0; x < screenPixels.length; x++) {
            for (int y = 0; y < screenPixels[0].length; y++) {
                screenPixels[x][y] = blank;
            }
        }

        for (int i = near; i < 10000; i++) {
            for (int j = 0; j < screenPolys.length; j++) {
                double[][] poly = screenPolys[j];

                if ((int)poly[Z_AVERAGE][0] == i) {
                    for (int x = 0; x < screenPixels.length; x++) {
                        for (int y = 0; y < screenPixels[0].length; y++) {
                            if (screenPixels[x][y] == blank && isInside(x, y, poly[POINT_1][X], poly[POINT_1][Y], poly[POINT_2][X], poly[POINT_2][Y], poly[POINT_3][X], poly[POINT_3][Y])) {
                                //System.out.println(poly[POINT_1][Z]);
                                //screenPixels[x][y] = new Color(colors[poly[COLOR][0]][R], colors[poly[COLOR][0]][G], colors[poly[COLOR][0]][B]);
                                double brightness = 255 / ((double)i / 100) - 255;
                                if (i > 500)
                                    brightness = brightness - (i - 500);

                                int[] color = {(int)(brightness + colors[(int)poly[COLOR][0]][R]), (int)(brightness + colors[(int)poly[COLOR][0]][G]), (int)(brightness + colors[(int)poly[COLOR][0]][B])};

                                for (int k = 0; k < color.length; k++) {
                                    if (color[k] > 255) {
                                        color[k] = 255;
                                    }
                                    if (color[k] < 0) {
                                        color[k] = 0;
                                    }
                                }

                                screenPixels[x][y] = new Color(color[R], color[G], color[B]);
                            }
                        }
                    }
                }
            }
        }
    }

    public double area(double x1, double y1, double x2, double y2, double x3, double y3) {
        return Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2);
    }

    public boolean isInside(double x, double y, double x1, double y1, double x2, double y2, double x3, double y3) {
        double A = area(x1, y1, x2, y2, x3, y3);

        double A1 = area(x, y, x2, y2, x3, y3);

        double A2 = area(x1, y1, x, y, x3, y3);

        double A3 = area(x1, y1, x2, y2, x, y);

        return (A == A1 + A2 + A3);
    }

    //public double[] calculateVector(double[][] tri) {


    //(𝑎2𝑏3−𝑎3𝑏2)𝑖−(𝑎1𝑏3−𝑎3𝑏1)𝑗+(𝑎1𝑏2−𝑎2𝑏1)𝑘
    //}


    public Color[][] getScreenPixels() {
        return screenPixels;
    }


}
