package com.first.team2052.steamworks.subsystems.shooter;

import com.first.team2052.lib.Loopable;
import com.first.team2052.steamworks.Constants;

public class Shooter implements Loopable {

    private static Shooter instance = new Shooter();

    private final Indexer indexer;
    private final Flywheel flywheel;

    private ShooterState shooterState = ShooterState.STOP;
    private boolean wantShoot = false, wantReverseAgitator = false, wantIdleRampUp = false;

    private Shooter() {
        indexer = Indexer.getInsance();
        flywheel = Flywheel.getInstance();
    }

    @Override
    public void update() {
        ShooterState newState = shooterState;
        switch (shooterState) {
            case STOP:
                indexer.setAgitatorSpeed(0.0);
                indexer.setIndexerSpeed(0.0);
                flywheel.setRpm(0.0);

                //This is where the wanted states fork off from. The stop state is the default state and if any of these flags are set, the new state is what comes next
                if (wantShoot) {
                    newState = ShooterState.RAMP_UP;
                } else if (wantIdleRampUp) {
                    newState = ShooterState.IDLE_RAMP_UP;
                } else if (wantReverseAgitator) {
                    newState = ShooterState.REVERSE_AGITATOR;
                }
                break;
            case IDLE_RAMP_UP:
                indexer.setAgitatorSpeed(0.0);
                indexer.setIndexerSpeed(0.0);
                flywheel.setRpm(Constants.Shooter.kShooterKeyVelocity);
                if (wantShoot) {
                    //If it wants to shoot, then go to the ramp-up state so that it can check that it is up to speed
                    newState = ShooterState.RAMP_UP;
                } else if (!wantIdleRampUp) {
                    //If it doesn't want to idle ramp-up anymore, just stop the shooter.
                    newState = ShooterState.STOP;
                }
                break;
            case RAMP_UP:
                indexer.setAgitatorSpeed(0.0);
                indexer.setIndexerSpeed(0.0);
                flywheel.setRpm(Constants.Shooter.kShooterKeyVelocity);

                if (!wantShoot) {
                    newState = ShooterState.STOP;
                } else if (flywheel.isOnTarget()) {
                    //Once the flywheel is up to target speed, switch the state to shooting so it runs the agitators
                    newState = ShooterState.SHOOTING;
                }
                break;
            case SHOOTING:
                indexer.setAgitatorSpeed(Constants.Shooter.kTatorSpeed);
                indexer.setIndexerSpeed(1.0);
                flywheel.setRpm(Constants.Shooter.kShooterKeyVelocity);

                if (!wantShoot) {
                    //If it doesn't want to shoot then see if it wants to keep the flywheel up to speed if not just stop the entire shooter
                    if (wantIdleRampUp) {
                        newState = ShooterState.IDLE_RAMP_UP;
                    } else {
                        newState = ShooterState.STOP;
                    }
                }
                break;
            case REVERSE_AGITATOR:
                indexer.setAgitatorSpeed(-Constants.Shooter.kTatorSpeed);
                indexer.setIndexerSpeed(-1.0);
                flywheel.setRpm(0.0);
                if (!wantReverseAgitator) {
                    newState = ShooterState.STOP;
                }
                break;
        }

        if (newState != shooterState) {
            shooterState = newState;
            System.out.println(String.format("Shoot state changed to %s", newState.name()));
        }
        //System.out.println("rpm: " + shootMotor.getSpeed());
    }


    @Override
    public void onStart() {
        flywheel.setRpm(0);
        setWantShoot(false);
        shooterState = ShooterState.STOP;
    }

    @Override
    public void onStop() {
        flywheel.setRpm(0);
        setWantShoot(false);
        shooterState = ShooterState.STOP;
    }

    /**
     * @return The current state of the shooter
     */
    public ShooterState getShooterState() {
        return shooterState;
    }

    /**
     * Sets the wanted state to reverse the agitator. This state is used to unjam the balls if they do get stuck
     */
    public void setWantReverseAgitator(boolean wantReverseAgitator) {
        this.wantReverseAgitator = wantReverseAgitator;
    }

    /**
     * Sets the wanted state to shoot. If the shooter isn't already spun up to it's target speed
     * - It can be already sped up under the idle ramp-up state -
     * after the flywheel is spun up, the agitator's will engage and the shooter will start firing balls
     */
    public void setWantShoot(boolean wantShoot) {
        this.wantShoot = wantShoot;
    }

    /**
     * Sets the wanted state to be idle ramp-up. This state spins up the flywheel to the target speed, but doesn't actually start shooting
     */
    public void setWantIdleRampUp(boolean wantIdleRampUp) {
        this.wantIdleRampUp = wantIdleRampUp;
    }

    /**
     * Enumerated shooter states for the shooter
     */
    public enum ShooterState {
        IDLE_RAMP_UP,
        RAMP_UP,
        SHOOTING,
        STOP,
        REVERSE_AGITATOR
    }

    public static Shooter getInstance() {
        return instance;
    }
}
