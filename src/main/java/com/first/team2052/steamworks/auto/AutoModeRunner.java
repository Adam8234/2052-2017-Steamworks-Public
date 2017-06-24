package com.first.team2052.steamworks.auto;

public class AutoModeRunner {
    Thread autoThread;
    AutoModeBase autoMode;

    public void start() {
        if (autoMode == null)
            return;
        autoThread = new Thread(() -> autoMode.start());
        autoThread.start();
    }

    public void setAutoMode(AutoModeBase autoMode) {
        System.out.println(autoMode);
        this.autoMode = autoMode;
    }

    public void stop() {
        if (autoMode != null)
            autoMode.stop();
        autoThread = null;
    }
}
