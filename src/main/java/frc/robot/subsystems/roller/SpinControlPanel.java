package frc.robot.subsystems.roller;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.constants.RobotConstants.SpinnerConstants;

import static frc.robot.Robot.spinner;

public class SpinControlPanel extends CommandBase {
  private Color startingColor;
  private double timesOnColor;
  private double timesToSeeColor;
  private boolean seenColor;

  /**
   * Spins the Control Panel for a total of three and a half times.
   */
  public SpinControlPanel() {
    this(3.5);
  }

  /**
   * Spins the Control Panel for a specified amount of times.
   */
  public SpinControlPanel(double amountOfSpins) {
    addRequirements(spinner);
    startingColor = spinner.getColor();
    timesToSeeColor = amountOfSpins * 2;
    seenColor = false;
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    spinner.move(SpinnerConstants.kDefaultSpeed);
    if (spinner.isOnColor(startingColor) && seenColor) {
      timesOnColor++;
      seenColor = false;
    } else {
      seenColor = !spinner.isOnColor(startingColor);
    }
  }

  @Override
  public void end(boolean interrupted) {
    spinner.stopMove();
  }

  @Override
  public boolean isFinished() {
    return timesOnColor >= timesToSeeColor;
  }
}
