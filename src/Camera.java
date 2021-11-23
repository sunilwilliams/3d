import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener {
    JFrame frame = new JFrame("cube");
    CameraPanel panel;

    double[][] points = {
            {-100, 50, 50},
            {30, 20, 50},
            {100, 200, 50},
            {500, 100, 50}
    };

    double[][][] tris = {
            {points[0], points[1], points[2]},
            //{points[1], points[2], points[3]}
    };

    double[] cameraPos = {0, 0, 0};

    Color[][] pixelColors = new Color[500][500];

    double[][] pointsLocation = new double[3][3];

    final int X = 0;
    final int Y = 1;
    final int Z = 2;
    final int POS_X = 3;
    final int POS_Y = 4;
    final int POS_Z = 5;

    public static void main(String[] args) {
        new Camera();
    }

    public Camera() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        frame.addKeyListener(this);

        for (int x = 0; x < pixelColors.length; x++) {
            for (int y = 0; y < pixelColors[0].length; y++) {
                pixelColors[x][y] = Color.LIGHT_GRAY;
            }
        }

        panel = new CameraPanel(pixelColors);
        frame.add(panel, BorderLayout.CENTER);
        frame.repaint();

        frame.setVisible(true);
    }

    double[] rotate = new double[3];
    double interval = (Math.PI / 24);

    public void rotate() {
        for (int i = 0; i < points.length; i++) {
            points[i][Z] = points[i][POS_Z] * Math.cos(rotate[Y]) + points[i][POS_X] * -Math.sin(rotate[Y]);
            points[i][X] = points[i][POS_Z] * Math.sin(rotate[Y]) + points[i][POS_X] * Math.cos(rotate[Y]);
            points[i][Y] = points[i][POS_Y];
        }

        System.out.println(points[0][X]);
    }

    double fov = Math.PI;
    double[] rayAngleIntervals = {
            (fov / frame.getWidth()), //angle interval x
            (fov / frame.getHeight()) //angle interval y
    };

    public void sendRays() {
        for (int x = 0; x < frame.getWidth(); x++) {
            for (int y = 0; y < frame.getHeight(); y++) {
                double[] origin = {cameraPos[X] + x, cameraPos[Y] + y, cameraPos[Z]};
                double[] slope = {0, 0, 1};
                pixelColors[y][x] = detectSurface(origin, slope);
            }
        }
    }

    public Color detectSurface(double[] origin, double[] slope) {
        // goes through origin (x1, y1, z1) and m is slope in relation to angle
        // y = m(x - camaraPos[X]) + cameraPos[Y] but in 3d form of course

        boolean hasHit = false;
        int counter = 0;
        Color color = Color.LIGHT_GRAY;
        double[] pos = origin;

        while (!hasHit) {
            for (int i = 0; i < tris.length; i++) {
                if ((int)pos[Z] == 50) {
                    if (isInside((int)pos[X], (int)pos[Y], (int)i)) {
                        color = Color.RED;
                        hasHit = true;
                    }
                }

                //System.out.println(counter);
            }


            //System.out.println(pos[X]);

            pos[X] = pos[X] + slope[X];
            pos[Y] = pos[Y] + slope[Y];
            pos[Z] = pos[Z] + slope[Z];

            counter++;
            if (counter == 100) hasHit = true; // if it checks 100 times, the loop stops
        }
        return color;
    }

    public void checkIfInView() {
        for (int x = 0; x < frame.getWidth(); x++) {
            for (int y = 0; y < frame.getHeight(); y++) {
                pixelColors[x][y] = Color.LIGHT_GRAY;
                if (isInside(cameraPos[X] + x, cameraPos[Y] + y, 0)) {
                    pixelColors[y][x] = Color.RED;
                }
            }
        }
    }

    public void extra(double[] pos, double[] slope) {

        for (int i = 0; i < 3; i++) pos[i] = pos[i] + slope[i];
    }

    public double area(double x1, double y1, double x2, double y2, double x3, double y3) {
        return Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2);
    }

    public boolean isInside(double x, double y, int triNum) {
        double x1 = tris[triNum][0][X];
        double y1 = tris[triNum][0][Y];
        double x2 = tris[triNum][1][X];
        double y2 = tris[triNum][1][Y];
        double x3 = tris[triNum][2][X];
        double y3 = tris[triNum][2][Y];

        double A = area(x1, y1, x2, y2, x3, y3);

        double A1 = area(x, y, x2, y2, x3, y3);

        double A2 = area(x1, y1, x, y, x3, y3);

        double A3 = area(x1, y1, x2, y2, x, y);

        return (A == A1 + A2 + A3);
    }


    double[] cameraAngle = new double[3];

    @Override
    public void keyTyped(KeyEvent e) {

        if (e.getKeyChar() == 'w') {cameraPos[Z]++;}
        if (e.getKeyChar() == 'a') {cameraPos[X]--;}
        if (e.getKeyChar() == 's') {cameraPos[Z]--;}
        if (e.getKeyChar() == 'd') {cameraPos[X]++;}
        for (int i = 0; i < 3; i++) {
            if (cameraAngle[i] > (Math.PI - .01) && cameraAngle[i] < (Math.PI + .01)) {cameraAngle[i] = 0;}
        }

        //checkIfInView();
        System.out.println(cameraPos[X]);
        System.out.println(cameraPos[Z]);
        sendRays();
        frame.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}




