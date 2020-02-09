package frc.robot.subsystems.loader;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import java.util.function.DoubleSupplier;

import static frc.robot.Robot.loader;
import static frc.robot.Robot.robotConstants;

/**
 * Spins the loader during the game for putting balls in the shooter, if the
 * current is too big the motor power flips.
 */
public class SetLoaderSpeed extends CommandBase {
    private DoubleSupplier power;
    private double lastTimeNotOnStall;
    private double backwardsSpinStartTime;

    /**
     * This constructor creates the command that spins
     * the loader at the power of LoaderPower.DefaultLoadToShoot.
     */
    public SetLoaderSpeed() {
        this(LoaderPower.DefaultLoadToShoot);
    }

    public SetLoaderSpeed(LoaderPower loaderPower) {
        this(() -> loaderPower.getPower());
    }

    /**
     * gets a supplier for motor power
     */
    public SetLoaderSpeed(DoubleSupplier power) {
        addRequirements(loader);
        this.power = power;
    }

    @Override
    public void initialize() {
        lastTimeNotOnStall = Timer.getFPGATimestamp();
        backwardsSpinStartTime = 0;
    }

    @Override
    public void execute() {
        if (Timer.getFPGATimestamp() - backwardsSpinStartTime < robotConstants.loaderConstants.kSpinBackwardsTime)
            loader.move(-power.getAsDouble());
        else {
            if (!loader.getIsInStall()) {
                lastTimeNotOnStall = Timer.getFPGATimestamp();
            }
            if (Timer.getFPGATimestamp() - lastTimeNotOnStall > robotConstants.loaderConstants.kStallWaitTime) {
                backwardsSpinStartTime = Timer.getFPGATimestamp();
                loader.move(-power.getAsDouble());
            } else
                loader.move(power.getAsDouble());
        }
    }

    @Override
    public void end(boolean interrupted) {
        loader.stopMove();
    }
}
