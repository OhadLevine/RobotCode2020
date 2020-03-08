package frc.robot.commands.command_groups;

import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.constants.RobotConstants.SpinnerConstants;
import frc.robot.subsystems.spinner.SpinPanelByColor;
import frc.robot.subsystems.spinner.SpinPanelByRotation;

import static frc.robot.Robot.spinner;

public class SpinControlPanel extends SequentialCommandGroup {
  /**
   * spins the control panel either by rotation or by color.
   * 
   * @param rotationOrColor if true spins control panel by rotation if false spins
   *                        it by color from the fms.
   */
  public SpinControlPanel(boolean rotationOrColor) {
    super(new WaitUntilCommand(() -> spinner.getProximity() > SpinnerConstants.minimumProximity),
        new ConditionalCommand(new SpinPanelByRotation(), new SpinPanelByColor(), () -> rotationOrColor));
  }
}
