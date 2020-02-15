package frc.robot.utils;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.MoveMovableSubsystem;
import frc.robot.commands.OverrideCommand;
import frc.robot.commands.command_groups.CollectCell;
import frc.robot.subsystems.climb.ClimbWithXbox;
import frc.robot.subsystems.intakeopener.CloseIntake;
import frc.robot.subsystems.intakeopener.OpenIntake;
import io.github.oblarg.oblog.Logger;

import static edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.getNumber;
import static edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putData;
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
        putDefaultNumber("Intake Opener/Intake Opener power", 0);
        putData("Intake Opener/Override intake opener", new OverrideCommand(intakeOpener,
            () -> getNumber("Intake Opener/Intake Opener power", 0)));
        putData("Intake Opener/Tune Open PID", new OpenIntake());
        putData("Intake Opener/Tune Close PID", new CloseIntake());
        putData("collect", new CollectCell());

        putData("Intake Opener/Open Intake", new OpenIntake(true));
        putData("Intake Opener/Close Intake", new OpenIntake(false));
        putData("Intake Opener/Move", new MoveMovableSubsystem(intakeOpener, () -> getNumber("Intake Opener/Intake Opener power", 0)));
        putDefaultNumber("hook", 0);
        putData("Climb/climb", new ClimbWithXbox(() -> 0,() -> SmartDashboard.getNumber("hook", 0)));
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
