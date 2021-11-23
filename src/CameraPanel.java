import javax.swing.*;
import java.awt.*;

public class CameraPanel extends JPanel {
    Color[][] pixelColors;

    final int X = 0;
    final int Y = 1;
    final int Z = 2;

    public CameraPanel(Color[][] in) {
        pixelColors = in;

    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);

        for (int x = 0; x < 500; x++) {
            for (int y = 0; y < 500; y++) {
                g.setColor(pixelColors[y][x]);
                g.fillRect(x, y, 1, 1);
            }
        }

    }
}
