package com.first.team2052.steamworks.subsystems.light;

import com.first.team2052.steamworks.subsystems.light.runner.FlashBurstRunner;
import com.first.team2052.steamworks.subsystems.light.runner.FlashNumberRunner;
import com.first.team2052.steamworks.subsystems.light.runner.FlashSpeedRunner;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * Naive implementation for a flashing a light for driver indication
 * Please don't take inspiration from this code - Adam
 */
public class LightFlasher {
    private static LightFlasher instance = new LightFlasher();
    private final Solenoid lightRelay;
    private LightFlashRunner runner;

    private LightFlasher() {
        lightRelay = new Solenoid(6);
    }

    public static LightFlasher getInstance() {
        return instance;
    }

    public void setLightOnOpen(boolean on) {
        if (runner != null) {
            runner.stopFlashSequence();
        }
        runner = null;
        setLightOn(on);
    }

    void setLightOn(boolean on) {
        lightRelay.set(on);
    }

    public void flashTimes(int number) {
        if(runner instanceof FlashNumberRunner && ((FlashNumberRunner) runner).getNumber() == number && runner.isRunning){
            return;
        }
        if(runner != null && runner.isRunning){
            runner.stopFlashSequence();
        }
        runner = new FlashNumberRunner(number);
    }

    public void flashSpeed(double delay_between_flash) {
        //The speed flasher is already running, don't touch it
        if (runner instanceof FlashSpeedRunner && ((FlashSpeedRunner) runner).getSpeed() == delay_between_flash) {
            return;
        }

        if(runner != null && runner.isRunning){
            runner.stopFlashSequence();
        }

        runner = new FlashSpeedRunner(delay_between_flash);
    }

    public void flashNumTimesByBurst(int number_per_burst, int number_burst, double delay_between_burst) {
        if(runner instanceof FlashBurstRunner && ((FlashBurstRunner) runner).compare(number_burst, number_per_burst, delay_between_burst) && runner.isRunning){
            return;
        }
        if(runner != null && runner.isRunning){
            runner.stopFlashSequence();
        }
        runner = new FlashBurstRunner(number_burst, number_per_burst, delay_between_burst);
    }
}
