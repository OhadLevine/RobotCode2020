package frc.robot.subsystems.spinner;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.constants.RobotMap;
import frc.robot.subsystems.OverridableSubsystem;
import io.github.oblarg.oblog.annotations.Log;
import frc.robot.constants.RobotConstants.SpinnerConstants;

public class Spinner extends OverridableSubsystem {
  private final WPI_TalonSRX talonSRX;
  private final ColorSensorV3 colorSensor;
  private final ColorMatch colorMatcher;
  private Color kBlue;
  private Color kGreen;
  private Color kRed;
  private Color kYellow;

  /**
   * This class holds all of the methods for the trench roller subsystem, which
   * spins the trench.
   */
  public Spinner() {
    talonSRX = new WPI_TalonSRX(RobotMap.kRollerTalonSRX);
    talonSRX.setNeutralMode(NeutralMode.Coast);
    talonSRX.setInverted(SpinnerConstants.kIsInverted);
    talonSRX.configOpenloopRamp(SpinnerConstants.kRampRate);

    colorSensor = new ColorSensorV3(RobotMap.kI2cPort);
    colorMatcher = new ColorMatch();

    createColors();
  }

  @Override
  public void overriddenMove(double power) {
    talonSRX.set(power);
  }

  /**
   * @return an enum of the color, including unknown if the minimum threshold is
   *         not met.
   */
  public Color getColor() {
    return colorSensor.getColor();
  }

  /**
   * Compare two colors
   * 
   * @return are the two colors equal.
   */
  public boolean compareColors(Color color, Color colorToCompare) {
    ColorMatchResult match = colorMatcher.matchClosestColor(color);
    return match.color == colorToCompare;
  }

  @Log(name = "Spinner/Is On Color")
  public boolean isOnColor(Color desiredColor) {
    return compareColors(colorSensor.getColor(), desiredColor);
  }

  /**
   * @return Proximity measurement value, ranging from 0 - far, to 2047 - close.
   */
  @Log(name = "Spinner/Proximity")
  public int getProximity() {
    return colorSensor.getProximity();
  }

  /** @return the number to multiply motor output by for positive or negative */
  @Log(name = "Spinner/Spin Direction")
  public int calculateSpinDirection(Color currentColor, Color desiredColor) {
    Color[] colors = { kBlue, kYellow, kGreen, kRed, kBlue };
    int currentColorIndex = 0;
    int desiredColorIndex = 0;
    for (int i = 0; i < colors.length; i++) {
      if (compareColors(colors[i], currentColor))
        currentColorIndex = i;
      if (compareColors(colors[i], desiredColor))
        desiredColorIndex = i;
    }

    // TODO: find when should turn right
    if (compareColors(colors[currentColorIndex + 1], colors[desiredColorIndex]))
      return 1;
    else
      return -1;
  }

  private void createColors() {
    colorMatcher.addColorMatch(SpinnerConstants.kBlue);
    colorMatcher.addColorMatch(SpinnerConstants.kGreen);
    colorMatcher.addColorMatch(SpinnerConstants.kBlue);
    colorMatcher.addColorMatch(SpinnerConstants.kYellow);
  }
}
