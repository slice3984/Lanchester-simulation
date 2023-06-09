import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SimulationGraphPanel extends JPanel {
    private final int maxY;
    private final int maxX;
    private final Font font;
    private boolean setInitialValues = false;
    private int xDrawAreaStart = 0;
    private int yDrawAreaEnd = 0;
    private final ArrayList<DotData> dots;

    SimulationGraphPanel(int maxY, int maxX) {
        this.maxY = maxY;
        this.maxX = maxX;
        dots = new ArrayList<>();
        font = new Font("Arial", Font.BOLD, 16);

        this.setBounds(0, 0, Constants.SGP_WIDTH, Constants.SGP_HEIGHT);

    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawAxis(g2d);
        plotDots(g2d);
    }

    public void update(double valX, double valY, Color color) {
        dots.add(new DotData(color, valX, valY));
    }

    private void plotDots(Graphics2D g) {
        for (DotData dot : dots) {
            plotDot(g, dot.color(), dot.valX(), dot.valY());
        }
    }

    private void drawAxis(Graphics2D g) {
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        // Biggest text width + Axis + spacing
        int xXAxisStart = metrics.stringWidth(String.valueOf(maxY)) + 20;

        int fontHeight = metrics.getHeight();
        int horizontalSpacing = (Constants.SGP_HEIGHT - fontHeight - 20) / 11; // Keep space of x axis
        int verticalSpacing = (Constants.SGP_WIDTH - xXAxisStart + 30) / 11;
        g.setColor(Color.WHITE);
        for (int i = 0; i < 11; i++) {
            // Y
            g.drawString(String.valueOf(maxY - (i * maxY / 10)), 0, i * horizontalSpacing + fontHeight);

            // X
            g.drawString(String.valueOf((double) (i * maxX / (double) 10)), xXAxisStart - 15 + verticalSpacing * i, Constants.SGP_HEIGHT - 50);
        }

        g.setStroke(new BasicStroke(3));

        // Y-Axis
        g.drawLine(xXAxisStart - 15, 0, xXAxisStart - 15, Constants.SGP_HEIGHT - 50 - fontHeight);

        // X-Axis
        g.drawLine(xXAxisStart - 15, Constants.SGP_HEIGHT - 50 - fontHeight, Constants.SGP_WIDTH, Constants.SGP_HEIGHT - 50 - fontHeight);

        if (!setInitialValues) {
            xDrawAreaStart = xXAxisStart;
            yDrawAreaEnd = Constants.SGP_HEIGHT - 50 - fontHeight;
            setInitialValues = true;
        }
    }

    private void plotDot(Graphics2D g, Color dotColor, double valX, double valY) {
        int drawWidth = Constants.SGP_WIDTH - xDrawAreaStart;
        int drawHeight = yDrawAreaEnd;

        g.setColor(dotColor);

        double pxPerNumX = valX / maxX * drawWidth;
        double pxPerNumY = valY / maxY * drawHeight;

        int yPos = drawHeight - (int) Math.round(pxPerNumY);
        int xPos = (int) Math.round(xDrawAreaStart - 16 + pxPerNumX);
        g.drawLine(xPos, yPos, xPos + 1, yPos + 1);
    }
}
