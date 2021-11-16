import javax.swing.*;
import java.awt.*;

public class CubePanel extends JPanel {

    int mouseX;
    int mouseY;

    int[] values;


    public CubePanel(int[] in) {
        values = in;


    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //System.out.println(values[0] + ", " + values[1]);

        g.setColor(Color.RED);



        for (double x = -250; x < 250; x = x + 1) {

            for (double i = 0; i < 1; i = i + .1) {
                g.fillRect((int)(x * 1) + 250, (int)(values[1] * Math.sin(values[0] * (x + i))) + 250, 1, 1);
            }
            //g.fillRect((int)(x * 1) + 250, (int)(values[1] * Math.sin((.1) * x)) + 250, 1, 1);

        }





    }


}
