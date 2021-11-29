import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import static java.awt.MouseInfo.getPointerInfo;

public class RasterizeTest implements KeyListener, MouseMotionListener, Runnable {
    JFrame frame = new JFrame("cube");
    PixelsPanel panel;

    double[][] points = {
            {-50, -50, -50},
            {-50, 50, -50},
            {50, 50, -50},
            {50, -50, -50},
            {-50, -50, 50},
            {-50, 50, 50},
            {50, 50, 50},
            {50, -50, 50},

            {100, -50, 100},
            {100, -50, 200},
            {200, -50, 200},
            {200, -50, 100},
            {150, 50, 150},
    };

    double[][] processedPoints = new double[points.length][points[0].length];

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

            {3, 1, 8, 9, 10},
            {3, 2, 9, 10, 11},
            {3, 3, 8, 9, 12},
            {3, 4, 9, 10, 12},
            {3, 5, 10, 11, 12},
            {3, 0, 11, 8, 12},
    };

    double[][][] screenPolys = new double[100][10][3];

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

    Color[][] screenPixels = new Color[500][500];

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

    boolean running = false;

    public static void main(String[] args) {
        new RasterizeTest();
    }

    public RasterizeTest() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
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

    double interval = (2 * Math.PI / 200);
    double xFov = (Math.PI / 512);
    double yFov = (Math.PI / 512);
    int near = 1;
    int far = 100;
    int xPixelsInFrame = 150;
    int yPixelsInFrame = 150;

    public void transformPoints() {
        for (int i = 0; i < points.length; i++) {
            double[] transformedPoint = setPoint(points[i][X], points[i][Y], points[i][Z]);
            processedPoints[i][X] = transformedPoint[X];
            processedPoints[i][Y] = transformedPoint[Y];
            processedPoints[i][Z] = transformedPoint[Z];
        }
    }

    public double[] setPoint(double x, double y, double z) {

        /////////////// vertex processing
        x = x - cameraPos[X];
        y = y - cameraPos[Y];
        z = z - cameraPos[Z];

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

        d[Y] = cosY * ((sinZ * y) + (cosZ * x)) - (sinY * z);
        d[X] = sinX * ((cosY * z) + (sinY * (sinZ * y + cosZ * x))) + (cosX * (cosZ * y - sinZ * x));
        d[Z] = cosX * ((cosY * z) + (sinY * (sinZ * y + cosZ * x))) - (sinX * (cosZ * y - sinZ * x));

        x = d[X];
        y = d[Y];
        z = d[Z];

        /////////////// projection transformation
        x = x * (frame.getWidth() / xPixelsInFrame);
        y = y * (frame.getHeight() / yPixelsInFrame);

        double w = -z;
        x = (x / Math.tan(xFov / 2));
        y = -(y / Math.tan(yFov / 2));
        z = ((z * ((far + near) / (far - near))) + ((2 * far * near) / (far - near)));

        x = (x / w);
        y = (y / w);
        //z = (z / w);

        double[] output = new double[3];
        output[X] = (int) x + (frame.getWidth() / 2);
        output[Y] = (int) y + (frame.getHeight() / 2);
        output[Z] = (int) z;

        return output;
    }

    public void addPolys() {
        for (int i = 0; i < polys.length; i++) {
            screenPolys[i][COLOR][0] = polys[i][COLOR];
            screenPolys[i][POINT_1] = processedPoints[polys[i][POINT_1]];
            screenPolys[i][POINT_2] = processedPoints[polys[i][POINT_2]];
            screenPolys[i][POINT_3] = processedPoints[polys[i][POINT_3]];

            screenPolys[i][Z_AVERAGE][0] = (screenPolys[i][POINT_1][Z] + screenPolys[i][POINT_2][Z] + screenPolys[i][POINT_3][Z]);
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

    public void goThroughZBuffer() {
        for (int i = near; i < 10000; i++) {
            for (int j = 0; j < screenPolys.length; j++) {
                if ((int)screenPolys[j][Z_AVERAGE][0] == i) {
                    //System.out.println(i);
                    double[][] poly = screenPolys[j];
                    for (int x = 0; x < screenPixels[0].length; x++) {
                        for (int y = 0; y < screenPixels.length; y++) {
                            if (screenPixels[x][y] == Color.LIGHT_GRAY && isInside(x, y, poly[POINT_1][X], poly[POINT_1][Y], poly[POINT_2][X], poly[POINT_2][Y], poly[POINT_3][X], poly[POINT_3][Y])) {
                                screenPixels[x][y] = colors[(int)poly[COLOR][0]];
                                //System.out.println(screenPixels[x][y]);
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

        if (e.getKeyChar() == 'a') left = true;
        if (e.getKeyChar() == 'd') right = true;

        if (e.getKeyCode() == KeyEvent.VK_LEFT) rotateLeft = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rotateRight = true;

        if (e.getKeyCode() == KeyEvent.VK_UP) rotateUp = true;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) rotateDown = true;

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            running = !running;
        }
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

        if (backward) {
            cameraPos[X] = cameraPos[X] + move * Math.cos(-cameraAngle[X] - (Math.PI / 2));
            cameraPos[Z] = cameraPos[Z] + move * Math.sin(-cameraAngle[X] - (Math.PI / 2));
        }
        if (forward) {
            cameraPos[X] = cameraPos[X] - move * Math.cos(-cameraAngle[X] - (Math.PI / 2));
            cameraPos[Z] = cameraPos[Z] - move * Math.sin(-cameraAngle[X] - (Math.PI / 2));
        }
        if (up) {
            cameraPos[Y] = cameraPos[Y] + move;
        }
        if (down) {
            cameraPos[Y] = cameraPos[Y] - move;
        }
        if (right) {
            cameraPos[X] = cameraPos[X] - move * Math.cos(-cameraAngle[X]);
            cameraPos[Z] = cameraPos[Z] - move * Math.sin(-cameraAngle[X]);
        }
        if (left) {
            cameraPos[X] = cameraPos[X] + move * Math.cos(-cameraAngle[X]);
            cameraPos[Z] = cameraPos[Z] + move * Math.sin(-cameraAngle[X]);
        }
        if (rotateLeft) {
            cameraAngle[X] = cameraAngle[X] + interval;
        }
        if (rotateRight) {
            cameraAngle[X] = cameraAngle[X] - interval;
        }
        if (rotateUp) {
            //cameraAngle[Y] = cameraAngle[Y] + interval;
        }
        if (rotateDown) {
            //cameraAngle[Y] = cameraAngle[Y] - interval;
        }
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

    double lastMouseX = getPointerInfo().getLocation().getX();

    @Override
    public void run() {

        while (running) {
            for (int x = 0; x < screenPixels[0].length; x++) {
                for (int y = 0; y < screenPixels.length; y++) {
                    screenPixels[x][y] = Color.LIGHT_GRAY;
                }
            }

            hideMouse();
            correctMousePosition();
            cameraAngle[X] = cameraAngle[X] - (getPointerInfo().getLocation().getX() - lastMouseX) / 512;
            lastMouseX = getPointerInfo().getLocation().getX();

            input();

            //System.out.println(cameraPos[Z]);

            transformPoints();
            addPolys();
            goThroughZBuffer();

            frame.repaint();

            try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}




