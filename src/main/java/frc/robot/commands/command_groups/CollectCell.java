package frc.robot.commands.command_groups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.MoveMovableSubsystem;
import frc.robot.subsystems.intake.SetIntakeSpeed;
import frc.robot.subsystems.intakeopener.OpenIntake;
import frc.robot.subsystems.mixer.SpinMixer;

import static frc.robot.Robot.loader;
import static frc.robot.Robot.robotConstants;

public class CollectCell extends SequentialCommandGroup {
    public CollectCell() {
        addCommands(
            new OpenIntake(true),
            parallel(
                new SetIntakeSpeed(() -> robotConstants.intakeConstants.kDefaultIntakePower),
                new SpinMixer(),
                new MoveMovableSubsystem(loader, () -> robotConstants.loaderConstants.kDefaultBackwardsPower)
            )
        );
    }
}