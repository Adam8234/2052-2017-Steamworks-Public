package com.first.team2052.steamworks.auto.actions;

import com.first.team2052.steamworks.subsystems.GearMan;

public class CloseGearAction implements Action {

    @Override
    public void done() {

    }

    @Override
    public boolean isFinished() {
        return GearMan.getInstance().getCurrentState() == GearMan.GearManState.CLOSED && GearMan.getInstance().getSolenoidState();
    }

    @Override
    public void start() {
        GearMan.getInstance().setWantOpen(false);
    }

    @Override
    public void update() {

    }
}
