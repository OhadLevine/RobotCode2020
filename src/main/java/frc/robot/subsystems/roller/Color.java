package frc.robot.subsystems.roller;

/**
 * This Enum holds all of the color constants.
 */
public enum Color {
    MixForShoot(Color),
    MixForFarShoot(0.3),
    MixForSort(0.3),
    MixForHardSort(-0.55),
    MixForAuto(0.3),
    MixReverse(-0.3);

    private final double power;

    Color(double power) {
        this.power = power;
    }

    public double getPower() {
        return power;
    }
}
