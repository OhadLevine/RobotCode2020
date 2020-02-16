package frc.robot.subsystems.led;

import edu.wpi.first.wpilibj2.command.CommandBase;

import static frc.robot.Robot.*;

public class SetLEDState extends CommandBase {
  private final static int kBlinkingTime = 3000;

  /**
   * Changes the color of the LED's according to different states in the robot.
   */
  public SetLEDState() {
    addRequirements(led);
  }

  @Override
  public void initialize() {
    led.setColor(LEDColor.Green);
  }

  @Override
  public void execute() {
    // Check the robots states and change the led colors accordingly
    if (drivetrain.getLeftMotorOutputVoltage() >= 0 || drivetrain.getRightMotorOutputVoltage() >= 0) {
      led.blinkColor(LEDColor.Green, kBlinkingTime);
    } else if (drivetrain.getLeftMotorOutputVoltage() <= 0) {
      led.blinkColor(LEDColor.Red, kBlinkingTime);
    } else if (shooter.getAverageSpeed() != 0) {
      led.blinkColor(LEDColor.Gold, kBlinkingTime);
    } else if (shooter.isSwitchPressed()) {
      led.setColor(LEDColor.Blue);
    } else if (mixer.isInStall()) {
      led.setColor(LEDColor.Random);
    } else if (climb.getClimbPower() != 0 || climb.getHookPower() != 0) {
      led.setColor(LEDColor.Blue);
    } else {
      led.setColor(LEDColor.White);
      System.out.println("You Missed a Robot state!!! Please add this specific state!");
    }
  }

  @Override
  public void end(boolean interrupted) {
    led.turnOffLED();
  }
}
