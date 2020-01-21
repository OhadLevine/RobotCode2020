package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.MoveableSubsystem;

public class MoveMovableSubsystem extends CommandBase {
  private MoveableSubsystem subsystem;
  private BooleanSupplier isFinished;
  private DoubleSupplier power;

  /**
   * This command can move any subsystem that implements MovableSubsystem and runs
   * forever.
   */
  public MoveMovableSubsystem(MoveableSubsystem subsystem, DoubleSupplier power) {
    this(subsystem, power, () -> false);
  }

  /**
   * This command can move any subsystem that implements MovableSubsystem, it uses
   * isFinished supplier is checked in the isFinished method for checking if the
   * command is done.
   */
  public MoveMovableSubsystem(MoveableSubsystem subsystem, DoubleSupplier power, BooleanSupplier isFinished) {
    this.subsystem = subsystem;
    this.power = power;
    this.isFinished = isFinished;
    addRequirements(subsystem);
  }

  @Override
  public void execute() {
    subsystem.move(power.getAsDouble());
  }

  @Override
  public void end(boolean interrupted) {
    subsystem.stopMove();
  }

  @Override
  public boolean isFinished() {
    return isFinished.getAsBoolean();
  }
}