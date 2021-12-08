import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PanelPainter implements MouseListener, ActionListener {

    JFrame frame = new JFrame("Painter");

    PainterPanel panel;
    
    int resX = 32;
    int resY = 32;

    JTextField name = new JTextField();
    JButton print = new JButton("print");
    Color[][] finalColors = new Color[resX][resY];
    Color[][] colors = new Color[resX][resY];

    int width;
    int height;

    boolean painting = false;

    Color lightGrayColor = new Color(240, 240, 240);

    JButton undo = new JButton("<");
    JButton reset = new JButton("reset");
    JButton erase = new JButton("erase");
    JButton paint = new JButton("paint");
    JSlider r = new JSlider(SwingConstants.VERTICAL);
    JSlider g = new JSlider(SwingConstants.VERTICAL);
    JSlider b = new JSlider(SwingConstants.VERTICAL);
    JButton setColor = new JButton("set color");
    JButton setSavedColors = new JButton("set");
    JSlider setSavedColorsNumber = new JSlider();
    JButton[] saveColors = new JButton[4];

    Color[] saveColorsColors = new Color[saveColors.length];
    Color color = new Color(r.getValue(), g.getValue(), b.getValue());

    Container south = new Container();
    Container savedColors = new Container();

    public static void main(String[] args) {
        new PanelPainter();
    }

    public PanelPainter() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 700);
        frame.setLayout(new BorderLayout());
        frame.addMouseListener(this);

        width = (frame.getWidth() - 200) / resX;
        height = (frame.getHeight() - 100) / resY;

        panel = new PainterPanel(colors, new int[]{width, height});
        frame.add(panel, BorderLayout.CENTER);
        frame.repaint();





        savedColors.add(name);
        name.addActionListener(this);

        savedColors.add(print);
        print.addActionListener(this);



        south.add(undo);
        south.add(reset);

        south.add(erase);
        south.add(paint);

        south.add(r);
        south.add(g);
        south.add(b);
        south.add(setColor);


        undo.addActionListener(this);
        reset.addActionListener(this);

        erase.addActionListener(this);
        paint.addActionListener(this);

        r.setMaximum(255);
        g.setMaximum(255);
        b.setMaximum(255);

        setColor.addActionListener(this);

        south.setLayout(new GridLayout(4, 2));
        frame.add(south, BorderLayout.EAST);

        savedColors.add(setSavedColorsNumber);
        savedColors.add(setSavedColors);

        setSavedColors.addActionListener(this);

        setSavedColorsNumber.setMaximum(saveColors.length - 1);

        for (int i = 0; i < saveColors.length; i++) {
            saveColors[i] = new JButton();
            savedColors.add(saveColors[i]);
            saveColors[i].addActionListener(this);
            saveColors[i].setOpaque(true);
        }

        savedColors.setLayout(new GridLayout(2, 4));
        frame.add(savedColors, BorderLayout.SOUTH);

        frame.setVisible(true);
}

    @Override
    public void actionPerformed(ActionEvent e) {

        erase.setEnabled(true);

        if (e.getSource().equals(undo) && inputNum > -1) {
            undo();
        }
        if (e.getSource().equals(reset)) {
            for (int x = 0; x < resX; x++) {
                for (int y = 0; y < resY; y++) {
                    colors[x][y] = null;
                    finalColors[x][y] = null;

                    setPassedInputs(x, y);
                }
            }
        }

        if (e.getSource().equals(erase)) {
            color = null;
            erase.setEnabled(false);
        }
        if (e.getSource().equals(paint)) {
            if (!painting) {
                painting = true;
                paint.setOpaque(true);
                paint.setBackground(Color.GRAY);
            } else {
                painting = false;
                paint.setOpaque(false);
                paint.setBackground(null);
            }
        }

        if (e.getSource().equals(setColor)) {
            color = new Color(r.getValue(), g.getValue(), b.getValue());
        }

        if (e.getSource().equals(setSavedColors)) {
            saveColors[setSavedColorsNumber.getValue()].setBackground(color);
            saveColorsColors[setSavedColorsNumber.getValue()] = color;
        }

        for (int i = 0; i < saveColors.length; i++) {
            if (e.getSource().equals(saveColors[i])) {
                color = saveColorsColors[i];
            }
        }

        frame.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    boolean clicking = false;

    @Override
    public void mousePressed(MouseEvent e) {
        clicking = true;
        
        colors[(int)(e.getX() / resX)][(int)(e.getY() / resY)] = color;

        frame.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clicking = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    Color clickedColor = color;

    int checks = 0;
    int checkNum = 10000;

    public void checkHorizontal(int x, int y) {
        int originX = x;

        while ((x - 1) > -1 && finalColors[x - 1][y] == null) {
            x--;

            colors[x][y] = color;
            finalColors[x][y] = color;
            colors[x][y] = color;
            if (checks < checkNum) {
                checkVertical(x, y);
                checks++;
            }
        }

        x = originX;

        while ((x + 1) < (resY) && finalColors[x + 1][y] == null) {
            x++;

            colors[x][y] = color;
            finalColors[x][y] = color;
            colors[x][y] = color;
            if (checks < checkNum) {
                checkVertical(x, y);
                checks++;
            }
        }


    }

    public void checkVertical(int x, int y) {
        int originY = y;

        while ((y - 1) > -1 && finalColors[x][y - 1] == null) {
            y--;

            colors[x][y] = color;
            finalColors[x][y] = color;
            colors[x][y] = color;
            if (checks < checkNum) {
                checkHorizontal(x, y);
                checks++;
            }
        }

        y = originY;

        while ((y + 1) < (resY) && finalColors[x][y + 1] == null) {
            y++;

            colors[x][y] = color;
            finalColors[x][y] = color;
            colors[x][y] = color;
            if (checks < checkNum) {
                checkHorizontal(x, y);
                checks++;
            }
        }


    }

    public void setPassedInputs(int x, int y) {
        passedX[inputNum + 1] = x;
        passedY[inputNum + 1] = y;
        passedColor[inputNum + 1] = colors[x][y];

        inputNum++;
    }

    int inputNum = 0;

    int[] passedX = new int[50000];
    int[] passedY = new int[50000];
    Color[] passedColor = new Color[50000];

    public void undo() {
        colors[passedX[inputNum]][passedY[inputNum]] = (passedColor[inputNum]);


        if (inputNum > -1)
            inputNum--;
    }

    
}
