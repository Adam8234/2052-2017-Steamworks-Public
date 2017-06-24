package com.first.team2052.steamworks.subsystems.light.runner;

import com.first.team2052.steamworks.subsystems.light.LightFlashRunner;
import edu.wpi.first.wpilibj.Timer;

/**
 * Created by Adam on 4/14/2017.
 */
public class FlashNumberRunner extends LightFlashRunner {
    private int number;

    public FlashNumberRunner(int number) {
        this.number = number;
    }

    @Override
    public void runFlashSequence() {
        for (int i = 0; i < number; i++) {
            setOn(true);
            Timer.delay(0.25);
            setOn(false);
            Timer.delay(0.25);
        }
        isRunning = false;
    }

    public int getNumber() {
        return number;
    }
}
