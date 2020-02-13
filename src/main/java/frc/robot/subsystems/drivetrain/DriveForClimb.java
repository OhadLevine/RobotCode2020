package frc.robot.subsystems.drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.utils.TrigonPIDController;
import java.util.function.DoubleSupplier;

import static frc.robot.Robot.drivetrain;
import static frc.robot.Robot.robotConstants;

/**
 * This command is used to move the drivetrain before
 * climbing so the robot will be located in the right spot
 */

public class DriveForClimb extends CommandBase {
    private TrigonPIDController pidController;
    private DoubleSupplier desiredDistance;

    /**
     * @param desiredDistance the desired distance the drivetrain will move.
     */
    public DriveForClimb(double desiredDistance) {
        this(() -> desiredDistance);
    }

    /**
     * @param desiredDistance the desired angle to turn by the gyro.
     */
    public DriveForClimb(DoubleSupplier desiredDistance) {
        addRequirements(drivetrain);
        this.desiredDistance = desiredDistance;
        pidController = new TrigonPIDController(robotConstants.controlConstants.driveForClimbSettings);
    }

    /**
     * Moves the drivetrain by encoders value. This constructor is used for tuning the PID
     * in the smartdashboard.
     */
    public DriveForClimb() {
        addRequirements(drivetrain);
        pidController = new TrigonPIDController("Drive For Climb Settings");
    }

    @Override
    public void initialize() {
        drivetrain.resetEncoders();
        pidController.reset();
    }

    @Override
    public void execute() {
        if (!pidController.isTuning())
            pidController.setSetpoint(desiredDistance.getAsDouble());
        drivetrain.move(pidController.calculate(drivetrain.getAverageDistance()));
    }

    @Override
    public boolean isFinished() {
        return pidController.atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.stopMove();
    }
}