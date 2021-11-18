import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public class Line implements KeyListener {
    JFrame frame = new JFrame("cube");
    CubePanel panel;
    boolean running;

    double[][][] points = new double[100][100][3];
    final int X = 0;
    final int Y = 1;
    final int Z = 2;

    int[][][] finalMap = new int[points.length][points[0].length][2];

    public static void main(String[] args) {
        new Line();
    }


    public Line() {
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

    int equals = (-1 * points.length / 2);
    int lessThan = (points.length / 2);

    public void setX() {
        for (int x = equals; x < lessThan; x++) {
            for (int y = equals; y < lessThan; y++) {
                double a = (Math.cos(angleX) * x);
                double b = (-Math.sin(angleZ) * x);
                double c = (Math.cos(angleX - (pi / 2)) * y);
                double d = (Math.sin(angleZ - (pi / 2)) * y);


                double e = (a * b);

                System.out.println(b);

                points[y + lessThan][x + lessThan][X] = (a + c) + (b + d);

                //points[y + lessThan][x + lessThan][X] = points[y + lessThan][x + lessThan][X] + (b + d);
            }
        }
    }

    public void setY() {
        for (int y = equals; y < lessThan; y++) {
            for (int z = equals; z < lessThan; z++) {
                double a = (Math.cos(angleY) * y);
                double b = (Math.sin(angleX) * y);
                double c = (Math.cos(angleY - (pi / 2)) * z);
                double d = (Math.sin(angleX - (pi / 2)) * z);


                //points[z + lessThan][y + lessThan][Y] = (a + c);
                points[z + lessThan][y + lessThan][Y] = (a + c) + (b + d);
            }
            System.out.println(Math.cos(angleY));
        }

    }

    public void setZ() {
        for (int z = equals; z < lessThan; z++) {
            for (int x = equals; x < lessThan; x++) {
                double a = (Math.cos(angleZ) * z);
                double b = (Math.sin(angleY) * z);
                double c = (Math.cos(angleZ - (pi / 2)) * x);
                double d = (Math.sin(angleY - (pi / 2)) * x);

                points[x + lessThan][z + lessThan][Z] = (a + c) + (b + d);
            }
        }

    }

    public void set() {
        //System.out.println(Math.cos(angleY - (pi / 2)));


        setX();
        setY();
        setZ();
    }

    public void setFinal() {


        for (int x = 0; x < points[0].length; x++) {
            for (int y = 0; y < points.length; y++) {
                double depth = points[y][x][Z] * 2 * pi;

                finalMap[y][x][X] = (int) (points[y][x][X] - 0);
                finalMap[y][x][Y] = (int) (points[y][x][Y] - 0);
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
}