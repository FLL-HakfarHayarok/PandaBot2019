package runs;

import functions.Color;
import functions.Gyro;
import functions.MediumMotors;
import functions.Stats;
import functions.TankDrive;
import robotUtils.RobotRun;
import robotUtils.RobotStructure;

public class Run3 extends RobotRun {

	@Override
	public void runInstructions() {
		// TODO Auto-generated method stub
		RobotStructure.getInstance().gyro.reset();

		Gyro.followerDegrees(400, Stats.cmToDegrees(80), 0, 10, true);
		Gyro.followerUntilColor(100, ColorReset.black, -3, 10, true);
		Color.straighten(50, ColorReset.black);

		//MediumMotors.rotateDegrees(RobotStructure.getInstance().rightArm, 800, 550, false);
		//MediumMotors.rotateDegrees(RobotStructure.getInstance().rightArm, 300, 300, true);

		Gyro.followerSecs(500, 0.8, 0, 10, true);

		//MediumMotors.rotateDegrees(RobotStructure.getInstance().rightArm, -400, 600);

		TankDrive.secs(-700, -700, 3);
	}

}
