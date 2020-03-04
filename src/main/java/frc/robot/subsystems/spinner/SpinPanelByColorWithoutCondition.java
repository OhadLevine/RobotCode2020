package frc.robot.subsystems.spinner;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.constants.RobotConstants.SpinnerConstants;
import frc.robot.utils.FMSColor;

import static frc.robot.Robot.spinner;

public class SpinPanelByColorWithoutCondition extends CommandBase {
  private Color setpoint;
  private Color startingColor;
  private int spinDirection;

  /**
   * Spins the Control Panel to a specified color for stage three.
   */
  public SpinPanelByColorWithoutCondition() {
    addRequirements(spinner);
  }

  @Override
  public void initialize() {
    setpoint = FMSColor.getFMSColor();
    startingColor = spinner.getColor();
    spinDirection = spinner.calculateSpinDirection(startingColor, setpoint);
  }

  @Override
  public void execute() {
    spinner.move(SpinnerConstants.kCloseToTargetSpeed * spinDirection);
  }

  @Override
  public boolean isFinished() {
    return spinner.isOnColor(setpoint);
  }

  @Override
  public void end(boolean interrupted) {
    spinner.stopMove();
  }

}
