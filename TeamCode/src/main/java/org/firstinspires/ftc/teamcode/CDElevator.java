package org.firstinspires.ftc.teamcode;

import android.text.method.*;

import com.qualcomm.robotcore.hardware.*;

public class CDElevator {

    CDHardware robotHardware;
    CDDistanceSensor myDistanceSensor;
    public boolean elevatorstop;
    public boolean magneticstop;
    public boolean elevatorerror;
    public double ELEVATORCURRENTTHRESHOLD;
    public double elevatorposcurrent;
    public double elevatorlastpos;
    public TouchSensor upelevatormagnetswitch;


    public CDElevator(CDHardware theHardware){

        robotHardware = theHardware;
        myDistanceSensor =new CDDistanceSensor(robotHardware);
        upelevatormagnetswitch = robotHardware.elevatormagneticswitch;

        // robotHardware.elevatorswitchtop;
        // robotHardware.elevatorswitchmiddle;
        // robotHardware.elevatorswitchbottom;
        // robotHardware.elevatorswitchground;

        robotHardware.elevatormotor.setDirection(DcMotorSimple.Direction.FORWARD);

        robotHardware.elevatormotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public double getElevatorThreshold () { return this.ELEVATORCURRENTTHRESHOLD; }

    public void setElevatorPower(double pow) {
        if (!upelevatormagnetswitch.isPressed() ) {
            robotHardware.elevatormotor.setPower(pow);
        }
    }

    public double getElevatorPosition() { return this.elevatorposcurrent; }

    // TODO: Add public method to gotoPosition(Top, Middle, bottom)
    public boolean setElevatorPosition(double elevatorpostarget) {
        final double THRESHOLD_POS = 3; // CM or whatever the Distance sensor is configured
        double elevatormult = 0.75; // to slow down the elevator if needed
        elevatorstop = false; // initially we want the elevator to move for the while loop
        magneticstop = false;
        elevatorerror = false;
        while (!elevatorstop && !magneticstop && !elevatorerror) {
            // Simple check to see if the magnetic switch is contacted
            if (!upelevatormagnetswitch.isPressed()) {
                magneticstop = true;
            }
            if (elevatorlastpos == elevatorposcurrent) {
                elevatorerror = true;
                return false; // There was an error, the value didn't change.
            }
            /* This gets the current distance off the floor from the Elevator Distance Sensor
          and sets it to a variable
           */
            elevatorlastpos = myDistanceSensor.getElevatorDistance(); // updates every loop to say where we are in the beginning.
            ELEVATORCURRENTTHRESHOLD = Math.abs(elevatorlastpos - elevatorpostarget);
            if (ELEVATORCURRENTTHRESHOLD < THRESHOLD_POS)  {
                setElevatorPower(0); // need to stop the elevator before leaving the loop
                elevatorstop = true; // leave the while loop
            } else if (elevatorlastpos >= 42.0) { // This check may be outdated with the magnetic switch
                setElevatorPower(0); // need to stop the elevator before leaving the loop
                elevatorstop = true; // leave the while loop
            } else if (elevatorlastpos <= 1) { // This is to stop runaways if we are on the ground already the string will wind up again
                setElevatorPower(0); // need to stop the elevator before leaving the loop
                elevatorstop = true; // leave the while loop
            } else if (elevatorlastpos > elevatorpostarget) {
                setElevatorPower(-1*elevatormult);
            } else if (elevatorlastpos < elevatorpostarget) {
                setElevatorPower(1*elevatormult);
            }
            elevatorposcurrent = myDistanceSensor.getElevatorDistance(); // updates every loop to see where we ended up.
        }
        return true; // Returns true if the elevator succeeded in moving to requested position.
    }
    //
}
