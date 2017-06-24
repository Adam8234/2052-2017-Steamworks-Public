package com.first.team2052.steamworks.auto.actions;

import com.first.team2052.steamworks.subsystems.shooter.Shooter;

/**
 * Action that starts shooting. Action finishes when the shooter has started running, this means that the flywheel is up to speed and no more
 */
public class StartShootingAction implements Action {

    private final Shooter shooter;

    public StartShootingAction() {
        shooter = Shooter.getInstance();
    }

    @Override
    public void done() {
    }

    @Override
    public boolean isFinished() {
        return shooter.getShooterState() == Shooter.ShooterState.SHOOTING;
    }

    @Override
    public void start() {
        shooter.setWantShoot(true);
    }

    @Override
    public void update() {

    }
}
