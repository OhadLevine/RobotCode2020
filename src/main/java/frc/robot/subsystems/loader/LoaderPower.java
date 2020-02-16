package frc.robot.subsystems.loader;

public enum LoaderPower {
    DefaultLoadToShoot(0.7), // 780 RPM
    FarShoot(0.45), 
    DefaultBackwardsForMix(-0.045);
    
    private final double power;

    LoaderPower(double power) {
        this.power = power;
    }

    public double getPower() {
        return power;
    }
}
