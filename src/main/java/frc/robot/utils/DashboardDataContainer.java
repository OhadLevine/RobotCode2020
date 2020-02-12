package frc.robot.utils;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.commands.OverrideCommand;
import frc.robot.commands.RunWhenDisabledCommand;
import frc.robot.commands.command_groups.AutoShoot;
import frc.robot.subsystems.drivetrain.RotateDrivetrain;
import frc.robot.subsystems.loader.LoaderPower;
import frc.robot.subsystems.loader.SetLoaderSpeed;
import frc.robot.subsystems.mixer.SpinMixer;
import frc.robot.subsystems.shooter.CalibrateShooterVelocity;
import frc.robot.subsystems.shooter.CheesySetShooterVelocity;
import frc.robot.subsystems.shooter.SetShooterVelocity;
import frc.robot.vision.Target;
import frc.robot.vision.TurnToTarget;
import io.github.oblarg.oblog.Logger;

import static edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.*;
import static frc.robot.Robot.*;

/**
 * DashboardDataContainer contains all the data to be viewed or put in the
 * dashboard.
 */
public class DashboardDataContainer {

    public DashboardDataContainer() {
        // Mixer dashboard data:
        putNumber("Mixer/Mixer power", 0);
        putData("Mixer/Override", new OverrideCommand(mixer,
            () -> getNumber("Mixer/Mixer power", 0)));
        
        // Drivetrain dashboard data
        putData("Drivetrain/Tune drivetrain rotate PID", new RotateDrivetrain());
        putData("Drivetrain/Reset Encoders", new RunWhenDisabledCommand(drivetrain::resetEncoders, drivetrain));
        putData("Drivetrain/Reset Gyro", new RunWhenDisabledCommand(drivetrain::resetGyro, drivetrain));
        putData("Drivetrain/Calibrate Gyro", new RunWhenDisabledCommand(drivetrain::calibrateGyro, drivetrain));
        putData("Drivetrain/Reset Odometry", new RunWhenDisabledCommand(drivetrain::resetOdometry, drivetrain));
        
        // Shooter dashboard data
        putNumber("Shooter/Shooting velocity setpoint", 3500);
        putData("Shooter/Set cheesy shooting velocity", new CheesySetShooterVelocity(() -> getNumber("Shooter/Shooting velocity setpoint", 0), false));
        putData("Shooter/Set shooting velocity", new SetShooterVelocity(() -> getNumber("Shooter/Shooting velocity setpoint", 0)));
        putData("Shooter/Enable tuning", new StartEndCommand(shooter::enableTuning, shooter::disableTuning));
        putNumber("Shooter/Override Power", 0);
        putData("Shooter/Override", new OverrideCommand(shooter,
            () -> getNumber("Shooter/Override Power", 0)));
        putData("Shooter/Calibrate shooter velocity", new CalibrateShooterVelocity(oi.getDriverXboxController()::getAButton,
            () -> getNumber("Shooter/Shooting velocity setpoint", 0), 100, 100)); // TODO: set starting distance and delta distance!
        putData("Shooter/Turn to port", new TurnToTarget(Target.PowerPort, drivetrain, "Turn PID"));

        // CommandGroup dashboard data
        putData("CommandGroup/Mix and Load", new ParallelCommandGroup(
            new SetLoaderSpeed(LoaderPower.DefaultLoadToShoot),
            new SpinMixer()));
        putData("CommandGroup/Auto Shoot", new AutoShoot(() -> getNumber("Shooter/Shooting velocity setpoint", 0), true));
    }

    public void update() {
        Logger.updateEntries();
    }
}
