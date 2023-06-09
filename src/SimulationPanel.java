import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class SimulationPanel extends JPanel {
    private final SimulationLogic simulationLogic;
    private final int startingPopulationRed;
    private final int startingPopulationBlue;
    private int currPopulationRed;
    private int currPopulationBlue;
    private final double efficiencyRed;
    private final double efficiencyBlue;
    private final ArrayList<LaserData> laserShots;
    private final ArrayList<ArmyUnit> armyRed;
    private final ArrayList<ArmyUnit> armyBlue;
    private double leftFractionalShotsRed;
    private double leftFractionalShotsBlue;
    private boolean simulationDone = false;
    private Image skullImage;

    SimulationPanel(int populationRed, int populationBlue, double efficiencyRed, double efficiencyBlue, SimulationLogic simulationLogic) {
        armyRed = new ArrayList<>();
        armyBlue = new ArrayList<>();
        laserShots = new ArrayList<>();

        leftFractionalShotsRed = 0;
        leftFractionalShotsBlue = 0;

        startingPopulationRed = populationRed;
        startingPopulationBlue = populationBlue;
        currPopulationRed = populationRed;
        currPopulationBlue = populationBlue;

        this.efficiencyRed = efficiencyRed;
        this.efficiencyBlue = efficiencyBlue;
        this.skullImage = null;

        this.simulationLogic = simulationLogic;

        initArmies();

        this.setBounds(0, 0, Constants.SP_WIDTH, Constants.SP_HEIGHT);
        this.setLayout(null);
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

        g2d.setColor(new Color(0xdd, 0xdd, 0xdd));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        drawHpBar(g2d, 0, Team.RED);
        drawHpBar(g2d, 680, Team.BLUE);
        spawnLasers(g2d);
        updateLasers();
        drawArmies(g2d);

        if (simulationDone) {
            if (armyRed.size() > 0) {
                // Draw skull centered on blue side
                drawSkull(g2d, Constants.SP_WIDTH / 2 + 150, Constants.SP_HEIGHT / 2 - 150);
            } else {
                drawSkull(g2d, 150, Constants.SP_HEIGHT / 2 - 150);
            }
        }
    }

    private void initArmies() {
        int biggestPopulation = Math.max(currPopulationRed, currPopulationBlue);
        int squareSize = (int) Math.sqrt(450 * 450 / biggestPopulation);
        int squaresPerRow = (int) Math.sqrt(biggestPopulation);

        int offsetY = 100;
        int currY = offsetY;
        int currX  = 0;

        // Red
        for (int i = 0; i < currPopulationRed; i++) {
            armyRed.add(new ArmyUnit(currX, currY, squareSize, Color.red));
            currX += squareSize;

            if (i % squaresPerRow == 0 && i > 0) {
                currY += squareSize;
                currX = 0;
            }
        }

        currY = offsetY;
        currX = Constants.SP_WIDTH - squareSize;

        // Blue
        for (int i = 0; i < currPopulationBlue; i++) {
            armyBlue.add(new ArmyUnit(currX, currY, squareSize, Color.BLUE));
            currX -= squareSize;

            if (i % squaresPerRow == 0 && i > 0) {
                currY += squareSize;
                currX = Constants.SP_WIDTH - squareSize;
            }
        }
    }

    private void drawArmies(Graphics2D g) {
        for (ArmyUnit unit : armyRed) {
            drawUnit(unit, g);
        }

        for (ArmyUnit armyUnit : armyBlue) {
            drawUnit(armyUnit, g);
        }
    }

    private void drawSkull(Graphics2D g, int x, int y) {
        if (skullImage == null) {
            try {
                skullImage = ImageIO.read(new File("skull.png"));
                skullImage = skullImage.getScaledInstance(300, 300, Image.SCALE_DEFAULT);
            } catch (Exception e) {
                System.out.println("Error reading skull image");
            }
        }

        g.drawImage(skullImage, x, y, null);
    }

    private void drawUnit(ArmyUnit unit, Graphics2D g) {
        g.setColor(unit.color());
        g.fillRect(unit.x(), unit.y(), unit.size(), unit.size());
    }

    public void update() {
        PopulationChangeData populationChangeData = simulationLogic.simulationStep();

        int shotsThisFrameRed = populationChangeData.deltaRed();
        int shotsThisFrameBlue = populationChangeData.deltaBlue();

        if (isArmyDefeated()) {
            simulationDone = true;
            return;
        }

        Collections.shuffle(armyRed);
        Collections.shuffle(armyBlue);

        while ((shotsThisFrameRed > 0 || shotsThisFrameBlue > 0) && !isArmyDefeated()) {
            ArmyUnit redUnit = null;
            ArmyUnit blueUnit = null;
            boolean isDeadRed = false;
            boolean isDeadBlue = false;

            // Red vs blue
            if (shotsThisFrameRed > 0) {
                redUnit = armyRed.get(0);
                blueUnit = armyBlue.get(0);

                laserShots.add(new LaserData(redUnit.x(), redUnit.y(), blueUnit.x(), blueUnit.y(), System.currentTimeMillis(), new Color(200, 0, 0)));
                isDeadBlue = true;
                shotsThisFrameRed--;
                currPopulationRed--;
            }

            // Blue vs red
            if (shotsThisFrameBlue > 0) {
                if (shotsThisFrameRed <= 0) {
                    redUnit = armyRed.get(0);
                    blueUnit = armyBlue.get(0);
                }

                laserShots.add(new LaserData(blueUnit.x(), blueUnit.y(), redUnit.x(), redUnit.y(), System.currentTimeMillis(), new Color(0, 0, 200)));
                isDeadRed = true;
                shotsThisFrameBlue--;
                currPopulationBlue--;
            }

            if (isDeadRed) {
                armyRed.remove(redUnit);
            }

            if (isDeadBlue) {
                armyBlue.remove(blueUnit);
            }
        }
    }


    private boolean isArmyDefeated() {
        return armyRed.size() == 0 || armyBlue.size() == 0;
    }

    private void updateLasers() {
        for (int i = 0; i < laserShots.size(); i++) {
            LaserData laserShot = laserShots.get(i);

            if (laserShot == null) {
                continue;
            }

            double endTime = laserShot.startTime() + Constants.LASER_LIFETIME;

            if (endTime < System.currentTimeMillis()) {
                laserShots.remove(laserShot);
            }
        }
    }

    private void spawnLasers(Graphics2D g) {
        for (LaserData laserData : laserShots) {
            if (laserData == null) {
                continue;
            }
            drawLaser(g, laserData.x(), laserData.y(), laserData.x2(), laserData.y2(), laserData.color());
        }
    }


    private void drawLaser(Graphics2D g, int x1, int y1, int x2, int y2, Color c) {
        g.setStroke(new BasicStroke(5));
        g.setColor(c);
        g.drawLine(x1, y1, x2, y2);
    }

    private void drawHpBar(Graphics2D g, int x, Team t) {
        int currPopulation = t == Team.RED ? armyRed.size() : armyBlue.size();

        int barWidth = 0;

        Font font = new Font("Arial", Font.BOLD, 20);
        FontMetrics metrics = g.getFontMetrics(font);
        g.setFont(font);
        g.setColor(Color.BLACK);

        if (t == Team.RED) {
            barWidth = (int) (600 / ((double) startingPopulationRed / (double) currPopulation));
            g.setColor(Color.RED);
            g.fillRect(x, 0, barWidth, 30);

            g.drawString("Population: " + currPopulation, 0, 50);
        } else {
            barWidth = (int) (600 / ((double) startingPopulationBlue / (double) currPopulation));
            g.setColor(Color.BLUE);
            g.fillRect(x + (585 - barWidth), 0, 600, 30);
            g.drawString("Population: " + currPopulation, Constants.SP_WIDTH - metrics.stringWidth("Population:    " + currPopulation), 50);
        }


        g.setColor(t == Team.RED ? Color.RED : Color.BLUE);
    }

    public boolean isSimulationDone() {
        return simulationDone;
    }

    public int getCurrPopulationRed() {
        return armyRed.size();
    }

    public int getCurrPopulationBlue() {
        return armyBlue.size();
    }
}
