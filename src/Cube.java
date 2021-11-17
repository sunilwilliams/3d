import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Cube implements MouseListener, KeyListener, Runnable {
    JFrame frame = new JFrame("cube");
    CubePanel panel;
    boolean running;

    double[][][] points = new double[100][100][3];
    final int X = 0;
    final int Y = 1;
    final int Z = 2;

    int[][][] finalMap = new int[points.length][points[0].length][2];

    public static void main(String[] args) {
        new Cube();
    }


    public Cube() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        frame.addKeyListener(this);

        panel = new CubePanel(finalMap);
        frame.add(panel, BorderLayout.CENTER);
        frame.repaint();

        frame.setVisible(true);
    }


    double angleX = 0;
    double angleY = 0;
    double angleZ = 0;

    double pi = Math.PI;


    public void setX() {
        for (int x = 0; x < points[0].length; x++) {
            for (int y = 0; y < points.length; y++) {
                double a = (Math.cos(angleX) * x);
                double b = (Math.sin(angleX) * x);
                double c = (Math.cos(angleX - (pi / 2)) * y);
                double d = (Math.sin(angleX - (pi / 2)) * y);

                points[y][x][Y] = (a + c);
                points[y][x][Z] = (b + d);
            }
        }
    }

    public void setY() {
        for (int x = 0; x < points[0].length; x++) {
            for (int y = 0; y < points.length; y++) {
                double a = (Math.cos(angleY) * x);
                double b = (Math.sin(angleY) * x);
                double c = (Math.cos(angleY - (pi / 2)) * y);
                double d = (Math.sin(angleY - (pi / 2)) * y);

                points[y][x][Z] = points[y][x][Y] + (a + c);
                points[y][x][X] = (b + d);
            }
        }

    }

    public void setZ() {
        for (int x = 0; x < points[0].length; x++) {
            for (int y = 0; y < points.length; y++) {
                double a = (Math.cos(angleZ) * x);
                double b = (Math.sin(angleZ) * x);
                double c = (Math.cos(angleZ - (pi / 2)) * y);
                double d = (Math.sin(angleZ - (pi / 2)) * y);

                points[y][x][X] = points[y][x][X] + (a + c);
                points[y][x][Y] = points[y][x][Y] + (b + d);
            }
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
        for (int x = 0; x < points[0].length; x++) {
            for (int y = 0; y < points.length; y++) {
                finalMap[y][x][X] = (int)(points[y][x][X]);
                finalMap[y][x][Y] = (int)(points[y][x][Y]);
            }
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
