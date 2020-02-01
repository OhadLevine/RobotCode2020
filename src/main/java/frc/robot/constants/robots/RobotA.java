package frc.robot.constants.robots;

import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import frc.robot.constants.RobotConstants;
import frc.robot.utils.PIDSettings;

/**
 * Constants and robot map for robot A.
 */
public class RobotA extends RobotConstants {
    // TODO: Set constants

    public RobotA() {
        /* Robot Constants */
        // Drivetrain Constants
        drivetrainConstants.kWheelDiameter = 0;
        drivetrainConstants.kWheelBaseWidth = 0;
        drivetrainConstants.kRobotLength = 0;
        drivetrainConstants.kRobotWidth = 0;
        drivetrainConstants.kLeftEncoderTicksPerMeter = 1;
        drivetrainConstants.kRightEncoderTicksPerMeter = 1;
        drivetrainConstants.kRampRate = 1;
        drivetrainConstants.kCurrentLimit = 1;
        drivetrainConstants.kTriggerThresholdCurrent = 1;
        drivetrainConstants.kTriggerThresholdTime = 1;

        // Trigon Drive Constants
        trigonDriveConstants.kSensitivity = 1;
        trigonDriveConstants.kThreshold = 0.5;

        // Intake Constants
        intakeConstants.kIntakeReversed = false;
        intakeConstants.kIntakeDefaultPower = 0.3;

        // Mixer Constants
        mixerConstants.kMixerMaxStall = 30;
        mixerConstants.kIsInverted = false;
        mixerConstants.kRampUpTime = 0;
        mixerConstants.kStallWaitTime = 0.2;
        mixerConstants.kBackwardsSpinTime = 0.2;
        mixerConstants.kDefaultPower = 0.5;

        // Climb Constants
        climbConstants.kHookCurrentLimit = 0;
        climbConstants.kHookThresholdLimit = 0;
        climbConstants.kHookCurrentTimeout = 0;
        climbConstants.kClimbCurrentLimit = 0;

        // Shooter Constants
        shooterConstants.kRampTime = 0.25;
        shooterConstants.kWheelRadius = 1;
        shooterConstants.kLeftUnitsPerRotation = 2048;
        shooterConstants.kRightUnitsPerRotation = 2048;
        shooterConstants.kShootingBallZone = 2000;
        shooterConstants.kWaitTimeZone = 0;
        shooterConstants.kIsLeftMotorInverted = false;
        shooterConstants.kIsRightMotorInverted = true;
        shooterConstants.kIsLeftEncoderInverted = false;
        shooterConstants.kIsRightEncoderInverted = true;
        shooterConstants.kStopLoadingTolerance = 10;
        shooterConstants.kVelocityTolerance = 2;
        controlConstants.leftShooterSettings = PIDSettings.fromTalonSettings(0.083, 0.0000325, 1.95, 0.04696, 2);
        controlConstants.rightShooterSettings = PIDSettings.fromTalonSettings(0, 0, 0, 0, 0);

        // Loader Constants
        loaderConstants.kRampRate = 0;
        loaderConstants.kCurrentLimit = 0;
        loaderConstants.kThresholdLimit = 0;
        loaderConstants.kTimeout = 0;
        loaderConstants.kTicksPerRotation = 1;
        loaderConstants.kDefaultVelocity = 0.5;
        loaderConstants.kStallLimit = 20;
        loaderConstants.kSpinBackwardsTime = 1;
        loaderConstants.kDefaultBackwardsPower = -0.2;
        loaderConstants.kOnStallPower = -0.3;
        controlConstants.loaderFeedforward = new SimpleMotorFeedforward(0, 0, 0);
        controlConstants.loaderPidSettings = new PIDSettings(0, 0, 0, 0, 0);

        // Motion Profiling Constants
        motionProfilingConstants.kMaxVelocity = 0;
        motionProfilingConstants.kMaxAcceleration = 0;
        motionProfilingConstants.kMaxCentripetalAcceleration = 0;
        motionProfilingConstants.kP = 0;
        motionProfilingConstants.kReverseKp = 0;
        controlConstants.motionProfilingSettings = new SimpleMotorFeedforward(0, 0, 0);
        controlConstants.motionProfilingReverseSettings = new SimpleMotorFeedforward(0, 0, 0);

        // Vision Constants
        visionConstants.kDistanceFromPortACoefficient = 0;
        visionConstants.kDistanceFromPortBCoefficient = 0;
        visionConstants.kDistanceFromFeederACoefficient = 0;
        visionConstants.kDistanceFromFeederBCoefficient = 0;
        visionConstants.kLimelightOffsetX = 0;
        visionConstants.kLimelightOffsetY = 0;
        visionConstants.kLimelightAngleOffset = 0;
        visionConstants.kTargetNotFoundWaitTime = 0.1;
        controlConstants.visionDistanceSettings = new PIDSettings(0, 0, 0, 0, 0);
        controlConstants.visionRotationSettings = new PIDSettings(0, 0, 0, 0, 0);
        controlConstants.drivetrainRotateSettings = new PIDSettings(0, 0, 0, 0, 0);

        /* Robot Map */
        // Drivetrain Map
        can.kDrivetrainLeftFrontTalonFX = 1;
        can.kDrivetrainLeftMiddleTalonFX = 2;
        can.kDrivetrainLeftRearTalonFX = 3;
        can.kDrivetrainRightFrontTalonFX = 4;
        can.kDrivetrainRightMiddleTalonFX = 5;
        can.kDrivetrainRightRearTalonFX = 6;
        can.kTemporaryTalonForLeftDrivetrainEncoder = 7;
        can.kTemporaryTalonForRightDrivetrainEncoder = 8;
        // Intake Map
        can.kIntakeSparkMax = 11;
        // Mixer Map
        can.kMixerTalonSRX = 12;
        // Loader Map
        can.kLoaderTalonSRX = 15;
        // Shooter Map
        can.kLeftShooterTalonFX = 4;
        can.kRightShooterTalonFX = 3;
        dio.kSwitchShooter = 0;
        // Climb Map
        can.kHookTalonSRX = 13;
        can.kClimbSparkMax = 14;
        // PWM Map
        pwm.kLedController = 0;
        // I2C Port
        i2c.kI2cPort = Port.kOnboard;
    }
}
