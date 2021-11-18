import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Points implements MouseListener, KeyListener, Runnable {
    JFrame frame = new JFrame("cube");
    PointsPanel panel;
    boolean running;

    double[][] points = {
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 100, 0},
            {0, 0, 0, 100, 100, 0},
            {0, 0, 0, 100, 0, 0},
            {0, 0, 0, 0, 0, 100},
            {0, 0, 0, 0, 100, 100},
            {0, 0, 0, 100, 100, 100},
            {0, 0, 0, 100, 0, 100}

    };
    final int X = 0;
    final int Y = 1;
    final int Z = 2;

    final int POS_X = 3;
    final int POS_Y = 4;
    final int POS_Z = 5;

    int[][] finalMap = new int[points.length][2];

    public static void main(String[] args) {
        new Points();
    }


    public Points() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        frame.addKeyListener(this);

        panel = new PointsPanel(finalMap);
        frame.add(panel, BorderLayout.CENTER);
        frame.repaint();

        frame.setVisible(true);
    }


    double angleX = 0;
    double angleY = 0;
    double angleZ = 0;

    double pi = Math.PI;


    public void setX() {
        for (int y = 0; y < points.length; y++) {
            double a = (Math.cos(angleX) * points[y][POS_Y]);
            double b = (Math.sin(angleX) * points[y][POS_Z]);

            points[y][Y] = (a);
            points[y][Z] = (b);
        }

    }

    public void setY() {
        for (int y = 0; y < points.length; y++) {
            double a = (Math.cos(angleY) * points[y][POS_Y]);
            double b = (Math.sin(angleY) * points[y][POS_X]);

            points[y][Z] = points[y][Z] + (a);
            points[y][X] = (b);
        }



    }

    public void setZ() {
        for (int y = 0; y < points.length; y++) {
            double a = (Math.cos(angleZ) * points[y][POS_X]); // calculating
            double b = (Math.sin(angleZ) * points[y][POS_Y]);

            points[y][X] = points[y][X] + (a);
            points[y][Y] = points[y][Y] + (b);
        }



    }

    double corrector = 1;

    public void rotateZ() {
        for (int y = 0; y < points.length; y++) {
            points[y][X] = (points[y][X] * cos(angleZ) - points[y][Y] * sin(angleZ) + points[y][Z] * 0);
            points[y][Y] = (points[y][X] * sin(angleZ) + points[y][Y] * cos(angleZ) + points[y][Z] * 0);
            points[y][Z] = (points[y][X] * 0 - points[y][Y] * 0 + points[y][Z] * 1);
        }
    }

    public void rotateX() {
        for (int y = 0; y < points.length; y++) {
            points[y][X] = ((points[y][X] * corrector) + (points[y][Y] * 0) + (points[y][Z] * 0));
            points[y][Y] = ((points[y][X] * 0) + (points[y][Y] * Math.cos(angleX)) - (points[y][Z] * Math.sin(angleX)));
            points[y][Z] = ((points[y][X] * 0) + (points[y][Y] * Math.sin(angleX)) + (points[y][Z] * Math.cos(angleX)));
        }
    }

    public void rotateY() {
        for (int y = 0; y < points.length; y++) {
            points[y][X] = ((points[y][X] * Math.cos(angleY)) + (points[y][Y] * 0) + (points[y][Z] * Math.sin(angleY)));
            points[y][Y] = ((points[y][X] * 0) + (points[y][Y] * corrector) + (points[y][Z] * 0));
            points[y][Z] = (-(points[y][X] * Math.sin(angleY)) - (points[y][Y] * 0) + (points[y][Z] * Math.cos(angleY)));
        }
    }

    public double sin(double angle) {
        double output = Math.sin(angle);
        return output;
    }
    public double cos(double angle) {
        double output = Math.cos(angle);
        return output;
    }

    public void setAll() {
        for (int y = 0; y < points.length; y++) {
            points[y][X] = (points[y][POS_X] * (cos(angleZ) * cos(angleY)) + points[y][POS_Y] * (cos(angleZ) * sin(angleY) * sin(angleX) - sin(angleZ) * cos(angleX)) + points[y][POS_Z] * (cos(angleZ) * sin(angleY) * cos(angleX) + sin(angleZ) * sin(angleX)));
            points[y][Y] = (points[y][POS_X] * (sin(angleZ) * cos(angleY)) + points[y][POS_Y] *(sin(angleZ) * sin(angleY) * sin(angleX) + cos(angleZ) * cos(angleX)) + points[y][POS_Z] * (sin(angleZ) * sin(angleY) * cos(angleX) - cos(angleZ) * sin(angleX)));
            points[y][Z] = (points[y][POS_X] * (-1 * sin(angleY)) + points[y][POS_Y] * (cos(angleY) * sin(angleX)) + points[y][POS_Z] * (cos(angleY) * cos(angleX)));
        }


    }

    public void set() {
        System.out.println(angleX);
        System.out.println(angleY);
        System.out.println(angleZ);


        //setX();
        //setY();
        //setZ();

        setAll();
        //rotateX();
        //rotateY();
    }

    public void setFinal() {

            for (int y = 0; y < points.length; y++) {
                finalMap[y][X] = (int)(points[y][X]);
                finalMap[y][Y] = (int)(points[y][Y]);
            }

        frame.repaint();

    }


    @Override
    public void keyTyped(KeyEvent e) {
        int precision = 12;

        switch (e.getKeyChar()) {
            case 'w': {
                angleX = angleX - (pi / precision);
                set();
                setFinal();
            }
            break;
            case 's': {
                angleX = angleX + (pi / precision);
                set();
                setFinal();
            }
            break;
            case 'd': {
                angleY = angleY - (pi / precision);
                set();
                setFinal();
            }
            break;
            case 'a': {
                angleY = angleY + (pi / precision);
                set();
                setFinal();
            }
            break;
            case 'q': {
                angleZ = angleZ - (pi / precision);
                set();
                setFinal();
            }
            break;
            case 'e': {
                angleZ = angleZ + (pi / precision);
                set();
                setFinal();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void run() {
        while (running) {

            frame.repaint();


            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
