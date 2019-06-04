package runs;

import robotUtils.RobotRun;
import robotUtils.RobotStructure;

public class GyroReset extends RobotRun {

	@Override
	public void runInstructions() {
		// TODO Auto-generated method stub
		
		RobotStructure.getInstance().gyro.reset();
	}

}
