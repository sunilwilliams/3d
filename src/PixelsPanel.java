import javax.swing.*;
import java.awt.*;

public class PixelsPanel extends JPanel {
    Color[][] pixels;

    public PixelsPanel(Color[][] in) {
        pixels = in;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);

        for (int x = 0; x < pixels.length; x++) {
            for (int y = pixels[0].length - 1; y > -1; y--) {
                g.setColor(pixels[x][y]);
                g.fillRect(x * 2, y * 2, 2, 2);
            }
        }
    }
}
