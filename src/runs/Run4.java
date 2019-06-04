package runs;

import functions.Gyro;
import functions.MediumMotors;
import functions.Stats;
import functions.TankDrive;
import functions.Wait;
import robotUtils.RobotRun;
import robotUtils.RobotStructure;
import robotUtils.RunHandler;

public class Run4 extends RobotRun {

	@Override
	public void runInstructions() {
		// TODO Auto-generated method stub

		RobotStructure.getInstance().gyro.reset();
		
		if (!RunHandler.getCurrentRun().isActive()) {
			return;
		}
		
		Gyro.followerDegrees(500, Stats.cmToDegrees(115), 0, 20, true);
		Gyro.followerUntilColor(100, ColorReset.black, RobotStructure.getInstance().rightColor, 0, 10, false);
		
		if (!RunHandler.getCurrentRun().isActive()) {
			return;
		}
		
		Gyro.straightenBothWheelsWithMaxTurns(100, -50, 1);
		
		if (!RunHandler.getCurrentRun().isActive()) {
			return;
		}
		
		Gyro.followerUntilColor(400, ColorReset.white, RobotStructure.getInstance().leftColor, -50, 10, true);
		Gyro.followerUntilColor(100, ColorReset.black, RobotStructure.getInstance().leftColor, -50, 10, true);
		Gyro.followerDegrees(100, 100, -50, 10, false);
		
		if (!RunHandler.getCurrentRun().isActive()) {
			return;
		}
		
		Gyro.straightenBothWheelsWithMaxTurns(100, -90, 1);
		
				
		if (!RunHandler.getCurrentRun().isActive()) { 
			return;
		}

		TankDrive.seconds(500, 500, 1, false);
		TankDrive.seconds(150, 150, 0.7, false);
		
		if (!RunHandler.getCurrentRun().isActive()) {
			return;
		}
		
		MediumMotors.rotateDegrees(RobotStructure.getInstance().rightArm, 800, 100);
		
		if (!RunHandler.getCurrentRun().isActive()) {
			return;
		}

		Wait.waitForSeconds(0.5);
		
		TankDrive.degrees(-315, -300, Stats.cmToDegrees(40));
		
		if (!RunHandler.getCurrentRun().isActive()) {
			return;
		}
		
		MediumMotors.rotateDegrees(RobotStructure.getInstance().rightArm, -800, 95);
				
		TankDrive.seconds(-20, -20, 3, false);
		
		if (!RunHandler.getCurrentRun().isActive()) {
			return;
		}
		
		Gyro.followerDegrees(200, Stats.cmToDegrees(13), -90, 10, false);

		Gyro.straightenBothWheelsWithMaxTurns(100, -130, 1);

		TankDrive.degrees(-400, -400, Stats.cmToDegrees(35));
		Wait.waitForSeconds(0.5);
		
		Gyro.followerDegrees(500, Stats.cmToDegrees(43), -130, 10, true);
		Gyro.straightenBothWheels(100, -145);
		Gyro.followerDegrees(500, Stats.cmToDegrees(51), -145, 10, false);

		Gyro.straightenBothWheels(100, -176);

		Gyro.followerDegrees(500, Stats.cmToDegrees(85), -176, 10, false);

		MediumMotors.rotateDegrees(RobotStructure.getInstance().leftArm, -400, 100);
		MediumMotors.rotateDegrees(RobotStructure.getInstance().leftArm, 400, 200);
	}

}
