package com.first.team2052.steamworks.subsystems.light;

public abstract class LightFlashRunner {
    private Thread lightThread;
    protected boolean isRunning = true;

    public LightFlashRunner() {
        lightThread = new Thread(this::runFlashSequence);
        lightThread.start();
    }

    public abstract void runFlashSequence();

    protected void setOn(boolean on) throws IllegalStateException {
        if (isRunning) {
            LightFlasher.getInstance().setLightOn(on);
        }
    }

    public void stopFlashSequence() {
        isRunning = false;
        lightThread = null;
    }
}
