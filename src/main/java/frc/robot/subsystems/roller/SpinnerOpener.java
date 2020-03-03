package frc.robot.subsystems.roller;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SpinnerOpener extends SubsystemBase {
  private final WPI_TalonSRX talonSRX;
  private AnalogPotentiometer potentiometer;
  /**
   * This opens and closes the Spinner SS so that it can go though the trench or
   * spin the control panel
   */
  public SpinnerOpener() {

  }

  @Override
  public void periodic() {
  }
}
