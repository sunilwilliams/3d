import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RasterizeTest implements KeyListener {
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

    double[] cameraPos = {0, 0, 0};

    double[] cameraAngle = {0, 0, (Math.PI / 2)};

    int[][] finalPointCoords = new int[100][3];

    final int X = 0;
    final int Y = 1;
    final int Z = 2;
    final int W = 3;

    public static void main(String[] args) {
        new RasterizeTest();
    }

    public RasterizeTest() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        frame.addKeyListener(this);

        panel = new RasterizePanel(finalPointCoords);
        frame.add(panel, BorderLayout.CENTER);
        frame.repaint();

        frame.setVisible(true);
    }

    double interval = (2 * Math.PI / 200);


    double fov = (Math.PI / 2);
    double near = 10;
    double far = 100;
    int xPixelsInFrame = 5;
    int yPixelsInFrame = 5;

    public void setPoints() {
        for (int i = 0; i < points.length; i++) {
            double x = points[i][X];
            double y = points[i][Y];
            double z = points[i][Z];

            double angleY = -cameraAngle[Y];
            x = ((x * Math.cos(angleY)) + (y * 0) + (z * Math.sin(angleY)));
            y = ((x * 0) + (y) + (z * 0));
            z = (-(x * Math.sin(angleY)) - (y * 0) + (z * Math.cos(angleY)));

            x = x + cameraPos[X];
            y = y + cameraPos[Y];
            z = z - cameraPos[Z];

            x = x * (frame.getWidth() / xPixelsInFrame);
            y = y * (frame.getHeight() / yPixelsInFrame);


            //z = (z * Math.cos(-cameraAngle[Y]) - (x * Math.sin(-cameraAngle[Y])));
            //x = (z * Math.sin(-cameraAngle[Y]) + (x * Math.cos(-cameraAngle[Y])));



            double w = (-z);
            x = (x / Math.tan(fov / 2));
            y = (y / Math.tan(fov / 2));
            z = ((z * ((far + near) / (far - near))) + ((2 * far * near) / (far - near)));
            //double w = -z;

            x = (x / w);
            y = (y / w);
            //z = (z / w);


            finalPointCoords[i][X] = -500;
            finalPointCoords[i][Y] = -500;
            finalPointCoords[i][Z] = -500;



            if (z >= (near * 2)) {
                finalPointCoords[i][X] = (int) x + (frame.getWidth() / 2);
                finalPointCoords[i][Y] = (int) y + (frame.getHeight() / 2);
                finalPointCoords[i][Z] = (int) z;
            }
        }

        System.out.println("a: " + cameraAngle[Y]);
        System.out.println("x: " + cameraPos[X]);
        System.out.println("y: " + cameraPos[Y]);
        System.out.println("z: " + cameraPos[Z]);
        System.out.println();
    }





    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        double move = 5;

        if (e.getKeyChar() == 'w') {
            cameraPos[X] = cameraPos[X] - move * Math.cos(cameraAngle[Y] - (Math.PI / 2));
            cameraPos[Z] = cameraPos[Z] - move * Math.sin(cameraAngle[Y] - (Math.PI / 2));
        }
        if (e.getKeyChar() == 's') {
            cameraPos[X] = cameraPos[X] + move * Math.cos(cameraAngle[Y] - (Math.PI / 2));
            cameraPos[Z] = cameraPos[Z] + move * Math.sin(cameraAngle[Y] - (Math.PI / 2));
        }

        if (e.getKeyChar() == 'q') {
            cameraPos[Y] = cameraPos[Y] - move;
        }
        if (e.getKeyChar() == 'e') {
            cameraPos[Y] = cameraPos[Y] + move;
        }

        if (e.getKeyChar() == 'a') {
            cameraPos[X] = cameraPos[X] - move * Math.cos(cameraAngle[Y]);
            cameraPos[Z] = cameraPos[Z] - move * Math.sin(cameraAngle[Y]);
        }
        if (e.getKeyChar() == 'd') {
            cameraPos[X] = cameraPos[X] + move * Math.cos(cameraAngle[Y]);
            cameraPos[Z] = cameraPos[Z] + move * Math.sin(cameraAngle[Y]);
        }

        //if (e.getKeyCode() == KeyEvent.VK_UP) {cameraAngle[X] = cameraAngle[X] - interval;}
        //if (e.getKeyCode() == KeyEvent.VK_DOWN) {cameraAngle[X] = cameraAngle[X] + interval;}

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {cameraAngle[Y] = cameraAngle[Y] + interval;}
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {cameraAngle[Y] = cameraAngle[Y] - interval;}

        setPoints();

        frame.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    boolean forward;
    boolean backward;
    boolean down;
    boolean up;
    boolean left;
    boolean right;
    boolean rotateLeft;
    boolean rotateRight;


    public void input() {
        double move = 5;

        if (forward) {
            cameraPos[X] = cameraPos[X] - move * Math.cos(cameraAngle[Y] - (Math.PI / 2));
            cameraPos[Z] = cameraPos[Z] - move * Math.sin(cameraAngle[Y] - (Math.PI / 2));
        }
        if (backward) {
            cameraPos[X] = cameraPos[X] + move * Math.cos(cameraAngle[Y] - (Math.PI / 2));
            cameraPos[Z] = cameraPos[Z] + move * Math.sin(cameraAngle[Y] - (Math.PI / 2));
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
    }




}




