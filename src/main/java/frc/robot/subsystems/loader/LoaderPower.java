package frc.robot.subsystems.loader;

public enum LoaderPower {
    DefaultLoadToShoot(0.755), FarShoot(0.45), DefaultBackwardsForMix(-0.1), onStall(-0.25);
    private final double power;

    LoaderPower(double power) {
        this.power = power;
    }

    public double getPower() {
        return power;
    }
}
