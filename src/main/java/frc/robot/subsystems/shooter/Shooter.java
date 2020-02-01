package frc.robot.subsystems.shooter;

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

        configPIDFGains();
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
    @Log(name = "Shooter/Left Velocity")
    public double getLeftVelocity() {
        return leftTalonFX.getSelectedSensorVelocity() * 600.0
            / robotConstants.shooterConstants.kLeftUnitsPerRotation;
    }

    /**
     * @return the velocity of the right shooter in RPM.
     */
    @Log(name = "Shooter/Right Velocity")
    public double getRightVelocity() {
        return rightTalonFX.getSelectedSensorVelocity() * 600.0
            / robotConstants.shooterConstants.kRightUnitsPerRotation;
    }

    /**
     * @return the velocity of the shooter in RPM.
     */
    @Log(name = "Shooter/Average Velocity")
    public double getAverageVelocity() {
        return (getLeftVelocity() + getRightVelocity()) / 2;
    }

    /**
     * This calculation was taken from team 254 2017 robot code.
     * 
     * @return kf of the left talonFX shooter estimated by it's current rpm and voltage 
     */
    public double estimateLeftKf() {
        final double speed_in_ticks_per_100ms = robotConstants.shooterConstants.kLeftUnitsPerRotation / 600.0 * getLeftVelocity();
        final double output = 1023.0 / 12.0 * getLeftVoltage();
        return output / speed_in_ticks_per_100ms;
    }

    /**
     * This calculation was taken from team 254 2017 robot code.
     * 
     * @return kf of the right talonFX shooter estimated by it's current rpm and voltage
     */
    public double estimateRightKf() {
        final double speed_in_ticks_per_100ms = robotConstants.shooterConstants.kRightUnitsPerRotation / 600.0 * getRightVelocity();
        final double output = 1023.0 / 12.0 * getRightVoltage();
        return output / speed_in_ticks_per_100ms;
    }

    public void resetEncoders() {
        leftTalonFX.setSelectedSensorPosition(0);
        rightTalonFX.setSelectedSensorPosition(0);
    }

    public void configPIDFGains() {
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
     * set the feedforward Gains of the shooter
     * 
     * @param leftKf left feedforward gain
     * @param rightKf right feedforward gain
     */
    public void configFeedforwardGains(double leftKf, double rightKf) {
        leftTalonFX.config_kF(0, leftKf);
        rightTalonFX.config_kF(0, rightKf);
    }

    /** set PID gains of the two sides of the shooter to zero.
     * This method is used for open loop control,
     * when we want only kF to affect the shooter velocity. 
     */
    public void zeroPIDGains() {
        leftTalonFX.config_kP(0, 0);
        leftTalonFX.config_kI(0, 0);
        leftTalonFX.config_kD(0, 0);
        rightTalonFX.config_kP(0, 0);
        rightTalonFX.config_kI(0, 0);
        rightTalonFX.config_kD(0, 0);
    }

    @Override
    public void periodic() {
        if (isTuning) configPIDFGains();
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
