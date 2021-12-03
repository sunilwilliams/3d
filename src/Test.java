

public class Test {

    double[][] points = {
            {5, 70},
            {100, 3},
            {50, 80},
    };

    //xLow = 5, xHigh = 100, yLow = 3, yHigh = 80

    final int X = 0;
    final int Y = 1;

    public static void main(String[] args) {
        new Test();
    }


    public Test() {
        findExtremes();
    }

    int xLimit = 320;
    int yLimit = 200;

    public void findExtremes() {

        double currentXLow = xLimit;
        double currentXHigh = 0;
        double currentYLow = yLimit;
        double currentYHigh = 0;

        for (int k = 0; k < 3; k++) {
            double xValue = points[k][X];
            double yValue = points[k][Y];

            if (xValue < currentXLow)
                currentXLow = xValue;
            if (xValue > currentXHigh)
                currentXHigh = xValue;
            if (yValue < currentYLow)
                currentYLow = yValue;
            if (yValue > currentYHigh)
                currentYHigh = yValue;

            System.out.println("currentXLow: " + currentXLow);
            System.out.println("currentXHigh: " + currentXHigh);
            System.out.println("currentYLow: " + currentYLow);
            System.out.println("currentYHigh: " + currentYHigh);
            System.out.println();
        }

        int xLow = (int)currentXLow;
        int xHigh = (int)currentXHigh;
        int yLow = (int)currentYLow;
        int yHigh = (int)currentYHigh;

        System.out.println("xLow: " + xLow);
        System.out.println("xHigh: " + xHigh);
        System.out.println("yLow: " + yLow);
        System.out.println("yHigh: " + yHigh);
        System.out.println();

        if (xLow < 0) xLow = 0;
        if (xHigh > xLimit) xHigh = xLimit;
        if (yLow < 0) yLow = 0;
        if (yHigh > yLimit) yHigh = yLimit;

        System.out.println("xLow: " + xLow);
        System.out.println("xHigh: " + xHigh);
        System.out.println("yLow: " + yLow);
        System.out.println("yHigh: " + yHigh);
    }

}
