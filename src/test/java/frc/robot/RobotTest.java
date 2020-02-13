package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.MoveMovableSubsystem;
import frc.robot.commands.OverrideCommand;
import frc.robot.subsystems.intake.SetIntakeSpeed;
import frc.robot.subsystems.intakeopener.CharacterizeIntakeOpener;
import frc.robot.subsystems.intakeopener.OpenIntake;
import frc.robot.subsystems.intakeopener.SetDesiredOpenerAngle;
import org.junit.BeforeClass;
import org.junit.Test;

public class RobotTest {
    private static Robot robot;

    @BeforeClass
    public static void init() {
        robot = new Robot();
        robot.robotInit();
    }

    @Test
    public void robotPeriodicTest() {
        robot.robotPeriodic();
    }

    @Test
    public void autonomousTest() {
        robot.autonomousInit();
        robot.autonomousPeriodic();
    }

    @Test
    public void teleopTest() {
        robot.teleopInit();
        robot.teleopPeriodic();
    }

    @Test
    public void commandsTest() {
        Command[] commands = new Command[] {
            new SetLoaderSpeed(),
            new SpinMixer(),
            new SetIntakeSpeed(Robot.robotConstants.intakeConstants.kDefaultIntakePower),
            new CheesySetShooterVelocity(),
            new CharacterizeIntakeOpener(),
            new OpenIntake(true, Robot.intakeOpener),
            new SetDesiredOpenerAngle(true),
            new ClimbWithXbox(() -> 0, () -> 0),
            new CalibrateVisionDistance(() -> false, Target.Feeder, 0),
            new FollowTarget(Target.Feeder),
            new TurnToTarget(Target.Feeder, Robot.drivetrain),
            new CalibrateFeedforward(),
            new FollowPath(AutoPath.FacingPowerPortToMiddleField),
            new RotateDrivetrain(),
            new DriveWithXbox(() -> 0, () -> 0),
            new SensorCheck(),
            new MoveMovableSubsystem(Robot.loader, () -> 0),
            new OverrideCommand(Robot.shooter, () -> 0),
            new CollectCell(),
            new CollectFromFeeder(),
            new AutoShoot(),
            new SimpleAuto(),
            new TrenchAuto(StartingPose.kFacingPowerPort),
            new MiddleFieldAuto(StartingPose.kFacingPowerPort),
        };

        for (Command command : commands) {
            command.initialize();
            command.execute();
            command.isFinished();
            command.end(false);
            command.end(true);
        }
    }
}