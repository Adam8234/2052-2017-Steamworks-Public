package com.first.team2052.steamworks.subsystems.light;

import com.first.team2052.lib.Loopable;
import com.first.team2052.steamworks.Controls;
import com.first.team2052.steamworks.subsystems.Climber;
import com.first.team2052.steamworks.subsystems.GearMan;
import com.first.team2052.steamworks.subsystems.shooter.Shooter;

public class LightFlasherLoopable implements Loopable {
    private static LightFlasherLoopable instance = new LightFlasherLoopable();
    private final Controls controls;
    private Shooter shooter;
    private LightFlasher lightFlasher;
    private GearMan gearMan;
    private Climber climber;

    private LightFlasherLoopable() {
        lightFlasher = LightFlasher.getInstance();
        shooter = Shooter.getInstance();
        gearMan = GearMan.getInstance();
        climber = Climber.getInstance();
        controls = Controls.getInstance();
    }

    public static LightFlasherLoopable getInstance() {
        return instance;
    }

    @Override
    public void update() {
        if (shooter.getShooterState() == Shooter.ShooterState.SHOOTING) {
            lightFlasher.flashSpeed(0.125);
        } else if (gearMan.getCurrentState() == GearMan.GearManState.OPEN_PUNCHED) {
            lightFlasher.flashSpeed(0.25);
        } else if (gearMan.getCurrentState() == GearMan.GearManState.CLOSING) {
            lightFlasher.flashSpeed(0.25);
        } else if (climber.getCurrentState() == Climber.ClimberState.UP) {
            lightFlasher.flashSpeed(0.25);
        } else if(climber.getCurrentState() == Climber.ClimberState.SLOW_UP) {
            lightFlasher.flashSpeed(0.5);
        } else if (controls.isClimberAmpLimitReached()) {
            lightFlasher.flashSpeed(0.125);
        } else if (!GearMan.getInstance().getSolenoidState()) {
            lightFlasher.flashSpeed(.125);
        } else {
            lightFlasher.setLightOnOpen(false);
        }
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        lightFlasher.setLightOnOpen(false);
    }
}
