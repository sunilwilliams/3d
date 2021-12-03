import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static java.awt.MouseInfo.getPointerInfo;

public class RasterizeTest implements KeyListener, Runnable {
    JFrame frame = new JFrame("cube");
    PixelsPanel panel;

    final int X = 0;
    final int Y = 1;
    final int Z = 2;
    final int W = 3;

    final int POINTS = 0;
    final int Z_AVERAGE = 0;
    final int TYPE = 1;
    final int COLOR = 2;
    final int POINT_1 = 3;
    final int POINT_2 = 4;
    final int POINT_3 = 5;
    final int POINT_4 = 6;
    final int POINT_5 = 7;

    final int SIDE = 0;
    final int PLATFORM = 1;

    double[][] points = {
            {-10, 0, -10},
            {-10, 20, -10},
            {10, 20, -10},
            {10, 0, -10},
            {-10, 0, 10},
            {-10, 20, 10},
            {10, 20, 10},
            {10, 0, 10},

            {20, -10, 20},
            {20, -10, 40},
            {40, -10, 40},
            {40, -10, 20},
            {30, 10, 30},

            {-500, 0, -500},
            {-500, 0, 500},
            {500, 0, 500},
            {500, 0, -500},
    };

    double[][] processedPoints = new double[points.length][4];

    int[][] polys = {
            {3, 0, 0, 0, 1, 2},
            {3, 0, 1, 4, 5, 6},
            {3, 0, 2, 0, 1, 4},
            {3, 0, 3, 1, 2, 5},
            {3, 0, 4, 2, 3, 6},
            //{3, 0, 5, 3, 0, 7},

            {3, 0, 0, 2, 3, 0},
            {3, 0, 1, 7, 4, 6},
            {3, 0, 2, 5, 1, 4},
            {3, 0, 3, 6, 2, 5},
            {3, 0, 4, 7, 3, 6},
            //{3, 0, 5, 4, 0, 7},

            //{3, 0, 1, 8, 9, 10},
            //{3, 0, 2, 9, 10, 11},
            {3, 0, 3, 8, 9, 12},
            {3, 0, 4, 9, 10, 12},
            {3, 0, 5, 10, 11, 12},
            {3, 0, 0, 11, 8, 12},

            //{3, 0, 5, 13, 14, 15},
            //{3, 0, 0, 15, 16, 13},
    };

    double[][][] screenPolys = new double[100][10][3];

    Color[] colors = {
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.GREEN,
            Color.BLUE,
            Color.MAGENTA,
            null,
    };

    double[] cameraPos = {0, 0, 0};

    double[] cameraAngle = {0, 0, (Math.PI / 2)};

    Color[][] screenPixels = new Color[250][200];



    boolean running = false;

    public static void main(String[] args) {
        new RasterizeTest();
    }

    public RasterizeTest() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());
        frame.addKeyListener(this);



        panel = new PixelsPanel(screenPixels);
        frame.add(panel, BorderLayout.CENTER);
        frame.repaint();

        if (!running) {
            running = true;
            Thread t = new Thread(this);
            t.start();
        }

        frame.setVisible(true);
    }

    public void setMousePosition() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        for (GraphicsDevice device: gs) {
            try {
                Robot r = new Robot(device);
                r.mouseMove((frame.getWidth() / 2), (frame.getHeight() / 2));
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    public void hideMouse() {
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        frame.getContentPane().setCursor(blankCursor);
    }

    public void correctMousePosition() {
        int buffers = 100;
        int corrector = frame.getWidth() / 2 - buffers;

        if (getPointerInfo().getLocation().getX() > (frame.getWidth() - buffers)) {
            setMousePosition();
            lastMouseX = lastMouseX - corrector;
        }
        if (getPointerInfo().getLocation().getX() < buffers) {
            setMousePosition();
            lastMouseX = lastMouseX + corrector;
        }
    }

    double interval = (2 * Math.PI / 200);
    double xFov = (Math.PI / 256);
    double yFov = (Math.PI / 256);
    int near = 0;
    int far = 10000;
    double[] rotateVector = {1, 1, 1};

    public void transformPoints() {
        for (int i = 0; i < points.length; i++) {
            double[] transformedPoint = setPoint(points[i][X], points[i][Y], points[i][Z]);
            processedPoints[i][X] = transformedPoint[X];
            processedPoints[i][Y] = transformedPoint[Y];
            processedPoints[i][Z] = transformedPoint[Z];
        }
    }

    public double[] rotateByVector(double x, double y, double z) {
        double[] d = new double[3];
        d[X] = x * (rotateVector[X] + 1);
        d[Y] = y * (rotateVector[Y] + 1);
        d[Z] = z * (rotateVector[Z] + 1);

        return d;
    }

    public void test(double x, double y, double z){
        double angleX = -cameraAngle[X];
        double angleY = -cameraAngle[Y];
        double angleZ = -cameraAngle[Z];

        double cosX = Math.cos(angleX);
        double cosY = Math.cos(angleY);
        double cosZ = Math.cos(angleZ);

        double sinX = Math.sin(angleX);
        double sinY = Math.sin(angleY);
        double sinZ = Math.sin(angleZ);

        double[] d = new double[3];
        d[X] = (x * (Math.cos(angleZ) * Math.cos(angleY)) + y * (Math.cos(angleZ) * Math.sin(angleY) * Math.sin(angleX) - Math.sin(angleZ) * Math.cos(angleX)) + z * (Math.cos(angleZ) * Math.sin(angleY) * Math.cos(angleX) + Math.sin(angleZ) * Math.sin(angleX)));
        d[Y] = (x * (Math.sin(angleZ) * Math.cos(angleY)) + y *(Math.sin(angleZ) * Math.sin(angleY) * Math.sin(angleX) + Math.cos(angleZ) * Math.cos(angleX)) + z * (Math.sin(angleZ) * Math.sin(angleY) * Math.cos(angleX) - Math.cos(angleZ) * Math.sin(angleX)));
        d[Z] = (x * (-1 * Math.sin(angleY)) + y * (Math.cos(angleY) * Math.sin(angleX)) + z * (Math.cos(angleY) * Math.cos(angleX)));

        d[Y] = cosY * ((sinZ * y) + (cosZ * x)) - (sinY * z);
        d[X] = sinX * ((cosY * z) + (sinY * (sinZ * y + cosZ * x))) + (cosX * (cosZ * y - sinZ * x));
        d[Z] = cosX * ((cosY * z) + (sinY * (sinZ * y + cosZ * x))) - (sinX * (cosZ * y - sinZ * x));
    }

    // just change rotationVector manually if necessary

    public void rotateRotateVector() {
        double angleX = -cameraAngle[X];
        double angleY = -cameraAngle[Y];
        double angleZ = -cameraAngle[Z];

        double cosX = Math.cos(angleX);
        double cosY = Math.cos(angleY);
        double cosZ = Math.cos(angleZ);

        double sinX = Math.sin(angleX);
        double sinY = Math.sin(angleY);
        double sinZ = Math.sin(angleZ);

        double x = rotateVector[X];
        double y = rotateVector[Y];
        double z = rotateVector[Z];

        double[] d = new double[3];
        d[X] = cosY * ((sinZ * y) + (cosZ * x)) - (sinY * z);
        d[Y] = sinX * ((cosY * z) + (sinY * (sinZ * y + cosZ * x))) + (cosX * (cosZ * y - sinZ * x));
        d[Z] = cosX * ((cosY * z) + (sinY * (sinZ * y + cosZ * x))) - (sinX * (cosZ * y - sinZ * x));

        rotateVector[X] = d[X];
        rotateVector[Y] = d[Y];
        rotateVector[Z] = d[Z];
    }

    public double[] setPoint(double x, double y, double z) {

        /////////////// vertex processing
        x = x - cameraPos[X];
        y = y - cameraPos[Y];
        z = z - cameraPos[Z];

        double[] d = rotateByVector(x, y, z);

        double angleX = -cameraAngle[X];
        double angleY = -cameraAngle[Y];
        double angleZ = -cameraAngle[Z];

        double cosX = Math.cos(angleX);
        double cosY = Math.cos(angleY);
        double cosZ = Math.cos(angleZ);

        double sinX = Math.sin(angleX);
        double sinY = Math.sin(angleY);
        double sinZ = Math.sin(angleZ);

        d[Y] = cosY * ((sinZ * y) + (cosZ * x)) - (sinY * z);
        d[X] = sinX * ((cosY * z) + (sinY * (sinZ * y + cosZ * x))) + (cosX * (cosZ * y - sinZ * x));
        d[Z] = cosX * ((cosY * z) + (sinY * (sinZ * y + cosZ * x))) - (sinX * (cosZ * y - sinZ * x));

        x = d[X];
        y = d[Y];
        z = d[Z];

        /////////////// projection transformation
        //x = x * (frame.getWidth() / xPixelsInFrame);
        //y = y * (frame.getHeight() / yPixelsInFrame);

        double[] output = new double[4];

        double w = -z;
        x = (x / Math.tan(xFov / 2));
        y = (y / Math.tan(yFov / 2));
        //z = ((z * ((far + near) / (far - near))) + ((2 * far * near) / (far - near)));

        x = (x / w);
        y = -(y / w);
        //z = (z / w);

        output[X] = (int) (x + (screenPixels.length / 2));
        output[Y] = (int) (y + (screenPixels[0].length / 2));
        output[Z] = (int) z;

        return output;
    }

    public void addPolys() {
        for (int i = 0; i < polys.length; i++) {
            screenPolys[i][COLOR][0] = polys[i][COLOR];
            screenPolys[i][POINT_1] = processedPoints[polys[i][POINT_1]];
            screenPolys[i][POINT_2] = processedPoints[polys[i][POINT_2]];
            screenPolys[i][POINT_3] = processedPoints[polys[i][POINT_3]];
            screenPolys[i][Z_AVERAGE][0] = (screenPolys[i][POINT_1][Z] + screenPolys[i][POINT_2][Z] + screenPolys[i][POINT_3][Z]) / 3;
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

    Color blank = Color.LIGHT_GRAY;

    public void goThroughZBuffer() {
        int counter = 0;
        for (int i = near; i < 10000; i++) {
            for (int j = 0; j < screenPolys.length; j++) {
                double[][] poly = screenPolys[j];
                if ((int)poly[Z_AVERAGE][0] == i) {
                    double currentXLow = 0;
                    double currentXHigh = 0;
                    double currentYLow = 0;
                    double currentYHigh = 0;

                    for (int k = POINT_1; k < (POINT_3 + 1); k++) {
                        double xValue = poly[k][X];
                        double yValue = poly[k][Y];
                        if (xValue < currentXLow)
                            currentXLow = xValue;
                        if (xValue > currentXHigh)
                            currentXHigh = xValue;
                        if (yValue < currentYLow)
                            currentYLow = yValue;
                        if (yValue > currentYHigh)
                            currentYHigh = yValue;
                    }

                    int xLow = (int)currentXLow;
                    int xHigh = (int)currentXHigh + 1;
                    int yLow = (int)currentYLow;
                    int yHigh = (int)currentYHigh + 1;

                    if (xLow < 0) xLow = 0;
                    if (xHigh > screenPixels.length) xHigh = screenPixels.length;
                    if (yLow < 0) yLow = 0;
                    if (yHigh > screenPixels[0].length) yHigh = screenPixels[0].length;

                    for (int x = xLow; x < xHigh; x++) {
                        for (int y = yLow; y < yHigh; y++) {
                            if (screenPixels[x][y] == blank && isInside(x, y, poly[POINT_1][X], poly[POINT_1][Y], poly[POINT_2][X], poly[POINT_2][Y], poly[POINT_3][X], poly[POINT_3][Y])) {
                                screenPixels[x][y] = colors[(int)poly[COLOR][0]];
                            }
                        }
                    }
                }
            }
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
        if (e.getKeyChar() == ' ') gravity = 10;

        if (e.getKeyChar() == 'a') left = true;
        if (e.getKeyChar() == 'd') right = true;

        if (e.getKeyCode() == KeyEvent.VK_LEFT) rotateLeft = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rotateRight = true;

        if (e.getKeyCode() == KeyEvent.VK_UP) rotateUp = true;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) rotateDown = true;

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {running = !running;}
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

    double eyeLevel = 10;

    double side = 0;
    double gravity = 0;
    double walk = 0;

    public void input() {
        double speed = 1;

        if (backward) {
            walk = walk + speed;
        }
        if (forward) {
            walk = walk - speed;
        }
        if (up) {
            gravity++;
        }
        if (down) {
            gravity--;
        }
        if (right) {
            side = side - speed;
        }
        if (left) {
            side = side + speed;
        }

        //cameraAngle[X] = 0;
        //cameraAngle[Y] = 0;

        if (rotateLeft) {
            cameraAngle[X] = interval;
        }
        if (rotateRight) {
            cameraAngle[X] = -interval;
        }
        if (rotateUp) {
            cameraAngle[Y] = interval;
        }
        if (rotateDown) {
            cameraAngle[Y] = -interval;
        }
    }

    public void move() {
        cameraPos[X] = cameraPos[X] + walk * Math.cos(-cameraAngle[X] - (Math.PI / 2));
        cameraPos[Z] = cameraPos[Z] + walk * Math.sin(-cameraAngle[X] - (Math.PI / 2));

        cameraPos[Y] = cameraPos[Y] + gravity;

        cameraPos[X] = cameraPos[X] + side * Math.cos(-cameraAngle[X]);
        cameraPos[Z] = cameraPos[Z] + side * Math.sin(-cameraAngle[X]);

        walk = walk / 1.5;

        if (cameraPos[Y] > eyeLevel) {
            gravity = gravity - 1;
        } else {
            cameraPos[Y] = eyeLevel;
        }

        side = side / 1.5;

    }

    public void ifTouching() {
        for (int i = 0; i < polys.length; i++) {
            int[] poly = polys[i];
            if (poly[TYPE] == SIDE) {

            }
            if (poly[TYPE] == PLATFORM) {

            }
        }


    }

    double lastMouseX = getPointerInfo().getLocation().getX();

    @Override
    public void run() {
        while (running) {
            for (int x = 0; x < screenPixels.length; x++) {
                for (int y = 0; y < screenPixels[0].length; y++) {
                    screenPixels[x][y] = blank;
                }
            }

            //System.out.println(screenPolys[6][Z_AVERAGE][0]);

            hideMouse();
            correctMousePosition();
            cameraAngle[X] = cameraAngle[X] - (getPointerInfo().getLocation().getX() - lastMouseX) / 256;
            //cameraAngle[X] = (getPointerInfo().getLocation().getX() - lastMouseX) / 256;
            lastMouseX = getPointerInfo().getLocation().getX();

            rotateRotateVector();
            //System.out.println(Arrays.toString(rotateVector));
            //System.out.println();

            input();

            move();

            transformPoints();
            addPolys();
            goThroughZBuffer();

            frame.repaint();

            try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
        }
    }
}




