package frc.robot.subsystems.intakeopener;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.utils.TrigonPIDController;
import java.util.function.DoubleSupplier;

import static frc.robot.Robot.intakeOpener;
import static frc.robot.Robot.robotConstants;

public class OpenIntake extends CommandBase {
    private DoubleSupplier angleSupplier;
    private TrigonPIDController pidController;

    /**
     * Either opens the Intake subsystem or closes it with PID
     *
     * @param isOpen true - opens the Intake, false - closes it.
     */
    public static CommandBase openIntake(boolean isOpen) {
        if (isOpen)
            return new OpenIntake(() -> robotConstants.intakeOpenerConstants.kOpenAngle).
                withTimeout(robotConstants.intakeOpenerConstants.kTimeout);
        else
            return new CloseIntake(() -> robotConstants.intakeOpenerConstants.kClosedAngle).
                withTimeout(robotConstants.intakeOpenerConstants.kTimeout);
    }

    /**
     * Either opens the Intake subsystem or closes it with PID.
     */
    public OpenIntake(DoubleSupplier angleSupplier) {
        addRequirements(intakeOpener);
        pidController = new TrigonPIDController(robotConstants.controlConstants.intakeOpenerSettings);
        this.angleSupplier = angleSupplier;
    }

    /**
     * Constructs Open Intake with PID tuning
     */
    public OpenIntake() {
        addRequirements(intakeOpener);
        pidController = new TrigonPIDController("Intake Opener");
    }

    @Override
    public void initialize() {
        pidController.reset();
    }

    @Override
    public void execute() {
        if (!pidController.isTuning())
            pidController.setSetpoint(angleSupplier.getAsDouble());
        intakeOpener.move(pidController.calculate(intakeOpener.getAngle(), -1, 1));
    }

    @Override
    public boolean isFinished() {
        return pidController.atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        intakeOpener.stopMove();
    }
}
