package frc.robot.subsystems.led;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.utils.DriverStationLogger;

import static frc.robot.Robot.*;

public class SetLEDState extends CommandBase {
  private final static int kBlinkingAmount = 3000;

  /**
   * Changes the color of the LED's according to different states in the robot.
   */
  public SetLEDState() {
    addRequirements(led);
  }

  @Override
  public void execute() {
    // Check the robots states and change the led colors accordingly
    if (climb.getClimbPower() != 0 || climb.getHookPower() != 0) {
      led.setColor(LEDColor.Blue);
    } else if (mixer.isInStall()) {
      led.setColor(LEDColor.Random);
      DriverStationLogger.logToDS("Mixer is in stall!!!");
    } else if (intake.getIsInStall()) {
      led.setColor(LEDColor.Random);
      DriverStationLogger.logToDS("Intake is in stall!!!");
    } else if (loader.getIsInStall()) {
      led.setColor(LEDColor.Random);
      DriverStationLogger.logToDS("Loader is in stall!!!");
    } else if (shooter.isSwitchPressed()) {
      led.setColor(LEDColor.Blue);
    } else if (shooter.getAverageSpeed() != 0) {
      led.blinkColor(LEDColor.Gold, kBlinkingAmount);
    } else if (intake.getOutputCurrent() != 0) {
      led.blinkColor(LEDColor.Yellow, kBlinkingAmount);
    } else if (robotConstants.visionConstants.isFollowingTarget) {
      led.setColor(LEDColor.Orange);
    } else if (drivetrain.getLeftMotorOutputVoltage() <= 0) {
      led.blinkColor(LEDColor.Red, kBlinkingAmount);
    } else if (drivetrain.getLeftMotorOutputVoltage() >= 0 || drivetrain.getRightMotorOutputVoltage() >= 0) {
      led.blinkColor(LEDColor.Green, kBlinkingAmount);
    } else {
      led.setColor(LEDColor.White);
      DriverStationLogger.logToDS("You missed a robot state!!!");
    }
  }

  @Override
  public void end(boolean interrupted) {
    led.turnOffLED();
  }
}