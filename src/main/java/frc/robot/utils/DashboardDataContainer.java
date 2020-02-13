package frc.robot.utils;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
        putDefaultNumber("Intake/Intake power", 0);
        putData("Intake/Override intake", new OverrideCommand(intake,
            () -> getNumber("Intake/Intake power", 0)));
        // IntakeOpener dashboard data
        putDefaultNumber("IntakeOpener/Intake Opener power", 0);
        putData("IntakeOpener/Override intake opener", new OverrideCommand(intakeOpener,
            () -> getNumber("IntakeOpener/Intake Opener power", 0)));
        putData("IntakeOpener/Tune PID", new StartEndCommand(() -> intakeOpener.getDefaultCommand().enableTuning(),
            () -> intakeOpener.getDefaultCommand().stopTuning(), intakeOpener));
        putData("IntakeOpener/Open", new SetDesiredOpenerAngle(true));
        putData("IntakeOpener/Close", new SetDesiredOpenerAngle(false));
    }

    /**
     * Sets the SmartDashboard entry's value if it does not exist.
     *
     * @param key the smartDashboard key
     * @param defaultValue the default value to set
     */
    private void putDefaultNumber(String key, double defaultValue) {
        SmartDashboard.getEntry(key).setDefaultNumber(defaultValue);
    }

    public void update() {
        Logger.updateEntries();
    }
}
