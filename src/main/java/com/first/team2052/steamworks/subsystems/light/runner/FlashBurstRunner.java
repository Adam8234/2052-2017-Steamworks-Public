package com.first.team2052.steamworks.subsystems.light.runner;

import com.first.team2052.steamworks.subsystems.light.LightFlashRunner;
import edu.wpi.first.wpilibj.Timer;

/**
 * Created by Adam on 4/14/2017.
 */
public class FlashBurstRunner extends LightFlashRunner {
    private int number_burst;
    private int number_per_burst;
    private double delay_between_burst;

    public FlashBurstRunner(int number_burst, int number_per_burst, double delay_between_burst) {
        this.number_burst = number_burst;
        this.number_per_burst = number_per_burst;
        this.delay_between_burst = delay_between_burst;
    }

    @Override
    public void runFlashSequence() {
        for (int burstIndex = 0; burstIndex < number_burst; burstIndex++) {
            for (int numberBurstIndex = 0; numberBurstIndex < number_per_burst; numberBurstIndex++) {
                setOn(true);
                Timer.delay(0.25);
                setOn(false);
                Timer.delay(0.25);
            }
            Timer.delay(delay_between_burst);
        }
        isRunning = false;
    }

    public boolean compare(int number_burst, int number_per_burst, double delay_between_burst) {
        return number_burst == this.number_burst && number_per_burst == this.number_per_burst && delay_between_burst == this.delay_between_burst;
    }
}
