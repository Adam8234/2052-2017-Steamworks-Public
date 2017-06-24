package com.first.team2052.steamworks.auto;

import com.first.team2052.steamworks.auto.actions.Action;
import edu.wpi.first.wpilibj.Timer;

public abstract class AutoModeBase {

    private boolean running = false;
    private Timer timer = new Timer();

    public void delay(double seconds) throws AutoModeEndedException {
        isRunningWithThrow();
        Timer.delay(seconds);
    }

    public void errorStop(String message) throws AutoModeEndedException {
        System.out.println(message);
        //stop();
        throw new AutoModeEndedException();
    }

    public double getAutoTime() {
        return timer.get();
    }

    protected abstract void init() throws AutoModeEndedException;

    public boolean isRunning() {
        return running;
    }

    public boolean isRunningWithThrow() throws AutoModeEndedException {
        if (!isRunning()) {
            throw new AutoModeEndedException();
        }
        return isRunning();
    }

    protected void runAction(Action action) throws AutoModeEndedException {
        isRunningWithThrow();
        action.start();
        while (!action.isFinished() && isRunningWithThrow()) {
            action.update();
            try {
                Thread.sleep((long) ((1.0 / 50.0) * 1000.0));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        action.done();
    }

    public void start() {
        running = true;
        timer.reset();
        timer.start();
        try {
            init();
        } catch (AutoModeEndedException e) {
            System.out.println("Auto Ended Early");
            e.printStackTrace();
        }
    }


    public void stop() {
        running = false;
        System.out.println("Stopping Auto");
        timer.stop();
    }
}
