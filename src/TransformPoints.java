import java.awt.*;

public class TransformPoints {

    final int X = 0;
    final int Y = 1;
    final int Z = 2;

    double[][] processedPoints;
    double[] cameraPos;
    double[] cameraAngle;
    Color[][] screenPixels = new Color[200][200];



    public TransformPoints(double[][] points, double[] position, double[] angle) {
        cameraPos = position;
        cameraAngle = angle;
        processedPoints = new double[points.length][4];

        changeOrientation();

        for (int i = 0; i < points.length; i++) {
            double[] transformedPoint = setPoint(points[i][X], points[i][Y], points[i][Z]);
            processedPoints[i][X] = transformedPoint[X];
            processedPoints[i][Y] = transformedPoint[Y];
            processedPoints[i][Z] = transformedPoint[Z];
        }
    }

    double interval = (2 * Math.PI / 200);
    double xFov = (Math.PI / 512);
    double yFov = (Math.PI / 512);
    int near = 100;
    int far = 1000;

    public double[] setPoint(double x, double y, double z) {

        /////////////// vertex processing
        {
            x = x - cameraPos[X];
            y = y - cameraPos[Y];
            z = z - cameraPos[Z];

            double angleX = -cameraAngle[X];
            double angleY = -cameraAngle[Y];
            double angleZ = -cameraAngle[Z];

            double cosX = Math.cos(angleX);
            double cosY = Math.cos(angleY);
            double cosZ = Math.cos(angleZ);

            double sinX = Math.sin(angleX);
            double sinY = Math.sin(angleY);
            double sinZ = Math.sin(angleZ);

            double[] d = new double[3];
            d[Y] = cosY * ((sinZ * y) + (cosZ * x)) - (sinY * z);
            d[X] = sinX * ((cosY * z) + (sinY * (sinZ * y + cosZ * x))) + (cosX * (cosZ * y - sinZ * x));
            d[Z] = cosX * ((cosY * z) + (sinY * (sinZ * y + cosZ * x))) - (sinX * (cosZ * y - sinZ * x));

            //d = rotateByVector(x, y, z);

            x = d[X];
            y = d[Y];
            z = d[Z];
        }
        /////////////// projection transformation
        x = x * (1);
        y = y * (1);

        double[] output = new double[4];


        x = (x / Math.tan(xFov / 2));
        y = -(y / Math.tan(yFov / 2));
        //z = ((z * ((far + near) / (far - near))) + ((2 * far * near) / (far - near)));
        double w = -z;

        x = (x / w);
        y = (y / w);
        //z = (z / w);


        output[X] = (int) (x + (screenPixels.length / 2));
        output[Y] = (int) (y + (screenPixels[0].length / 2));
        output[Z] = (int) z;

        return output;
    }

    double[][] orientationVectors = {
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}
    };

    public void changeOrientation() {
        for (int i = 0; i < 3; i++) {
            double[] current = orientationVectors[i];

            current = rotateY(current[X], current[Y], current[Z]);
            current = rotateX(current[X], current[Y], current[Z]);

            orientationVectors[i] = current;
        }
    }

    public double[] rotateX(double x, double y, double z) {
        double[] output = new double[3];
        output[X] = x;
        output[Y] = y * Math.cos(cameraAngle[X]) - z * Math.sin(cameraAngle[X]);
        output[Z] = y * Math.sin(cameraAngle[X]) + z * Math.cos(cameraAngle[X]);
        return output;
    }
    public double[] rotateY(double x, double y, double z) {
        double[] output = new double[3];
        output[X] = z * Math.sin(cameraAngle[Y]) + x * Math.cos(cameraAngle[Y]);
        output[Y] = y;
        output[Z] = z * Math.cos(cameraAngle[Y]) - x * Math.sin(cameraAngle[Y]);
        return output;
    }
    public double[] rotateZ(double x, double y, double z) {
        double[] output = new double[3];
        output[X] = x * Math.sin(cameraAngle[Z]) + y * Math.cos(cameraAngle[Z]);
        output[Y] = x * Math.cos(cameraAngle[Z]) - y * Math.sin(cameraAngle[Z]);
        output[Z] = z;
        return output;
    }

    public double[] rotateByVector(double x, double y, double z) {
        double[] outputs = new double[3];

        for (int i = 0; i < 3; i++) {
            outputs[i] = x * orientationVectors[i][X] + y * orientationVectors[i][Y] + z * orientationVectors[i][Z];
        }

        return outputs;
    }

    public double[][] getProcessedPoints() {
        return processedPoints;
    }

}
