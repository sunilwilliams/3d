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

    double[][] points = {
            {-50, 0, -50},
            {-50, 100, -50},
            {50, 100, -50},
            {50, 0, -50},
            {-50, 0, 50},
            {-50, 100, 50},
            {50, 100, 50},
            {50, 0, 50},

            {100, -50, 100},
            {100, -50, 200},
            {200, -50, 200},
            {200, -50, 100},
            {150, 50, 150},

            {-500, -500, 500},
            {-500, 500, 500},
            {500, 500, 500},
            {500, -500, 500},

            {-50, 0, -50},
            {-50, 100, -50},
            {50, 100, -50},
            {50, 0, -50},
            {-50, 0, 50},
            {-50, 100, 50},
            {50, 100, 50},
            {50, 0, 50},
    };

    double[][] processedPoints = new double[points.length][4];

    int[][] polys = {
            {3, 0, 0, 1, 2},
            {3, 1, 4, 5, 6},
            {3, 2, 0, 1, 4},
            {3, 3, 1, 2, 5},
            {3, 4, 2, 3, 6},
            //{3, 5, 3, 0, 7},

            {3, 0, 2, 3, 0},
            {3, 1, 7, 4, 6},
            {3, 2, 5, 1, 4},
            {3, 3, 6, 2, 5},
            {3, 4, 7, 3, 6},
            //{3, 5, 4, 0, 7},

            //{3, 1, 8, 9, 10},
            //{3, 2, 9, 10, 11},
            {3, 3, 8, 9, 12},
            {3, 4, 9, 10, 12},
            {3, 5, 10, 11, 12},
            {3, 0, 11, 8, 12},

            //{3, 5, 13, 14, 15},
            //{3, 0, 15, 16, 13},
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

    Color[][] screenPixels = new Color[200][200];

    double[][] lights = {
            {1, -1, 0, -200, 100, 0, 500},
    };

    final int X = 0;
    final int Y = 1;
    final int Z = 2;
    final int W = 3;

    final int POS_X = 3;
    final int POS_Y = 4;
    final int POS_Z = 5;
    final int POWER = 6;

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
        frame.setSize(400, 400);
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
    int near = 100;

    public void addPolys() {
        for (int i = 0; i < polys.length; i++) {
            screenPolys[i][COLOR][0] = polys[i][COLOR];
            screenPolys[i][POINT_1] = processedPoints[polys[i][POINT_1]];
            screenPolys[i][POINT_2] = processedPoints[polys[i][POINT_2]];
            screenPolys[i][POINT_3] = processedPoints[polys[i][POINT_3]];
            screenPolys[i][Z_AVERAGE][0] = (screenPolys[i][POINT_1][Z] + screenPolys[i][POINT_2][Z] + screenPolys[i][POINT_3][Z]) / 3;
        }
    }

    Color blank = Color.BLACK;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'w') forward = true;
        if (e.getKeyChar() == 's') backward = true;

        if (e.getKeyChar() == 'q') down = true;
        if (e.getKeyChar() == 'e') up = true;
        if (e.getKeyChar() == ' ') gravity = 20;

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

    double eyeLevel = 50;

    double side = 0;
    double gravity = 0;
    double walk = 0;

    public void input() {
        double speed = 5;

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
            cameraAngle[X] = cameraAngle[X] + interval;
        }
        if (rotateRight) {
            cameraAngle[X] = cameraAngle[X] - interval;
        }
        if (rotateUp) {
            cameraAngle[Y] = cameraAngle[Y] + interval;
        }
        if (rotateDown) {
            cameraAngle[Y] = cameraAngle[Y] - interval;
        }
    }

    double[] walkingAngle = new double[3];

    public void move() {
        cameraPos[X] = cameraPos[X] + walk * Math.cos(-cameraAngle[X] - (Math.PI / 2));
        cameraPos[Z] = cameraPos[Z] + walk * Math.sin(-cameraAngle[X] - (Math.PI / 2));

        cameraPos[Y] = cameraPos[Y] + gravity;

        cameraPos[X] = cameraPos[X] + side * Math.cos(-cameraAngle[X]);
        cameraPos[Z] = cameraPos[Z] + side * Math.sin(-cameraAngle[X]);

        walk = walk / 1.5;

        if (cameraPos[Y] > eyeLevel) {
            gravity = gravity - 2;
        } else {
            cameraPos[Y] = eyeLevel;
        }

        side = side / 1.5;

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

        if (getPointerInfo().getLocation().getY() > (frame.getHeight() - buffers)) {
            setMousePosition();
            lastMouseY = lastMouseY - corrector;
        }
        if (getPointerInfo().getLocation().getY() < buffers) {
            setMousePosition();
            lastMouseY = lastMouseY + corrector;
        }
    }

    double lastMouseX = getPointerInfo().getLocation().getX();
    double lastMouseY = getPointerInfo().getLocation().getY();

    @Override
    public void run() {

        while (running) {

            //System.out.println(Arrays.deepToString(orientationVectors));

            walkingAngle[Y] = walkingAngle[Y] + cameraAngle[Y];

            input();
            move();

            hideMouse();
            correctMousePosition();
            //cameraAngle[X] = cameraAngle[X] - (getPointerInfo().getLocation().getX() - lastMouseX) / 512;
            lastMouseY = getPointerInfo().getLocation().getY();

            cameraAngle[X] = cameraAngle[X] - (getPointerInfo().getLocation().getX() - lastMouseX) / 320;
            lastMouseX = getPointerInfo().getLocation().getX();

            //changeOrientation();

            //transformPoints();

            TransformPoints newPoints = new TransformPoints(points, cameraPos, cameraAngle);
            processedPoints = newPoints.getProcessedPoints();

            addPolys();

            DrawPolys draw = new DrawPolys(screenPolys, near, blank, screenPixels);
            screenPixels = draw.getScreenPixels();

            frame.repaint();

            try {Thread.sleep(30);} catch (InterruptedException e) {e.printStackTrace();}
        }
    }
}
