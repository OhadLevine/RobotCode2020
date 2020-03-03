package frc.robot.subsystems.roller;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.util.Color;
import frc.robot.constants.RobotMap;
import frc.robot.subsystems.OverridableSubsystem;
import frc.robot.constants.RobotConstants.RollerConstants;

public class Roller extends OverridableSubsystem {
  private final WPI_TalonSRX talonSRX;
  private final ColorSensorV3 colorSensor;
  ColorMatch colorMatch;
  /**
   * This class holds all of the methods for the trench roller subsystem, which
   * spins the trench.
   */
  public Roller() {
    talonSRX = new WPI_TalonSRX(RobotMap.kRollerTalonSRX);
    talonSRX.setNeutralMode(NeutralMode.Coast);
    talonSRX.setInverted(RollerConstants.kIsInverted);
    talonSRX.configOpenloopRamp(RollerConstants.kRampRate);
    talonSRX.configClosedloopRamp(RollerConstants.kRampRate);

    colorSensor = new ColorSensorV3(RobotMap.kI2cPort);
    colorMatch = new ColorMatch();
    colorMatch.addColorMatch(new Color(0.0, 0.0, 0.0));
    ColorMatchResult match = colorMatch.matchClosestColor(colorSensor.getColor());
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
    colorMatch.addColorMatch(desiredColor);
    return true;
  }

  /**
   * @return Proximity measurement value, ranging from 0 - far, to 2047 - close.
   */
  public int getProximity() {
    return colorSensor.getProximity();
  }

  //TODO: write calibration function
  public void calibrateColors() {

  }
}
