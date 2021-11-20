import javax.swing.*;
import java.awt.*;

public class FillTriangle {
    JFrame frame = new JFrame("cube");
    FillPanel panel;

    double[][] points = {
            {200, 10, 70},
            {30, 200, 50},
            {400, 300, 10}
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

        panel = new FillPanel(points);
        frame.add(panel, BorderLayout.CENTER);
        frame.repaint();

        int pointCounter = 0;

        int radius = 10;




        frame.setVisible(true);
    }


}
