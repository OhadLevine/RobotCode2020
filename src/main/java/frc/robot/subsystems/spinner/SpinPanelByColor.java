package frc.robot.subsystems.spinner;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.constants.RobotConstants.SpinnerConstants;
import frc.robot.utils.DriverStationLogger;

import static frc.robot.Robot.spinner;

public class SpinPanelByColor extends CommandBase {
  private final Color setpoint; 
  private final Color startingColor;
  private final int spinDirection;
  private boolean startedOnSetpoint;
  /**
   * Spins the Control Panel to a specified color by the fms for stage three.
   */
  public SpinPanelByColor() {
    this(spinner.getFMSColor());
  }

  /**
   * Spins the Control Panel to a specified color for stage three.
   */
  public SpinPanelByColor(Color colorSetpoint) {
    addRequirements(spinner);
    setpoint = colorSetpoint;
    if (setpoint == null) {
      DriverStationLogger.logErrorToDS("Did not get any color so cannot run SpinPanelByColor");
      end(true);
    }
    startingColor = spinner.getColor();
    spinDirection = spinner.calculateSpinDirection(startingColor, setpoint);

    if (startingColor == setpoint) startedOnSetpoint = true;
    else startedOnSetpoint = false;
  }

  @Override
  public void execute() {
    spinner.move(SpinnerConstants.kCloseToTargetSpeed * spinDirection);

    if (!spinner.isOnColor(setpoint )) startedOnSetpoint = false;
  }

  @Override
  public void end(boolean interrupted) {
    spinner.stopMove();
  }

  @Override
  public boolean isFinished() {
    return spinner.isOnColor(setpoint) & !startedOnSetpoint;
  }
}
