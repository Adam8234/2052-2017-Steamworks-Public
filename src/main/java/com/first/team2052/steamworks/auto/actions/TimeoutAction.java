package com.first.team2052.steamworks.auto.actions;

import edu.wpi.first.wpilibj.Timer;

public class TimeoutAction implements Action {
    Action action;
    private double mTimeToWait;
    private double mStartTime;

    public TimeoutAction(Action action, double mTimeToWait) {
        this.action = action;
        this.mTimeToWait = mTimeToWait;
    }

    @Override
    public void done() {
        action.done();
    }

    @Override
    public boolean isFinished() {
        if ((Timer.getFPGATimestamp() - mStartTime) >= mTimeToWait && !action.isFinished()) {
            System.out.println("TIMEOUT ACTION - SHOULD FINISH");
        }
        return (Timer.getFPGATimestamp() - mStartTime) >= mTimeToWait || action.isFinished();
    }

    @Override
    public void start() {
        mStartTime = Timer.getFPGATimestamp();
        action.start();
    }

    @Override
    public void update() {
        action.update();
    }
}
