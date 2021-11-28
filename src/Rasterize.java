import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Rasterize implements KeyListener {
    JFrame frame = new JFrame("cube");
    RasterizePanel panel;

    double[][] points = {
            {-50, 200, 50},
            {-100, -50, 50},
            {200, -100, 50},
            {-500, -500, 75},
            {500, -500, 75},
            {50, 500, 75},
    };

    int[][] tris = {
            {0, 1, 2, 0},
            {3, 4, 5, 1}
    };
    
    int[][][] screenTris = new int[100][4][2];


    Color[] colors = {
            Color.RED,
            Color.YELLOW,
            Color.GREEN,
            Color.BLUE
    };

    double[] cameraPos = {0, 0, 0};

    Color[][] pixelColors = new Color[500][500];

    double[][] pointsLocation = new double[3][3];

    double[] cameraAngle = {0, 0, (Math.PI / 2)};

    double[] orientationVector = {0, 0, 1};

    int[][] finalPointCoords = new int[100][3];

    final int X = 0;
    final int Y = 1;
    final int Z = 2;
    final int W = 3;

    public static void main(String[] args) {
        new Rasterize();
    }

    public Rasterize() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        frame.addKeyListener(this);

        for (int x = 0; x < pixelColors.length; x++) {
            for (int y = 0; y < pixelColors[0].length; y++) {
                pixelColors[x][y] = Color.LIGHT_GRAY;
            }
        }

        panel = new RasterizePanel(screenTris);
        frame.add(panel, BorderLayout.CENTER);
        frame.repaint();

        frame.setVisible(true);
    }

    double[] rotate = new double[3];
    double interval = (Math.PI / 24);

    double fov = (Math.PI / 2);
    double near = 1;
    double far = 100;

    //public double[] matrixC ()

    public double[] viewingTransform(double x, double y, double z) {
        x = x - cameraPos[X];
        y = y - cameraPos[Y];
        z = z - cameraPos[Z];

        z = (z * Math.cos(-cameraAngle[Y]) - (x * Math.sin(-cameraAngle[Y])));
        x = (z * Math.sin(-cameraAngle[Y]) + (x * Math.cos(-cameraAngle[Y])));

        double[] outputs = new double[4];
        outputs[X] = (x / Math.tan(fov / 2));
        outputs[Y] = (y / Math.tan(fov / 2));
        outputs[Z] = ((z * ((far + near) / (far - near))) + ((2 * far * near) / (far - near)));
        outputs[W] = -z;

        return backTo3D(outputs[X], outputs[Y], outputs[Z], outputs[W]);
    }

    public double[] flatTransform(double x, double y, double z) {
        x = x - cameraPos[X];
        y = y - cameraPos[Y];
        z = z - cameraPos[Z];

        z = (z * Math.cos(cameraAngle[Y]) - (x * Math.sin(cameraAngle[Y])));
        x = (z * Math.sin(cameraAngle[Y]) + (x * Math.cos(cameraAngle[Y])));


        return new double[]{x, y, z};
    }

    public double[] backTo3D(double x, double y, double z, double w) {
        double[] outputs = new double[3];
        outputs[X] = (x / w);
        outputs[Y] = (y / w);
        outputs[Z] = (z / w);

        return outputs;
    }

    public void setPoints() {
        for (int i = 0; i < points.length; i++) {
            double[] finalCoords = flatTransform(points[i][X], points[i][Y], points[i][Z]);
            finalPointCoords[i][X] = (int)finalCoords[X];
            finalPointCoords[i][Y] = (int)finalCoords[Y];
            finalPointCoords[i][Z] = (int)finalCoords[Z];


        }

        System.out.println(cameraAngle[Y] * Math.PI);
        System.out.println(cameraPos[X]);
        System.out.println(cameraPos[Y]);
        System.out.println(cameraPos[Z]);

        for (int i = 0; i < tris.length; i++) {
            for (int j = 0; j < 3; j++) {
                screenTris[i][j] = finalPointCoords[tris[i][j]];
                //screenTris[i][j][4] = tris[i][3];
            }
        }

    }
    
    



    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        double interval = (Math.PI / 24);
        double move = 1;

        if (e.getKeyChar() == 'w') {
            cameraPos[X] = cameraPos[X] - move * Math.cos(cameraAngle[Y] - (Math.PI / 2));
            cameraPos[Z] = cameraPos[Z] - move * Math.sin(cameraAngle[Y] - (Math.PI / 2));
        }
        if (e.getKeyChar() == 's') {
            cameraPos[X] = cameraPos[X] + move * Math.cos(cameraAngle[Y] - (Math.PI / 2));
            cameraPos[Z] = cameraPos[Z] + move * Math.sin(cameraAngle[Y] - (Math.PI / 2));
        }

        if (e.getKeyChar() == 'q') {
            cameraPos[Y] = cameraPos[Y] - move;
        }
        if (e.getKeyChar() == 'e') {
            cameraPos[Y] = cameraPos[Y] + move;
        }

        if (e.getKeyChar() == 'a') {
            cameraPos[X] = cameraPos[X] - move * Math.cos(cameraAngle[Y]);
            cameraPos[Z] = cameraPos[Z] - move * Math.sin(cameraAngle[Y]);
        }
        if (e.getKeyChar() == 'd') {
            cameraPos[X] = cameraPos[X] + move * Math.cos(cameraAngle[Y]);
            cameraPos[Z] = cameraPos[Z] + move * Math.sin(cameraAngle[Y]);
        }

        //if (e.getKeyCode() == KeyEvent.VK_UP) {cameraAngle[X] = cameraAngle[X] - interval;}
        //if (e.getKeyCode() == KeyEvent.VK_DOWN) {cameraAngle[X] = cameraAngle[X] + interval;}

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {cameraAngle[Y] = cameraAngle[Y] - interval;}
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {cameraAngle[Y] = cameraAngle[Y] + interval;}

        setPoints();

        frame.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}




