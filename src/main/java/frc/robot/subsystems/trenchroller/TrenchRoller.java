package frc.robot.subsystems.trenchroller;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.robot.constants.RobotMap;
import frc.robot.subsystems.OverridableSubsystem;
import frc.robot.constants.RobotConstants.TrenchRollerConstants;

public class TrenchRoller extends OverridableSubsystem{
  private WPI_TalonSRX talonSRX;
  /**
   * This class holds all of the methods for the trench roller subsystem, which
   * spins the trench.
   */
  public TrenchRoller() {
    talonSRX = new WPI_TalonSRX(RobotMap.kTrenchRollerTalonSRX);
    talonSRX.setNeutralMode(NeutralMode.Coast);
    talonSRX.setInverted(TrenchRollerConstants.kIsInverted);
  }

  @Override
  public void overriddenMove(double power) {
    talonSRX.set(power);
  }
}
