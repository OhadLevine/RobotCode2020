package frc.robot.subsystems.mixer;

public enum MixerPower {
    MixForShoot(0.8),
    MixForIntake(0.6),
    MixForOrder(0.2);

    private final double power;

    MixerPower(double power) {
        this.power = power;
    }

    public double getPower() {
        return power;
    }
}