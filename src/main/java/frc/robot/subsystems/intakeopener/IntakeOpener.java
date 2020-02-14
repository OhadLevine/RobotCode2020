package frc.robot.subsystems.intakeopener;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.OverridableSubsystem;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

import static frc.robot.Robot.robotConstants;

/**
 * This subsystem is responsible for opening the intake and closing it.
 */
public class IntakeOpener extends OverridableSubsystem implements Loggable {
    private final WPI_TalonSRX talonSRX;
    private final AnalogPotentiometer potentiometer;
    private double velocity;
    private double lastPosition;
    private double lastTimestamp;

    public IntakeOpener() {
        talonSRX = new WPI_TalonSRX(robotConstants.can.kIntakeOpenerTalonSRX);
        talonSRX.setInverted(robotConstants.intakeOpenerConstants.kIsInverted);
        talonSRX.setNeutralMode(NeutralMode.Brake);
        talonSRX.configSupplyCurrentLimit(
            new SupplyCurrentLimitConfiguration(false, robotConstants.intakeOpenerConstants.kCurrentLimit,
                robotConstants.intakeOpenerConstants.kThresholdLimit,
                robotConstants.intakeOpenerConstants.kTriggerThresholdTime));
        potentiometer = new AnalogPotentiometer(robotConstants.analogInput.kIntakeOpenerPotentiometer,
            robotConstants.intakeOpenerConstants.kPotentiometerAngleMultiplier,
            robotConstants.intakeOpenerConstants.kPotentiometerOffset);
    }

    @Override
    public void overriddenMove(double power) {
        talonSRX.set(power);
    }

    @Override
    public void move(double power) {
        if ((getAngle() >= robotConstants.intakeOpenerConstants.kOpenAngle && power > 0)
            || (getAngle() <= robotConstants.intakeOpenerConstants.kClosedAngle && power < 0))
            super.move(0);
        else if (!isOverridden())
            super.move(power);
    }

    /**
     * Moves the intakeOpener motor.
     */
    public void setIntakeOpenerVoltage(double voltage) {
        move(voltage / RobotController.getBatteryVoltage());
    }

    public double getMotorOutputVoltage() {
        return talonSRX.getMotorOutputVoltage();
    }

    /** @return The angle of the potentiometer parallel to the floor. */
    @Log(name = "IntakeOpener/Angle")
    public double getAngle() {
        return -(potentiometer.get() - 600);
    }


    public boolean isAtGoal() {
        return true;
    }

    @Override
    public void periodic() {
        final double newTimestamp = Timer.getFPGATimestamp();
        final double dt = newTimestamp - lastTimestamp;
        final double newPosition = getAngle();
        velocity = (newPosition - lastPosition) / dt;
        lastPosition = newPosition;
        lastTimestamp = newTimestamp;
    }

    public WPI_TalonSRX getTalonSRX() {
        return talonSRX;
    }
}
