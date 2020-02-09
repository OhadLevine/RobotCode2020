package frc.robot.subsystems.loader;

public enum LoaderPower {
    defaults(0.3), backwards(-0.2), faraway(0.2), onStall(-0.3);
    private final double power;

    LoaderPower(double power) {
        this.power = power;
    }

    public double getPower() {
        return power;
    }
}
