import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private final double r;
    private final double s;
    private final int startingPopulationRed;
    private final int startingPopulationBlue;
    private int currPopulationRed;
    private int currPopulationRedLanchester;
    private int currPopulationBlue;
    private int currPopulationBlueLanchester;
    private final int predictionPopulationRed;
    private final int predictionPopulationBlue;
    private final double timescale;
    private double currSimulationMillis;

    public InfoPanel(double r,
                     double s,
                     int startingPopulationRed,
                     int startingPopulationBlue,
                     int predictionPopulationRed,
                     int predictionPopulationBlue,
                     double timescale,
                     double currSimulationMillis) {
        this.r = r;
        this.s = s;
        this.startingPopulationRed = startingPopulationRed;
        this.startingPopulationBlue = startingPopulationBlue;
        this.currPopulationRed = startingPopulationRed;
        this.currPopulationRedLanchester = startingPopulationRed;
        this.currPopulationBlue = startingPopulationBlue;
        this.currPopulationBlueLanchester = startingPopulationBlue;
        this.predictionPopulationRed = predictionPopulationRed;
        this.predictionPopulationBlue = predictionPopulationBlue;
        this.timescale = timescale;
        this.currSimulationMillis = currSimulationMillis;

        setBounds(0, 0, Constants.IP_WIDTH, Constants.IP_HEIGHT);
        setLayout(null);
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

        drawPanel(g2d);
    }

    public void update(int currPopulationRed,
                       int currPopulationBlue,
                       int currPopulationRedLanchester,
                       int currPopulationBlueLanchester,
                       double currSimulationMillis) {
        this.currPopulationRed = currPopulationRed;
        this.currPopulationBlue = currPopulationBlue;
        this.currPopulationRedLanchester = currPopulationRedLanchester;
        this.currPopulationBlueLanchester = currPopulationBlueLanchester;
        this.currSimulationMillis = currSimulationMillis;
    }

    private void drawPanel(Graphics2D g) {
        int offset = 20;

        Font fontHeader = new Font("Arial", Font.BOLD, 20);
        Font font = new Font("Arial", Font.BOLD, 16);

        // Drawing info text for red centered horizontally
        g.setFont(fontHeader);
        g.setColor(Color.RED);
        g.drawString("Red", offset, offset * 2);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Starting population: " + startingPopulationRed, offset, offset * 4);
        g.drawString("Current population: " + currPopulationRed, offset, offset * 5);
        g.drawString("Current population (Lanchester): " + currPopulationRedLanchester, offset, offset * 6);
        g.drawString("Predicted population: " + predictionPopulationRed, offset, offset * 7);
        g.drawString("Efficiency: " + String.format("%f", r), offset, offset * 8);


        // Drawing info text for blue
        g.setFont(fontHeader);
        g.setColor(Color.BLUE);
        g.drawString("Blue", Constants.IP_WIDTH / 2 + offset, offset * 2);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Starting population: " + startingPopulationBlue, Constants.IP_WIDTH / 2 + offset, offset * 4);
        g.drawString("Current population: " + currPopulationBlue, Constants.IP_WIDTH / 2 + offset, offset * 5);
        g.drawString("Current population (Lanchester): " + currPopulationBlueLanchester, Constants.IP_WIDTH / 2 + offset, offset * 6);
        g.drawString("Predicted population: " + predictionPopulationBlue, Constants.IP_WIDTH / 2 + offset, offset * 7);
        g.drawString("Efficiency: " + String.format("%f", s), Constants.IP_WIDTH / 2 + offset, offset * 8);

        // Display simulation time
        g.setFont(fontHeader);
        g.setColor(Color.WHITE);
        g.drawString("Timescale: " + String.format("%f", timescale), offset, offset * 10);
        g.drawString("Simulation time: " + currSimulationMillis / 1000 + "s", offset, offset * 12);
    }
}
