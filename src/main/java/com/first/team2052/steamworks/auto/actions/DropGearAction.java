package com.first.team2052.steamworks.auto.actions;

import com.first.team2052.steamworks.subsystems.GearMan;


public class DropGearAction implements Action {

    GearMan gearMan;

    public DropGearAction() {
        this.gearMan = GearMan.getInstance();
    }

    @Override
    public void done() {

    }

    @Override
    public boolean isFinished() {
        return gearMan.getCurrentState() == GearMan.GearManState.OPEN_MANUAL && !gearMan.getSolenoidState();
    }

    @Override
    public void start() {
        gearMan.setWantOpen(true);
    }

    @Override
    public void update() {
    }
}
