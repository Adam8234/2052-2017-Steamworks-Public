package com.first.team2052.steamworks.subsystems.shooter;

import com.ctre.CANTalon;
import com.first.team2052.steamworks.Constants;

class Indexer {
    private CANTalon agitator1, agitator2;
    private CANTalon indexer;

    private static Indexer instance = new Indexer();

    private Indexer() {
        agitator1 = new CANTalon(Constants.CAN.kLeftAgitatorMotorPort);
        agitator2 = new CANTalon(Constants.CAN.kRightAgitatorMotorPort);
        indexer = new CANTalon(Constants.CAN.kIndexerId);
    }

    void setAgitatorSpeed(double speed){
        agitator1.set(-speed);
        agitator2.set(speed);
    }

    void setIndexerSpeed(double speed){
        indexer.set(speed);
    }

    static Indexer getInsance() {
        return instance;
    }
}
