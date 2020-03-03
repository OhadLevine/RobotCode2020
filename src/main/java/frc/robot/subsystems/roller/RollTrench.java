package frc.robot.subsystems.roller;

import edu.wpi.first.wpilibj2.command.CommandBase;

import static frc.robot.Robot.roller;

public class RollTrench extends CommandBase {
  /**
   * Spins the trench to a specific color or for a specific time.
   */
  public RollTrench() {
    addRequirements(roller);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
