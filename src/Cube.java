import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Cube implements MouseListener, Runnable {


    JFrame frame = new JFrame("cube");

    CubePanel panel;

    boolean running;

    int[] values = {0, 0};

    public static void main(String[] args) {
        new Cube();
    }


    public Cube() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        frame.addMouseListener(this);

        panel = new CubePanel(values);
        frame.add(panel, BorderLayout.CENTER);
        frame.repaint();

        frame.setVisible(true);


        if (!running) {
            running = true;
            Thread t = new Thread(this);
            t.start();
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        values[0] = e.getX();
        values[1] = e.getY();
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
            //System.out.println("running");

            //System.out.println(values[0] + ", " + values[1]);

            frame.repaint();


            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
