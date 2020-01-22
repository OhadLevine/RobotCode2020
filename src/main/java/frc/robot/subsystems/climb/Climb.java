package frc.robot.subsystems.climb;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
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
    hookTalonSRX = new WPI_TalonSRX(robotConstants.can.HOOK_TALON_SRX);
    climbSparkMax = new CANSparkMax(robotConstants.can.CLIMB_SPARK_MAX, MotorType.kBrushless);

    hookTalonSRX.configSupplyCurrentLimit(
      new SupplyCurrentLimitConfiguration(true, robotConstants.climbConstants.HOOK_CURRENT_LIMIT,
      robotConstants.climbConstants.HOOK_THRESHOLD_LIMIT, robotConstants.climbConstants.HOOK_CURRENT_TIMEOUT));
    
    climbSparkMax.setSmartCurrentLimit(robotConstants.climbConstants.CLIMB_CURRENT_LIMIT);
    
    hookTalonSRX.setNeutralMode(NeutralMode.Coast);    
    climbSparkMax.setIdleMode(IdleMode.kCoast);

    climbSparkMax.burnFlash();
  }

  public void setHookPower(double power) {
    hookTalonSRX.set(power);
  }

  public void setClimbPower(double power) {
    climbSparkMax.set(power);
  }
}
