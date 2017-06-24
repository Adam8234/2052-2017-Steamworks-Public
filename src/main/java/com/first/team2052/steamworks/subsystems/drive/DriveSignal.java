package com.first.team2052.steamworks.subsystems.drive;

public class DriveSignal {
    public double leftMotor;
    public double rightMotor;

    public DriveSignal(double left, double right) {
        this.leftMotor = left;
        this.rightMotor = right;
    }

    public static DriveSignal NEUTRAL = new DriveSignal(0, 0);

    @Override
    public String toString() {
        return "L: " + leftMotor + ", R: " + rightMotor;
    }
}
