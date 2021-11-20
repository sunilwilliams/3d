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

    public void rotate() {
        double corrector = 1;

            int a = 0; int b = 0; int j = 0;

            if (rotate[0] != 0) {a = 1; b = 2; j = 0;}
            if (rotate[1] != 0) {a = 2; b = 0; j = 1;}
            if (rotate[2] != 0) {a = 0; b = 1; j = 2;}

            for (int i = 0; i < points.length; i++) {
                points[i][a] = points[i][a] * Math.cos(rotate[j]) * corrector + points[i][b] * -Math.sin(rotate[j]) * corrector;
                points[i][b] = points[i][a] * Math.sin(rotate[j]) * corrector + points[i][b] * Math.cos(rotate[j]) * corrector;
            }



    }

    @Override
    public void keyTyped(KeyEvent e) {
        double interval = (Math.PI / 24);
        double corrector = 1.02;

        for (int i = 0; i < 3; i++) {rotate[i] = 0;}

        if (e.getKeyChar() == 'a') {
            rotate[Y] = -interval;
            //for (int i = 0; i < 3; i++) { points[i][Y] = points[i][Y] / corrector; points[i][X] = points[i][X] * corrector;}
        }
        if (e.getKeyChar() == 'd') {
            rotate[Y] = interval;
            //for (int i = 0; i < 3; i++) { points[i][Y] = points[i][Y] / corrector; points[i][X] = points[i][X] * corrector;}
        }
        if (e.getKeyChar() == 'w') {
            rotate[X] = -interval;
            //for (int i = 0; i < 3; i++) { points[i][X] = points[i][X] / corrector; points[i][Z] = points[i][Z] * corrector;}
        }
        if (e.getKeyChar() == 's') {
            rotate[X] = interval;
            //for (int i = 0; i < 3; i++) { points[i][X] = points[i][X] / corrector; points[i][Z] = points[i][Z] * corrector;}
        }
        if (e.getKeyChar() == 'q') {
            rotate[Z] = -interval;
            //for (int i = 0; i < 3; i++) { points[i][Z] = points[i][Z] / corrector; points[i][Y] = points[i][Y] * corrector;}
        }
        if (e.getKeyChar() == 'e') {
            rotate[Z] = interval;
            //for (int i = 0; i < 3; i++) { points[i][Z] = points[i][Z] / corrector; points[i][Y] = points[i][Y] * corrector;}
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                points[i][j] = points[i][j] * 1.0085;
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
