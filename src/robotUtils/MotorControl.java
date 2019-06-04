package robotUtils;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

/**
 * Template for controlling the motors using brick buttons like in EV3. This
 * extends RobotRun since it utilizes the threading infrastructure
 * 
 * @author John & Wifi
 *
 */
public class MotorControl extends RobotRun {

	public void runInstructions() {

		boolean controllingArms = false;
		int buttonValues;

		LCD.clear();
		LCD.drawString("Now controlling:", 0, 0);
		LCD.drawString("Wheels", 0, 1);

		// set your motor speeds here

		RobotStructure.getInstance().leftArm.setSpeed(800);
		RobotStructure.getInstance().rightArm.setSpeed(800);
		RobotStructure.getInstance().leftWheel.setSpeed(800);
		RobotStructure.getInstance().rightWheel.setSpeed(800);

		// TODO: Input your motors below !
		while (RunHandler.getCurrentRun().isActive()) {

			buttonValues = Button.getButtons();

			// Check bit flags for UP and DOWN brick buttons
			if ((buttonValues & Button.ID_UP) == Button.ID_UP) {
				if (controllingArms) {
					RobotStructure.getInstance().leftArm.forward();
				} else {
					RobotStructure.getInstance().leftWheel.forward();
				}
			} else if ((buttonValues & Button.ID_DOWN) == Button.ID_DOWN) {
				if (controllingArms) {
					RobotStructure.getInstance().leftArm.backward();
				} else {
					RobotStructure.getInstance().leftWheel.backward();
				}
			} else {
				if (controllingArms) {
					RobotStructure.getInstance().leftArm.stop(true);
				} else {
					RobotStructure.getInstance().leftWheel.stop(true);
				}
			}

			// Check bit flags for RIGHT and LEFT brick buttons
			if ((buttonValues & Button.ID_RIGHT) == Button.ID_RIGHT) {
				if (controllingArms) {
					RobotStructure.getInstance().rightArm.forward();
				} else {
					RobotStructure.getInstance().rightWheel.forward();
				}
			} else if ((buttonValues & Button.ID_LEFT) == Button.ID_LEFT) {
				if (controllingArms) {
					RobotStructure.getInstance().rightArm.backward();
				} else {
					RobotStructure.getInstance().rightWheel.backward();
				}
			} else {
				if (controllingArms) {
					RobotStructure.getInstance().rightArm.stop(true);
				} else {
					RobotStructure.getInstance().rightWheel.stop(true);
				}
			}

			// Swap arms and wheels when ENTER is pressed and released
			if (buttonValues == Button.ID_ENTER) {
				while (Button.getButtons() == Button.ID_ENTER && !RunHandler.getCurrentRun().isActive())
					;
				controllingArms = !controllingArms;
				LCD.clear();
				if (controllingArms)
					LCD.drawString("Arms", 0, 1);
				else
					LCD.drawString("Wheels", 0, 1);
			}

		}

	}

}
