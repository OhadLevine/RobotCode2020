package frc.robot.utils;

import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.commands.OverrideCommand;
import frc.robot.subsystems.intakeopener.SetDesiredOpenerAngle;
import io.github.oblarg.oblog.Logger;

import static edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.*;
import static frc.robot.Robot.intake;
import static frc.robot.Robot.intakeOpener;

/**
 * DashboardDataContainer contains all the data to be viewed or put in the
 * dashboard.
 */
public class DashboardDataContainer {
    
    public DashboardDataContainer() {
        // Intake dashboard data
        putNumber("Intake/Intake power", 0);
        putData("Intake/Override intake", new OverrideCommand(intake,
            () -> getNumber("Intake/Intake power", 0)));
        // IntakeOpener dashboard data
        putNumber("IntakeOpener/Intake Opener power", 0);
        putData("IntakeOpener/Override intake opener", new OverrideCommand(intakeOpener,
            () -> getNumber("IntakeOpener/Intake Opener power", 0)));
        putData("IntakeOpener/Tune PID", new StartEndCommand(() -> intakeOpener.getDefaultCommand().enableTuning(),
            () -> intakeOpener.getDefaultCommand().stopTuning(), intakeOpener));
        putData("IntakeOpener/Open", new SetDesiredOpenerAngle(true));
        putData("IntakeOpener/Close", new SetDesiredOpenerAngle(false));
    }

    public void update() {
        Logger.updateEntries();
    }
}
