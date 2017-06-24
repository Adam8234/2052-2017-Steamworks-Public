package com.first.team2052.lib;

import edu.wpi.first.wpilibj.Timer;

/**
 * There is no running condition for the regular time in WPILib
 * Why? No clue.
 * It's under the @{@link edu.wpi.first.wpilibj.internal.HardwareTimer}'s @{@link edu.wpi.first.wpilibj.internal.HardwareTimer.TimerImpl} but is not wrapped to the @{@link Timer}
 */
public class KnightTimer extends Timer {
    private boolean running;

    @Override
    public void start() {
        super.start();
        running = true;
    }

    @Override
    public void stop() {
        super.stop();
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean hasPassedTime(double time) {
        return get() > time;
    }
}
