package frc.robot.commands.command_groups;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.drivetrain.DriveForClimb;

import static frc.robot.Robot.robotConstants;

public class AutoClimb extends SequentialCommandGroup {
    public AutoClimb() {
        addCommands(
            new DriveForClimb(robotConstants.drivetrainConstants.kClimbDriveDistance)
        );
    }
}