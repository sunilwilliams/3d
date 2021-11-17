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
            {0, 0, 0, 100, 0, 0}

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

    public void set() {
        System.out.println(angleX);
        System.out.println(angleY);
        System.out.println(angleZ);


        setX();
        setY();
        setZ();
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
        switch (e.getKeyChar()) {
            case 'a': {
                angleX = angleX - (pi / 12);
                set();
                setFinal();
            }
            break;
            case 'd': {
                angleX = angleX + (pi / 12);
                set();
                setFinal();
            }
            break;
            case 'w': {
                angleY = angleY - (pi / 12);
                set();
                setFinal();
            }
            break;
            case 's': {
                angleY = angleY + (pi / 12);
                set();
                setFinal();
            }
            break;
            case 'q': {
                angleZ = angleZ - (pi / 12);
                set();
                setFinal();
            }
            break;
            case 'e': {
                angleZ = angleZ + (pi / 12);
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
