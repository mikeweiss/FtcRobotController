package org.firstinspires.ftc.team8580;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Hardware;

public class CDTurret {

    CDHardware robotHardware;

    public  CDTurret(CDHardware theHardware){

        robotHardware = theHardware;


        robotHardware.turretmotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }



}