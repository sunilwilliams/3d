import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FillTriangle implements KeyListener {
    JFrame frame = new JFrame("cube");
    FillPanel panel;

    double[][] points = {
            {-100, 50, 0},
            {30, 20, 0},
            {100, 200, 0}
    };

    double[][] pointsLocation = new double[3][3];

    final int X = 0;
    final int Y = 1;
    final int Z = 2;

    public static void main(String[] args) {
        new FillTriangle();
    }

    public FillTriangle() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        frame.addKeyListener(this);

        panel = new FillPanel(points);
        frame.add(panel, BorderLayout.CENTER);
        frame.repaint();

        frame.setVisible(true);
    }

    double[] rotate = new double[3];
    double interval = (Math.PI / 24);

    public void rotate() {
        double corrector = 1;
        for (int j = 0; j < 3; j++) {
            int a = 0; int b = 0;

            if (j == 0) {a = 1; b = 2;


            }
            if (j == 1) {a = 2; b = 0;}
            if (j == 2) {a = 0; b = 1;}

            for (int i = 0; i < points.length; i++) {
                points[i][a] = points[i][a] * Math.cos(rotate[j]) * corrector + points[i][b] * -Math.sin(rotate[j]) * corrector;
                points[i][b] = points[i][a] * Math.sin(rotate[j]) * corrector + points[i][b] * Math.cos(rotate[j]) * corrector;
            }

            System.out.println(Math.cos(rotate[j]));
        }


    }

    @Override
    public void keyTyped(KeyEvent e) {

        double corrector = 1.003;

        for (int i = 0; i < 3; i++) {rotate[i] = 0;}

        if (e.getKeyChar() == 'a') {
            rotate[1] = rotate[1] - interval;
        }
        if (e.getKeyChar() == 'd') {
            rotate[1] = rotate[1] + interval;
        }
        if (e.getKeyChar() == 'w') {
            rotate[0] = rotate[0] - interval;
        }
        if (e.getKeyChar() == 's') {
            rotate[0] = rotate[0] + interval;
        }
        if (e.getKeyChar() == 'q') {
            rotate[2] = rotate[2] - interval;
        }
        if (e.getKeyChar() == 'e') {
            rotate[2] = rotate[2] + interval;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //points[i][j] = points[i][j] * 1.005;
            }
        }

        rotate();
        frame.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
