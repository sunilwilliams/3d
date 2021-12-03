import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class RotationVector implements KeyListener {
    JFrame frame = new JFrame("rotationVector");

    double[] iHat = {1, 0, 0};
    double[] jHat = {0, 1, 0};
    double[] kHat = {0, 0, 1};

    double[] cameraAngle = new double[3];

    final int X = 0;
    final int Y = 1;
    final int Z = 2;

    public static void main(String[] args) {
        new RotationVector();
    }

    public RotationVector() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());
        frame.addKeyListener(this);
        frame.setVisible(true);

    }

    public void rotateRotateVector() {
        double angleX = -cameraAngle[X];
        double angleY = -cameraAngle[Y];
        double angleZ = -cameraAngle[Z];

        double cosX = Math.cos(angleX);
        double cosY = Math.cos(angleY);
        double cosZ = Math.cos(angleZ);

        double sinX = Math.sin(angleX);
        double sinY = Math.sin(angleY);
        double sinZ = Math.sin(angleZ);

        double x = iHat[X];
        double y = iHat[Y];
        double z = iHat[Z];

        double[] d = new double[3];
        d[X] = cosY * ((sinZ * y) + (cosZ * x)) - (sinY * z);
        d[Y] = sinX * ((cosY * z) + (sinY * (sinZ * y + cosZ * x))) + (cosX * (cosZ * y - sinZ * x));
        d[Z] = cosX * ((cosY * z) + (sinY * (sinZ * y + cosZ * x))) - (sinX * (cosZ * y - sinZ * x));

        iHat[X] = d[X];
        iHat[Y] = d[Y];
        iHat[Z] = d[Z];
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    double interval = Math.PI / 256;

    @Override
    public void keyPressed(KeyEvent e) {
        cameraAngle[Y] = 0;
        cameraAngle[X] = 0;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) cameraAngle[Y] = -interval;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) cameraAngle[Y] = interval;
        if (e.getKeyCode() == KeyEvent.VK_UP) cameraAngle[X] = -interval;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) cameraAngle[X] = interval;

        rotateRotateVector();

        System.out.println(Arrays.toString(iHat));
        System.out.println(Arrays.toString(jHat));
        System.out.println(Arrays.toString(kHat));
        System.out.println();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
