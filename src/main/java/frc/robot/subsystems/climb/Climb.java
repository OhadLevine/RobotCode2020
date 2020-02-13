package frc.robot.subsystems.climb;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Robot.robotConstants;

public class Climb extends SubsystemBase {
    private WPI_TalonSRX hookTalonSRX;
    private CANSparkMax climbSparkMax;

    /**
     * The climb holds all the methods used for the robots climb in the endgame.
     * Climb is the system that pulls the rope to make the robot levitate. Hook is
     * the system that extends to hang on the climb.
     */
    public Climb() {
        hookTalonSRX = new WPI_TalonSRX(robotConstants.can.kHookTalonSRX);
        climbSparkMax = new CANSparkMax(robotConstants.can.kClimbSparkMax, MotorType.kBrushless);

        hookTalonSRX.configSupplyCurrentLimit(
            new SupplyCurrentLimitConfiguration(false, robotConstants.climbConstants.kHookCurrentLimit,
                robotConstants.climbConstants.kHookThresholdLimit, robotConstants.climbConstants.kHookCurrentTimeout));

        //climbSparkMax.setSmartCurrentLimit(robotConstants.climbConstants.kClimbCurrentLimit);
        climbSparkMax.getEncoder(EncoderType.kNoSensor, 0);

        hookTalonSRX.setInverted(robotConstants.climbConstants.kIsHookInverted);
        climbSparkMax.setInverted(robotConstants.climbConstants.kIsClimbInverted);

        hookTalonSRX.setNeutralMode(NeutralMode.Coast);
        climbSparkMax.setIdleMode(IdleMode.kCoast);

        climbSparkMax.burnFlash();
    }

    public void setHookPower(double power) {
        hookTalonSRX.set(power);
    }

    /** @param power should be only positive or zero, otherwise, zero power is applied. 
     * The climb can only rotate in one direction because of ratchet connected to its transmission. 
     */
    public void setClimbPower(double power) {
        climbSparkMax.set(power >= 0 ? power : 0);
    }

    /** Used for right drivetrain encoder */
    public WPI_TalonSRX getHookTalonSRXInstance() {
        return hookTalonSRX;
    }
}
