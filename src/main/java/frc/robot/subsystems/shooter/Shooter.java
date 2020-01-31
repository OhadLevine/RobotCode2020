package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Units;
import frc.robot.subsystems.OverridableSubsystem;
import frc.robot.utils.DriverStationLogger;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

import static frc.robot.Robot.robotConstants;

/**
 * This subsystem handles shooting power cells into the outer and inner ports.
 */
public class Shooter extends OverridableSubsystem implements Loggable {
    private WPI_TalonFX leftTalonFX;
    private WPI_TalonFX rightTalonFX;
    private DigitalInput microSwitch;
    private boolean isTuning;

    public Shooter() {
        //setting up the talon fx
        leftTalonFX = new WPI_TalonFX(robotConstants.can.kLeftShooterTalonFX);
        leftTalonFX.setNeutralMode(NeutralMode.Coast);
        leftTalonFX.configClosedloopRamp(robotConstants.shooterConstants.kRampTime);
        leftTalonFX.configOpenloopRamp(robotConstants.shooterConstants.kRampTime);
        leftTalonFX.selectProfileSlot(0, 0);
        leftTalonFX.setInverted(robotConstants.shooterConstants.kIsLeftMotorInverted);
        leftTalonFX.setSensorPhase(robotConstants.shooterConstants.kIsLeftEncoderInverted);
        DriverStationLogger.logErrorToDS(leftTalonFX.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 0),
        "Could not set left shooter encoder");

        rightTalonFX = new WPI_TalonFX(robotConstants.can.kRightShooterTalonFX);
        rightTalonFX.setNeutralMode(NeutralMode.Coast);
        rightTalonFX.configClosedloopRamp(robotConstants.shooterConstants.kRampTime);
        rightTalonFX.configOpenloopRamp(robotConstants.shooterConstants.kRampTime);
        rightTalonFX.selectProfileSlot(0, 0);
        rightTalonFX.setInverted(robotConstants.shooterConstants.kIsRightMotorInverted);
        rightTalonFX.setSensorPhase(robotConstants.shooterConstants.kIsRightEncoderInverted);
        DriverStationLogger.logErrorToDS(rightTalonFX.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 0),
        "Could not set right shooter encoder");

        configPIDGains();
        //microSwitch = new DigitalInput(robotConstants.dio.kSwitchShooter);
        resetEncoders();
        overridden = false;
    }

    @Override
    public void overriddenMove(double power) {
        leftTalonFX.set(power);
        rightTalonFX.set(power);
    }

    public void setPower(double leftPower, double rightPower) {
        if (!overridden) {
            leftTalonFX.set(leftPower);
            rightTalonFX.set(rightPower);
        }
    }

    public void setLeftKf(double kF) {
        leftTalonFX.config_kF(0, kF);
    }

    public void turnOffPID() {
        leftTalonFX.config_kP(0, 0);
        leftTalonFX.config_kI(0, 0);
        leftTalonFX.config_kD(0, 0);
    }

    public void setDefaultVelocity() {
        setVelocity(ShooterVelocity.kDefault.getVelocity());
    }

    /**
     * Starts using velocity PID instead of open-loop.
     *
     * @param velocitySetpoint velocity to set the talons in RPM.
     */
    public void setVelocity(ShooterVelocity velocitySetpoint) {
        setVelocity(velocitySetpoint.getVelocity());
    }

    /**
     * Starts using velocity PID instead of open-loop.
     *
     * @param velocitySetpoint velocity to set the talons in RPM.
     */
    public void setVelocity(double velocitySetpoint) {
        if (overridden)
            return;
        double leftVelocityInTalonUnits = velocitySetpoint * robotConstants.shooterConstants.kLeftUnitsPerRotation
            / 600;
        double rightVelocityInTalonUnits = velocitySetpoint * robotConstants.shooterConstants.kRightUnitsPerRotation
            / 600;
        leftTalonFX.set(TalonFXControlMode.Velocity, leftVelocityInTalonUnits);
        rightTalonFX.set(TalonFXControlMode.Velocity, rightVelocityInTalonUnits);
    }

    public double getLeftVoltage() {
        return leftTalonFX.getMotorOutputVoltage();
    }

    public double getRightVoltage() {
        return rightTalonFX.getMotorOutputVoltage();
    }

    // @Log(name = "Shooter/Is Switch Pressed")
    // public boolean isSwitchPressed() {
    //     return microSwitch.get();
    // }

    public void enableTuning() {
        DriverStationLogger.logToDS("Shooter tuning enabled");
        isTuning = true;
        // left shooter gains
        SmartDashboard.putData("PID/Left Shooter Settings", robotConstants.controlConstants.leftShooterSettings);
        // right shooter gains
        SmartDashboard.putData("PID/Right Shooter Settings", robotConstants.controlConstants.rightShooterSettings);
    }

    public void disableTuning() {
        isTuning = false;
    }

    @Log(name = "Shooter/Left Ticks")
    public int getLeftTicks() {
        return leftTalonFX.getSelectedSensorPosition();
    }

    @Log(name = "Shooter/Right Ticks")
    public int getRightTicks() {
        return rightTalonFX.getSelectedSensorPosition();
    }

    /**
     * @return the speed of the left shooter in RPM.
     */
    @Log(name = "Shooter/Left Speed")
    public double getLeftSpeed() {
        return leftTalonFX.getSelectedSensorVelocity() * 600.0
            / robotConstants.shooterConstants.kLeftUnitsPerRotation;
    }

    /**
     * @return the speed of the right shooter in RPM.
     */
    @Log(name = "Shooter/Right Speed")
    public double getRightSpeed() {
        return rightTalonFX.getSelectedSensorVelocity() * 600.0
            / robotConstants.shooterConstants.kRightUnitsPerRotation;
    }

    /**
     * @return the speed of the shooter in RPM.
     */
    @Log(name = "Shooter/Average Speed")
    public double getAverageSpeed() {
        return (getLeftSpeed() + getRightSpeed()) / 2;
    }

    public void resetEncoders() {
        leftTalonFX.setSelectedSensorPosition(0);
        rightTalonFX.setSelectedSensorPosition(0);
    }

    @Override
    public void periodic() {
        if (isTuning) configPIDGains();
    }

    private void configPIDGains() {
        leftTalonFX.config_kP(0, robotConstants.controlConstants.leftShooterSettings.getKP());
        leftTalonFX.config_kI(0, robotConstants.controlConstants.leftShooterSettings.getKI());
        leftTalonFX.config_kD(0, robotConstants.controlConstants.leftShooterSettings.getKD());
        leftTalonFX.config_kF(0, robotConstants.controlConstants.leftShooterSettings.getKF());
        rightTalonFX.config_kP(0, robotConstants.controlConstants.rightShooterSettings.getKP());
        rightTalonFX.config_kI(0, robotConstants.controlConstants.rightShooterSettings.getKI());
        rightTalonFX.config_kD(0, robotConstants.controlConstants.rightShooterSettings.getKD());
        rightTalonFX.config_kF(0, robotConstants.controlConstants.rightShooterSettings.getKF());
    }

    /**
     * This calculation was taken from team 254 2017 robot code.
     *
     * @param rpm     current velocity in rotations per minute
     * @param voltage the current voltage
     * @return kf estimated by to be used with talonFX
     */
    public static double estimateKf(double rpm, double voltage) { //TODO: set magic nums to constants 
        final double speed_in_ticks_per_100ms = 2048.0 / 600.0 * rpm;
        final double output = 1023.0 / 12.0 * voltage; // TODO: change calculation
        return output / speed_in_ticks_per_100ms;
    }

    /**
     * @param RPM revolutions per minute
     * @return velocity in meters per second
     */
    public static double rpmToMeterPerSecond(double RPM) {
        return Units.rotationsPerMinuteToRadiansPerSecond(RPM) * robotConstants.shooterConstants.kWheelRadius;
    }

    /**
     * @param meterPerSecond speed to be converted
     * @return velocity in revolution per minute
     */
    public static double meterPerSecondToRPM(double meterPerSecond) {
        return Units.radiansPerSecondToRotationsPerMinute(meterPerSecond / robotConstants.shooterConstants.kWheelRadius);
    }
}
