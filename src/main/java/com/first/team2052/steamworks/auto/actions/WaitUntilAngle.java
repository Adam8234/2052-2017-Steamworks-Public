package com.first.team2052.steamworks.auto.actions;

import com.first.team2052.steamworks.subsystems.drive.DriveTrain;

public class WaitUntilAngle implements Action {
    private final double angle;

    public WaitUntilAngle(double angle){
        this.angle = angle;
    }

    @Override
    public void done() {
    }

    @Override
    public boolean isFinished() {
        return Math.abs(DriveTrain.getInstance().getGyroAngleDegrees() - angle) < 1.5;
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {

    }
}
