import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CubeLabels implements KeyListener {
    boolean running = false;
    boolean jump = false;
    boolean left = false;
    boolean right = false;

    JFrame frame = new JFrame("3D");

    JLabel[][] square = new JLabel[100][10000];



    double[][][] locations = new double[3][3][3];

    final int X = 0;
    final int Y = 1;
    final int Z = 2;


    public static void main(String[] args) {  //main method

        new CubeLabels();

    }

    public CubeLabels() {  //constructor
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(null);
        frame.addKeyListener(this);

        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 10000; y++) {
                square[x][y] = new JLabel();
                square[x][y].setSize(1, 1);
                frame.add(square[x][y]);
            }
        }




        frame.setVisible(true);


    }

    int lineNumber = 0;
    double pi = Math.PI;

    public void line(int lineGroup, int x, int y, double angle, int lineNum) {
        //System.out.println((Math.sin((pi / 2) - angle)) / pi);
        for (int h = -13; h < 14; h++) {
            double a = (Math.cos(angle) * h);
            double b = (Math.sin(angle) * h);
            double c = (Math.cos(angle - (pi / 2)) * lineNum);
            double d = (Math.sin(angle - (pi / 2)) * lineNum);

            //double c = (Math.cos((pi / 2) - angle) * lineNum);
            //double d = (Math.sin((pi / 2) - angle) * lineNum);

            square[lineGroup][lineNumber].setLocation(x + (int)(2 * (a + c)), y + (int)(2 * (b + d)));
            square[lineGroup][lineNumber].setBackground(Color.BLACK);
            square[lineGroup][lineNumber].setOpaque(true);
            lineNumber++;
        }
    }

    public void cube(int lineGroup, int x, int y, double angle, int lineNum) {
        x = 300;
        y = 300;


        for (int i = -13; i < 14; i++) {
            for (int h = -13; h < 14; h++) {
                double a = (Math.cos(angle) * h);
                double b = (Math.sin(angle) * h);
                double c = (Math.cos(angle - (pi / 2)) * lineNum);
                double d = (Math.sin(angle - (pi / 2)) * lineNum);

                //double c = (Math.cos((pi / 2) - angle) * lineNum);
                //double d = (Math.sin((pi / 2) - angle) * lineNum);

                square[lineGroup][lineNumber].setLocation(x + (int)(2 * (a + c)), y + (int)(2 * (b + d)));
                square[lineGroup][lineNumber].setBackground(Color.BLACK);
                square[lineGroup][lineNumber].setOpaque(true);
                lineNumber++;
            }
        }

    }

    double angle = 0;

    @Override
    public void keyTyped(KeyEvent e) {

        switch (e.getKeyChar()) {
            case 'a': {
                lineNumber = 0;
                angle = angle - (pi / 12);
                for (int i = -13; i < 14; i++) {
                    line(0, 300, 300, angle, i);
                }
            }
            break;
            case 'd': {
                lineNumber = 0;
                angle = angle + (pi / 12);
                for (int i = -13; i < 14; i++) {
                    line(0, 300, 300, angle, i);
                }
            }
            break;
            case ' ': {
                angle = 0;

                jump = true;
                //}
            }
            break;
            case 'w': {
                //if (touchingGround() || touchingPlatform()[0] == 1) {
                jump = true;
                //}
            }
            break;
            case 'r': {
                //reset();
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