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

    double[][] points = new double[1331][7];



    final int X = 0;
    final int Y = 1;
    final int Z = 2;

    final int POS_X = 3;
    final int POS_Y = 4;
    final int POS_Z = 5;

    final int VISIBLE = 6;

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

        int pointCounter = 0;

        int radius = 10;

        for (int x = 0; x <= radius; x = x + 1) {
            for (int y = 0; y <= radius; y = y + 1) {
                for (int z = 0; z <= radius; z = z + 1) {
                    //if (x == -radius || x == radius ||y == -radius || y == radius ||z == -radius || z == radius) {
                        points[pointCounter][POS_X] = x;
                        points[pointCounter][POS_Y] = y;
                        points[pointCounter][POS_Z] = z;

                        points[pointCounter][X] = x;
                        points[pointCounter][Y] = y;
                        points[pointCounter][Z] = z;

                        pointCounter++;
                    //}
                }
            }
        }




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

    public void rotateZ1() {
        for (int y = 0; y < points.length; y++) {
            points[y][X] = (points[y][X] * cos(angleZ) - points[y][Y] * sin(angleZ));
            points[y][Y] = (points[y][X] * sin(angleZ) + points[y][Y] * cos(angleZ));
        }
    }

    public void rotateX1() {
        for (int y = 0; y < points.length; y++) {
            points[y][X] = ((points[y][X] * corrector) + (points[y][Y] * 0) + (points[y][Z] * 0));
            points[y][Y] = ((points[y][X] * 0) + (points[y][Y] * Math.cos(angleX)) - (points[y][Z] * Math.sin(angleX)));
            points[y][Z] = ((points[y][X] * 0) + (points[y][Y] * Math.sin(angleX)) + (points[y][Z] * Math.cos(angleX)));
        }
    }

    public void rotateY1() {
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
            points[y][POS_X] = points[y][POS_X] + moveX;
            points[y][POS_Y] = points[y][POS_Y] + moveY;
            points[y][POS_Z] = points[y][POS_Z] + moveZ;

            points[y][X] = (points[y][X] * (cos(angleZ) * cos(angleY)) + points[y][Y] * (cos(angleZ) * sin(angleY) * sin(angleX) - sin(angleZ) * cos(angleX)) + points[y][Z] * (cos(angleZ) * sin(angleY) * cos(angleX) + sin(angleZ) * sin(angleX)));
            points[y][Y] = (points[y][X] * (sin(angleZ) * cos(angleY)) + points[y][Y] *(sin(angleZ) * sin(angleY) * sin(angleX) + cos(angleZ) * cos(angleX)) + points[y][Z] * (sin(angleZ) * sin(angleY) * cos(angleX) - cos(angleZ) * sin(angleX)));
            points[y][Z] = (points[y][X] * (-1 * sin(angleY)) + points[y][Y] * (cos(angleY) * sin(angleX)) + points[y][Z] * (cos(angleY) * cos(angleX)));
        }


    }

    public void set() {
        System.out.println(angleX);
        System.out.println(angleY);
        System.out.println(angleZ);

        System.out.println(moveX);
        System.out.println(moveY);


        //setX();
        //setY();
        //setZ();

        setAll();
        //rotateX();
        //rotateY();
        //rotateZ();

        //rotateZ1();
    }

    public void pickFront() {
        double[][] currentFront = new double[frame.getWidth()][frame.getHeight()];

        for (int i = 0; i < points.length; i++) {
            for (int x = 0; x < frame.getWidth(); x++) {
                for (int y = 0; y < frame.getHeight(); y++) {
                    if ((int)points[i][X] == x && (int)points[i][Y] == y) {
                        if (points[i][Z] >= currentFront[x][y])
                            currentFront[x][y] = i;
                    }
                }
            }
        }

        for (int j = 0; j < points.length; j++) {
            if (currentFront[(int)points[j][X]][(int)points[j][Y]] == j)
                finalMap[j][X] = (int)(points[j][X] * 10 + 0);
                finalMap[j][Y] = (int)(points[j][Y] * 10 + 0);
        }
    }

    double vanishX = 250;
    double vanishY = 250;

    public void setFinal() {
        for (int j = 0; j < points.length; j++) {
            double depth = (50 / points[j][Z]);

            //System.out.println(points[y][Z]);
            finalMap[j][X] = (int)(points[j][X] * 10 + 0);
            finalMap[j][Y] = (int)(points[j][Y] * 10 + 0);

        }

        //pickFront();


        frame.repaint();

    }

    double moveX;
    double moveY;
    double moveZ;




    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int precision = 24;

        for (int i = 0; i < 3; i++) {
            angleX = 0;
            angleY = 0;
            angleZ = 0;
        }

        switch (e.getKeyChar()) {
            case 'w': {
                angleX = angleX - (pi / precision);
            }
            break;
            case 's': {
                angleX = angleX + (pi / precision);
            }
            break;
            case 'd': {
                angleY = angleY - (pi / precision);
            }
            break;
            case 'a': {
                angleY = angleY + (pi / precision);
            }
            break;
            case 'q': {
                angleZ = angleZ - (pi / precision);
            }
            break;
            case 'e': {
                angleZ = angleZ + (pi / precision);
            }
        }

        moveX = 0;
        moveY = 0;
        moveZ = 0;

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveX = - 10;
            System.out.println("left");
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveX = 10;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            moveY = - 10;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            moveY = 10;
        }
        set();
        setFinal();
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
