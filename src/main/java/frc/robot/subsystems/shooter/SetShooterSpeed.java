package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import java.util.function.DoubleSupplier;

import static frc.robot.Robot.robotConstants;
import static frc.robot.Robot.shooter;


/**
 * This command spins the wheel in the desired velocity in order to shoot the power cells.
 */
public class SetShooterSpeed extends CommandBase {

    private static final int LOADED_CELLS_IN_AUTO = 3;
    private DoubleSupplier velocitySetpoint;
    private boolean isAuto;
    private double lastSetpoint;
    private boolean isInZone;
    private double lastTimeOutsideZone;
    private int cellsShot;

    /**
     * Constructs a shoot command with default RPM setpoint.
     * @see frc.robot.constants.RobotConstants.ShooterConstants#DEFAULT_RPM
     */
    public SetShooterSpeed() {
        this(robotConstants.shooterConstants.DEFAULT_RPM);
    }
    public SetShooterSpeed(boolean isAuto) {
        this();
        this.isAuto = isAuto;
    }

    /**
     * @param velocitySetpoint The setpoint used for calculation the PID velocity error in RPM.
     */
    public SetShooterSpeed(double velocitySetpoint) {
        this(() -> velocitySetpoint);
    }

    /**
     * @param velocitySetpoint The setpoint used for calculation the PID velocity error in RPM.
     * @param isAuto Whether the command is run in autonomous.
     */
    public SetShooterSpeed(double velocitySetpoint, boolean isAuto){
        this(velocitySetpoint);
        this.isAuto = isAuto;
    }

    public SetShooterSpeed(DoubleSupplier velocitySetpointSupplier) {
        addRequirements(shooter);
        velocitySetpoint = velocitySetpointSupplier;
    }

    @Override
    public void initialize() {
        lastSetpoint = velocitySetpoint.getAsDouble();
        shooter.startPID(lastSetpoint);
        cellsShot = 0;
        isInZone = true;
    }

    @Override
    public void execute() {
        double newSetpoint = velocitySetpoint.getAsDouble();
        if (lastSetpoint != newSetpoint) {
            shooter.startPID(newSetpoint);
            lastSetpoint = newSetpoint;
        }
        if (isAuto && !isInZone && shooter.getAverageSpeed() < robotConstants.shooterConstants.SHOOTING_BALL_ZONE &&
                Timer.getFPGATimestamp() - lastTimeOutsideZone > robotConstants.shooterConstants.WAIT_TIME_ZONE) {
            isInZone = true;
            cellsShot++;
        }
        if (isAuto && isInZone && shooter.getAverageSpeed() > robotConstants.shooterConstants.SHOOTING_BALL_ZONE) {
            isInZone = false;
            lastTimeOutsideZone = Timer.getFPGATimestamp();
        }
    }

    @Override
    public boolean isFinished() {
        return isAuto && cellsShot >= LOADED_CELLS_IN_AUTO;
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stopMove();
    }
}
