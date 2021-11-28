import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class RasterizeTest implements KeyListener, MouseMotionListener, Runnable {
    JFrame frame = new JFrame("cube");
    RasterizePanel panel;

    double[][] points = {
            {-50, -50, -50},
            {-50, 50, -50},
            {50, 50, -50},
            {50, -50, -50},
            {-50, -50, 50},
            {-50, 50, 50},
            {50, 50, 50},
            {50, -50, 50},
    };

    int[][] polys = {
            {3, 0, 0, 1, 2},
            {3, 1, 4, 5, 6},
            {3, 2, 0, 1, 4},
            {3, 3, 1, 2, 5},
            {3, 4, 2, 3, 6},
            {3, 5, 3, 0, 7},

            {3, 0, 2, 3, 0},
            {3, 1, 7, 4, 6},
            {3, 2, 5, 1, 4},
            {3, 3, 6, 2, 5},
            {3, 4, 7, 3, 6},
            {3, 5, 4, 0, 7},
    };

    int[][][] screenPolys = new int[20][10][3];

    Color[] colors = {
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.GREEN,
            Color.BLUE,
            Color.MAGENTA,
    };

    double[] cameraPos = {0, 0, 0};

    double[] cameraAngle = {0, 0, (Math.PI / 2)};

    int[][] finalPointCoords = new int[100][3];

    final int X = 0;
    final int Y = 1;
    final int Z = 2;
    final int W = 3;

    final int POINTS = 0;
    final int COLOR = 1;
    final int POINT_1 = 2;
    final int POINT_2 = 3;
    final int POINT_3 = 4;
    final int POINT_4 = 5;
    final int POINT_5 = 6;

    boolean running = false;

    public static void main(String[] args) {
        new RasterizeTest();
    }

    public RasterizeTest() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        frame.addKeyListener(this);

        panel = new RasterizePanel(screenPolys);
        frame.add(panel, BorderLayout.CENTER);
        frame.repaint();

        if (!running) {
            running = true;
            Thread t = new Thread(this);
            t.start();
        }

        frame.setVisible(true);
    }

    double interval = (2 * Math.PI / 200);
    double xFov = (Math.PI / 3);
    double yFov = (Math.PI / 3);
    double near = 10;
    double far = 100;
    int xPixelsInFrame = 5;
    int yPixelsInFrame = 5;

    public void setPoints() {
        for (int i = 0; i < points.length; i++) {
            double[] values = setPoint(points[i][X], points[i][Y], points[i][Z]);

            finalPointCoords[i][X] = (int)values[X];
            finalPointCoords[i][Y] = (int)values[Y];
            finalPointCoords[i][Z] = (int)values[Z];

        }
    }

    public double[] CheckOutI(double[][] values) {
        int leftBoundary = -100;
        int rightBoundary = 100;
        int upBoundary = -100;
        int downBoundary = 100;

        double[] output = new double[3];

        for (int i = 0; i < values.length; i++) {
            int inside1;
            int inside2;
            if (values[i][X] > rightBoundary) {
                if (i == 0) {inside1 = 1; inside2 = 2;}
                if (i == 1) {inside1 = 2; inside2 = 0;}
                if (i == 2) {inside1 = 0; inside2 = 1;}
            }


        }


        return output;
    }

    public double[] setPoint(double x, double y, double z) {
        x = x - cameraPos[X];
        y = y - cameraPos[Y];
        z = z - cameraPos[Z];

        double[] d = new double[3];

        double angleX = cameraAngle[X];
        double angleY = cameraAngle[Y];
        double angleZ = cameraAngle[Z];

        double cosX = Math.cos(angleX);
        double cosY = Math.cos(angleY);
        double cosZ = Math.cos(angleZ);

        double sinX = Math.sin(angleX);
        double sinY = Math.sin(angleY);
        double sinZ = Math.sin(angleZ);


        d[X] = cosY * ((sinZ * y) + (cosZ * x)) - (sinY * z);
        d[Y] = sinX * ((cosY * z) + (sinY * (sinZ * y + cosZ * x))) + (cosX * (cosZ * y - sinZ * x));
        d[Z] = cosX * ((cosY * z) + (sinY * (sinZ * y + cosZ * x))) - (sinX * (cosZ * y - sinZ * x));

        x = d[X];
        y = d[Y];
        z = d[Z];

        //z = (z * Math.cos(angleY) - (x * Math.sin(angleY)));
        //x = (z * Math.sin(angleY) + (x * Math.cos(angleY)));


        //y = (y * Math.cos(angleX) - (z * Math.sin(angleX)));
        //z = (y * Math.sin(angleX) + (z * Math.cos(angleX)));

        x = x * (frame.getWidth() / xPixelsInFrame);
        y = y * (frame.getHeight() / yPixelsInFrame);

        double w = (-z / 2);
        x = (x / Math.tan(xFov / 2));
        y = (y / Math.tan(yFov / 2));
        z = ((z * ((far + near) / (far - near))) + ((2 * far * near) / (far - near)));
        //double w = -z;

        x = (x / w);
        y = (y / w);
        //z = (z / w);

        double[] output = new double[3];
        output[X] = (int) x + (frame.getWidth() / 2);
        output[Y] = (int) y + (frame.getHeight() / 2);
        output[Z] = (int) z;

        return output;
    }

    public void setPolys() {
        int polyCounter = 0;

        for (int i = 0; i < polys.length; i++) { //set each poly
            int[] pointsNum = {polys[i][POINTS]};
            screenPolys[polyCounter][POINTS] = pointsNum; //set points on screenPolys
            int[] colorNum = {polys[i][COLOR]};
            screenPolys[polyCounter][COLOR] = colorNum; //set color on screenPolys
            for (int j = 2; j < (polys[i][POINTS] + 2); j++) { //for polys points num and after points and colors
                double[] values = setPoint(points[polys[i][j]][X], points[polys[i][j]][Y], points[polys[i][j]][Z]);
                screenPolys[polyCounter][j][X] = (int) values[X];
                screenPolys[polyCounter][j][Y] = (int) values[Y];
                screenPolys[polyCounter][j][Z] = (int) values[Z];
            }
            polyCounter++;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'w') forward = true;
        if (e.getKeyChar() == 's') backward = true;

        if (e.getKeyChar() == 'q') down = true;
        if (e.getKeyChar() == 'e') up = true;

        if (e.getKeyChar() == 'a') left = true;
        if (e.getKeyChar() == 'd') right = true;

        if (e.getKeyCode() == KeyEvent.VK_LEFT) rotateLeft = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rotateRight = true;

        if (e.getKeyCode() == KeyEvent.VK_UP) rotateUp = true;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) rotateDown = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'w') forward = false;
        if (e.getKeyChar() == 's') backward = false;

        if (e.getKeyChar() == 'q') down = false;
        if (e.getKeyChar() == 'e') up = false;

        if (e.getKeyChar() == 'a') left = false;
        if (e.getKeyChar() == 'd') right = false;

        if (e.getKeyCode() == KeyEvent.VK_LEFT) rotateLeft = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rotateRight = false;

        if (e.getKeyCode() == KeyEvent.VK_UP) rotateUp = false;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) rotateDown = false;
    }

    boolean forward;
    boolean backward;
    boolean down;
    boolean up;
    boolean left;
    boolean right;
    boolean rotateLeft;
    boolean rotateRight;
    boolean rotateUp;
    boolean rotateDown;

    public void input() {
        double move = 5;

        if (forward) {
            cameraPos[X] = cameraPos[X] + move * Math.cos(cameraAngle[Y] - (Math.PI / 2));
            cameraPos[Z] = cameraPos[Z] + move * Math.sin(cameraAngle[Y] - (Math.PI / 2));
        }
        if (backward) {
            cameraPos[X] = cameraPos[X] - move * Math.cos(cameraAngle[Y] - (Math.PI / 2));
            cameraPos[Z] = cameraPos[Z] - move * Math.sin(cameraAngle[Y] - (Math.PI / 2));
        }
        if (down) {
            cameraPos[Y] = cameraPos[Y] - move;
        }
        if (up) {
            cameraPos[Y] = cameraPos[Y] + move;
        }
        if (left) {
            cameraPos[X] = cameraPos[X] - move * Math.cos(cameraAngle[Y]);
            cameraPos[Z] = cameraPos[Z] - move * Math.sin(cameraAngle[Y]);
        }
        if (right) {
            cameraPos[X] = cameraPos[X] + move * Math.cos(cameraAngle[Y]);
            cameraPos[Z] = cameraPos[Z] + move * Math.sin(cameraAngle[Y]);
        }
        if (rotateLeft) {
            cameraAngle[Y] = cameraAngle[Y] + interval;
        }
        if (rotateRight) {
            cameraAngle[Y] = cameraAngle[Y] - interval;
        }
        if (rotateUp) {
            cameraAngle[X] = cameraAngle[X] + interval;
        }
        if (rotateDown) {
            cameraAngle[X] = cameraAngle[X] - interval;
        }
    }

    @Override
    public void run() {
        while (running) {
            input();
            setPolys();
            frame.repaint();

            try {Thread.sleep(60);} catch (InterruptedException e) {e.printStackTrace();}
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}




