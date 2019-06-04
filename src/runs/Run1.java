package runs;

import functions.MediumMotors;
import functions.TankDrive;
import functions.Wait;
import robotUtils.RobotRun;
import robotUtils.RobotStructure;
import robotUtils.RunHandler;

public class Run1 extends RobotRun {

	@Override
	public void runInstructions() {

		// resets the gyro
		RobotStructure.getInstance().gyro.reset();

		if (!RunHandler.getCurrentRun().isActive()) {
			return;
		}

		// moves left arm up
		MediumMotors.rotateDegrees(RobotStructure.getInstance().leftArm, 400, 750, true);

		// drives forward to the space travel mission
		TankDrive.seconds(600, 607, 2.3, true);
		TankDrive.seconds(200, 202, 1.2, true);

		if (!RunHandler.getCurrentRun().isActive()) {
			return;
		}

		Wait.waitForSeconds(0.5);

		// drives a little bit backwards
		TankDrive.seconds(-80, -80, 1, false);

		Wait.waitForSeconds(0.5);

		// drives forward
		TankDrive.seconds(110, 130, 1, false);

		Wait.waitForSeconds(1.2);

		// loop that runs twice
		for (int count = 0; count < 2; count++) {
			// puts the supply and crew payloads on the ramp
			MediumMotors.rotateDegrees(RobotStructure.getInstance().leftArm, -200, 700);

			RobotStructure.getInstance().leftArm.setSpeed(50);
			RobotStructure.getInstance().leftArm.backward();

			// drives backwards to release the payloads
			TankDrive.seconds(-50, -50, 2, false);

			RobotStructure.getInstance().leftArm.stop();

			if (!RunHandler.getCurrentRun().isActive()) {
				return;
			}

			// moves left arm up
			MediumMotors.rotateDegrees(RobotStructure.getInstance().leftArm, 800, 600);

			// drives forward to make the payloads roll down
			TankDrive.seconds(200, 200, 0.7, false);
		}

		if (!RunHandler.getCurrentRun().isActive()) {
			return;
		}

		Wait.waitForSeconds(2);

		// returns to base
		TankDrive.seconds(-500, -500, 1, true);
		MediumMotors.rotateDegrees(RobotStructure.getInstance().leftArm, -600, 600, true);
		TankDrive.seconds(-500, -500, 2.2, false);

	}

}
