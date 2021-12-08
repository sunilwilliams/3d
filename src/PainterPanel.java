import javax.swing.*;
import java.awt.*;

public class PainterPanel extends JPanel {
    Color[][] pixels;
    int width;
    int height;

    public PainterPanel(Color[][] in, int[] dimensions) {
        pixels = in;
        width = dimensions[0];
        height = dimensions[1];
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("paint");
        System.out.println(width);

        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[0].length; y++) {
                g.setColor(pixels[x][y]);

                if (pixels[x][y] == null) {
                    g.setColor(Color.white);
                }
                g.fillRect(x * width, y * height, width, height);
            }
        }
    }
}
