package frc.robot.subsystems.spinner;

import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.utils.FMSColor;

/**
 * Add your docs here.
 */
public class SpinPanelByColor extends ConditionalCommand {

    public SpinPanelByColor() {
        super(new SpinPanelByColorWithoutCondition(), new InstantCommand(), () -> FMSColor.getFMSColor() != null);
    }
}
