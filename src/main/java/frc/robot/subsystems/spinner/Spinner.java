package frc.robot.subsystems.spinner;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.constants.RobotMap;
import frc.robot.subsystems.OverridableSubsystem;
import frc.robot.utils.DriverStationLogger;
import frc.robot.constants.RobotConstants.SpinnerConstants;

public class Spinner extends OverridableSubsystem {
  private final WPI_TalonSRX talonSRX;
  private final ColorSensorV3 colorSensor;
  private final ColorMatch colorMatcher;

  /**
   * This class holds all of the methods for the trench roller subsystem, which
   * spins the trench.
   */
  public Spinner() {
    talonSRX = new WPI_TalonSRX(RobotMap.kRollerTalonSRX);
    talonSRX.setNeutralMode(NeutralMode.Coast);
    talonSRX.setInverted(SpinnerConstants.kIsInverted);
    talonSRX.configOpenloopRamp(SpinnerConstants.kRampRate);
    talonSRX.configClosedloopRamp(SpinnerConstants.kRampRate);

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

  // TODO: write compare color function
  public boolean isOnColor(Color desiredColor) {
    ColorMatchResult match = colorMatcher.matchClosestColor(colorSensor.getColor());
    return match.color == desiredColor ? true : false;
  }

  /**
   * @return Proximity measurement value, ranging from 0 - far, to 2047 - close.
   */
  public int getProximity() {
    return colorSensor.getProximity();
  }

  public Color getFMSColor() {
    String gameData;
    gameData = DriverStation.getInstance().getGameSpecificMessage();
    if (gameData.length() > 0) {
      switch (gameData.charAt(0)) {
      case 'B':
        return Color.kFirstBlue;
      case 'G':
        return Color.kGreen;
      case 'R':
        return Color.kFirstRed;
      case 'Y':
        return Color.kYellow;
      default:
        DriverStationLogger.logErrorToDS("FMS COLOR DATA IS CORRUPT");
        return null;
      }
    } else {
      return null;
    }
  }

  /** @return True when the desired color is within two of the current color */
  public int calculateSpinDirection(Color currentColor, Color desiredColor) {
    int currentColorIndex = 0;
    int desiredColorIndex = 0;
    for (int i = 0; i >= Colors.values().length - 1; i++) {
      if (Colors.values()[i].getColor() == currentColor)
        currentColorIndex = i;
      if (Colors.values()[i].getColor() == desiredColor)
        desiredColorIndex = i;
    }

    // TODO: find when should turn right
    if (Colors.values()[currentColorIndex + 1].getColor() == Colors.values()[desiredColorIndex].getColor())
      return 1;
    else
      return -1;
  }

  private void createColors() {
    colorMatcher.addColorMatch(Color.kFirstRed);
    colorMatcher.addColorMatch(Color.kGreen);
    colorMatcher.addColorMatch(Color.kFirstBlue);
    colorMatcher.addColorMatch(Color.kYellow);
  }

  public enum Colors {
    Blue(Color.kFirstBlue), Yellow(Color.kYellow), Green(Color.kGreen), Red(Color.kFirstRed),
    SecondBlue(Color.kFirstBlue);

    private Color color;

    Colors(Color color) {
      this.color = color;
    }

    public Color getColor() {
      return color;
    }
  }
}
