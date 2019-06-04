package runs;

import functions.Gyro;
import functions.Stats;
import functions.TankDrive;
import functions.Wait;
import lejos.hardware.lcd.LCD;
import robotUtils.RobotRun;
import robotUtils.RobotStructure;
import robotUtils.RunHandler;

public class Run2 extends RobotRun {

	@Override
	public void runInstructions() {
		// resets the gyro
		RobotStructure.getInstance().gyro.reset();

		// frees itself from the launch
		Gyro.followerDegrees(250, Stats.cmToDegrees(11), 0, 10, false);

		if (!RunHandler.getCurrentRun().isActive()) {
			return;
		}

		TankDrive.untilGyroDegrees(500, 290, 45);

		Wait.waitForSeconds(0.5);
		LCD.clear();
		LCD.drawInt(Gyro.getAngle(), 0, 0);

		if (!RunHandler.getCurrentRun().isActive()) {
			return;
		}

		Wait.waitForSeconds(0.5);

		TankDrive.seconds(105, 100, 7, true);
		TankDrive.seconds(-150, -150, 1, true);
		TankDrive.seconds(150, 150, 1.5, true);
		TankDrive.seconds(-150, -150, 0.7, true);
		TankDrive.untilGyroDegrees(700, 150, 75);
		TankDrive.seconds(-200, -200, 1, false);
		Gyro.straightenBothWheelsWithMaxTurns(150, 180, 1);

		/*
		 * Gyro.followerDegrees(300, Stats.cmToDegrees(37), 45, 10, false);
		 * 
		 * Gyro.followerSecs(-200, 0.7, 45, 5, false);
		 * 
		 * if (!RunHandler.getCurrentRun().isActive()) { return; }
		 * 
		 * TankDrive.seconds(600, 100, 1.6, false);
		 */
		TankDrive.seconds(600, 600, 2.6, false);

	}

}
