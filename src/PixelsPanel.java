import javax.swing.*;
import java.awt.*;

public class PixelsPanel extends JPanel {
    Color[][] pixels;

    final int X = 0;
    final int Y = 1;
    final int Z = 2;

    final int POINTS = 0;
    final int COLOR = 1;
    final int POINT_1 = 2;
    final int POINT_2 = 3;
    final int POINT_3 = 4;
    final int POINT_4 = 5;
    final int POINT_5 = 6;

    int frameWidth = 500;
    int frameHeight = 500;

    int[][][] screenPolys;

    int opacity = 50;

    Color[] colors = {
            new Color(255, 255, 255, opacity),
            new Color(255, 125, 0, opacity),
            new Color(255, 255, 0, opacity),
            new Color(0, 255, 0, opacity),
            new Color(0, 0, 255, opacity),
            new Color(255, 0, 255, opacity),
    };

    public PixelsPanel(Color[][] in) {
        pixels = in;
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);

        for (int x = 0; x < pixels[0].length; x++) {
            for (int y = pixels.length - 1; y > -1; y--) {
                g.setColor(pixels[x][y]);
                //System.out.println("pixels[x][y]");
                g.fillRect(x, y, 1, 1);
            }
        }

        //System.out.println(pixels.length);

    }
}
