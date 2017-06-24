package com.first.team2052.steamworks.subsystems.light.runner;

import com.first.team2052.steamworks.subsystems.light.LightFlashRunner;
import edu.wpi.first.wpilibj.Timer;

public class FlashSpeedRunner extends LightFlashRunner {

    private double speed;

    public FlashSpeedRunner(double speed){
        this.speed = speed;
    }

    @Override
    public void runFlashSequence() {
        while (isRunning) {
            setOn(true);
            Timer.delay(speed);
            setOn(false);
            Timer.delay(speed);
        }
    }

    public double getSpeed() {
        return speed;
    }
}
