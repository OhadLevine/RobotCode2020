package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.subsystems.OverridableSubsystem;
import java.util.function.DoubleSupplier;


public class OverrideCommand extends StartEndCommand {

    public OverrideCommand(OverridableSubsystem overridableSubsystem) {
        super(() -> overridableSubsystem.startOverride(),
            overridableSubsystem::stopOverride);
    }
}
