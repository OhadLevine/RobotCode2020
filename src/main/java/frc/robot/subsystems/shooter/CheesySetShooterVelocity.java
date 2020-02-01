package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import io.github.oblarg.oblog.annotations.Log;

import java.util.function.DoubleSupplier;

import static frc.robot.Robot.robotConstants;
import static frc.robot.Robot.shooter;

/**
 * This command spins the wheel in the desired velocity in order to shoot the power cells.
 */
public class CheesySetShooterVelocity extends CommandBase {

    private static final int kLoadedCellsInAuto = 3;
    private static final int kMinimumKfSamples = 20;
    private DoubleSupplier velocitySetpoint;
    private CheesyShooterState currentShooterState;
    private boolean isAuto;
    private double setpoint;
    private double kfSamplesAmount;
    private double leftKfSamplesSum;
    private double rightKfSamplesSum;
    private double firstTimeOutsideZone;
    private double firstTimeInZone;
    private boolean isInZone;
    private int cellsShot;

    /**
     * Constructs a shoot command with default RPM setpoint.
     *
     * @see frc.robot.subsystems.shooter.ShooterVelocity#kDefault
     */
    public CheesySetShooterVelocity() {
        this(false);
    }

    /**
     * Constructs a shoot command with default RPM setpoint.
     *
     * @param isAuto whether the command should stop after shooting 3 cells
     * @see frc.robot.subsystems.shooter.ShooterVelocity#kDefault
     */
    public CheesySetShooterVelocity(boolean isAuto) {
        this(ShooterVelocity.kDefault, isAuto);
    }

    /**
     * @param velocity the velocity setpoint the talon fx will try to achieve.
     */
    public CheesySetShooterVelocity(ShooterVelocity velocity) {
        this(velocity, false);
    }

    /**
     * @param velocity the velocity setpoint the talon fx will try to achieve.
     * @param isAuto   whether the command should stop after shooting 3 cells
     */
    public CheesySetShooterVelocity(ShooterVelocity velocity, boolean isAuto) {
        this(velocity.getVelocity(), isAuto);
    }

    /**
     * @param velocitySetpoint The setpoint used for calculation the PID velocity error in RPM.
     */
    public CheesySetShooterVelocity(double velocitySetpoint) {
        this(velocitySetpoint, false);
    }

    /**
     * @param velocitySetpoint The setpoint used for calculation the PID velocity error in RPM.
     * @param isAuto           Whether the command is run in autonomous.
     */
    public CheesySetShooterVelocity(double velocitySetpoint, boolean isAuto) {
        this(() -> velocitySetpoint, isAuto);
    }

    public CheesySetShooterVelocity(DoubleSupplier velocitySetpointSupplier) {
        this(velocitySetpointSupplier, false);
    }

    public CheesySetShooterVelocity(DoubleSupplier velocitySetpointSupplier, boolean isAuto) {
        addRequirements(shooter);
        velocitySetpoint = velocitySetpointSupplier;
        this.isAuto = isAuto;
    }

    @Override
    public void initialize() {
        setpoint = velocitySetpoint.getAsDouble();
        currentShooterState = CheesyShooterState.SpinUp;
        kfSamplesAmount = 0;
        leftKfSamplesSum = 0;
        rightKfSamplesSum = 0;
        cellsShot = 0;
        firstTimeOutsideZone = 0;
        isInZone = true;
        shooter.setVelocity(setpoint);
    }

    @Override
    public void execute() {
        if (currentShooterState == CheesyShooterState.SpinUp)
            // reach target velocity in close loop control and calculate kF
            spinUpExecute();
        else if(currentShooterState == CheesyShooterState.HoldWhenReady)
            // set avarage calculated kF to both shooter sides and set kP, kI and kD values to 0; 
            setShooterToHoldMode();
        else
            // switch to open loop with calculated kF    
            holdExecute();
    }

    private void spinUpExecute() {
        if (Math.abs(getError()) < robotConstants.shooterConstants.kVelocityTolerance)
            updateKf();
        else {
            // reset kF since we are to far
            kfSamplesAmount = 0;
            leftKfSamplesSum = 0;
            rightKfSamplesSum = 0;
        }
        if (kMinimumKfSamples <= kfSamplesAmount) {
            currentShooterState = CheesyShooterState.HoldWhenReady;
        }
    }

    private void setShooterToHoldMode() {
        // set PID gains of the two sides of the shooter to zero  
        shooter.zeroPIDGains();
        // set feedforward gains of the two sides of the shooter to the calculated gains from SpinUp mode 
        shooter.configFeedforwardGains(leftKfSamplesSum / kfSamplesAmount, rightKfSamplesSum / kfSamplesAmount);
        currentShooterState = CheesyShooterState.Hold;
    }

    private void holdExecute() {
        // The shooter might heat up after extended use so we need to make sure to update kF if the shooter too much fast.
        if (shooter.getAverageVelocity() > setpoint) {
            updateKf();
            shooter.configFeedforwardGains(leftKfSamplesSum / kfSamplesAmount, rightKfSamplesSum / kfSamplesAmount);
        }
        // If in auto, check how many cells were shot.
        if (isAuto) {
            //boolean isCellBeingShot = shooter.isSwitchPressed();
            //We might want to use current in order to count the amount of shot cells instead of using limit switches
            //boolean isCellBeingShot = Math.abs(setpoint - shooter.getAverageSpeed()) < robotConstants.shooterConstants.kShootingBallZone;
            //countShotCells(isCellBeingShot);
        }
    }

    private void countShotCells(boolean isCellBeingShot) {
        if (!isInZone && isCellBeingShot &&
            Timer.getFPGATimestamp() - firstTimeOutsideZone > robotConstants.shooterConstants.kWaitTimeZone) {
            isInZone = true;
            firstTimeInZone = Timer.getFPGATimestamp();
            cellsShot++;
        } else if (isInZone && !isCellBeingShot &&
            Timer.getFPGATimestamp() - firstTimeInZone > robotConstants.shooterConstants.kWaitTimeZone) {
            isInZone = false;
            firstTimeOutsideZone = Timer.getFPGATimestamp();
        }
    }

    private void updateKf() {
        leftKfSamplesSum += shooter.estimateLeftKf();
        rightKfSamplesSum += shooter.estimateRightKf();
        kfSamplesAmount++;
    }

    @Override
    public boolean isFinished() {
        return isAuto && cellsShot >= kLoadedCellsInAuto;
    }

    @Override
    public void end(boolean interrupted) {
        if (!interrupted)
            shooter.stopMove();
        shooter.configPIDFGains();
    }

    /**
     * @return whether the shooter is ready to shoot cells.
     * Shooter must be in Hold mode (open loop shooting contorl) and on target velocity.
     * The velocity tolerance is taken from {@link frc.robot.constants.RobotConstants.ShooterConstants#kVelocityTolerance}
     */
    public boolean readyToShoot() { 
        return Math.abs(getError()) <
            robotConstants.controlConstants.leftShooterSettings.getTolerance() 
            && currentShooterState == CheesyShooterState.Hold;
    }

    public double getError() {
        if (shooter.isOverridden())
            return 0;
        return shooter.getAverageVelocity() - setpoint;
    }

    @Log (name = "Shooter/Current Cheesy Shooter State")
    private String logCurrentShooterState() {
        return currentShooterState.toString();
    }

    private enum CheesyShooterState {
        SpinUp, // PIDF to desired RPM
        HoldWhenReady, // calculate average kF
        Hold, // switch to pure kF control
    }
}

