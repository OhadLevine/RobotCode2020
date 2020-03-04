package frc.robot.subsystems.spinner;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.constants.RobotConstants.SpinnerConstants;
import frc.robot.utils.DriverStationLogger;

import static frc.robot.Robot.spinner;

public class SpinPanelByColor extends CommandBase {
  private Color setpoint;
  private Color startingColor;
  private int spinDirection;
  private boolean startedOnSetpoint;

  /**
   * Spins the Control Panel to a specified color for stage three.
   */
  public SpinPanelByColor() {
    addRequirements(spinner);
  }

  @Override
  public void initialize() {
    setpoint = spinner.getFMSColor();
    if (setpoint != null) {
      startingColor = spinner.getColor();
      spinDirection = spinner.calculateSpinDirection(startingColor, setpoint);
      startedOnSetpoint = spinner.compareColors(startingColor, setpoint);
    } else
      DriverStationLogger.logErrorToDS("Did not get any color so cannot run SpinPanelByColor");
  }

  @Override
  public void execute() {
    if (setpoint != null) {
      spinner.move(SpinnerConstants.kCloseToTargetSpeed * spinDirection);

      if (!spinner.isOnColor(startingColor))
        startedOnSetpoint = false;
    }
  }

  @Override
  public void end(boolean interrupted) {
    spinner.stopMove();
  }

  @Override
  public boolean isFinished() {
    return spinner.isOnColor(setpoint) && !startedOnSetpoint || setpoint == null;
  }
}
