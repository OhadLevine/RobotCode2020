package frc.robot.subsystems.loader;

public enum LoaderPower {
    DefaultLoadToShoot(0.7), 
    FarShoot(0.45), 
    DefaultBackwardsForMix(-0.1);
    
    private final double power;

    LoaderPower(double power) {
        this.power = power;
    }

    public double getPower() {
        return power;
    }
}
