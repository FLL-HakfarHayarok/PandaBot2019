package runs;

import functions.Color;
import lejos.hardware.lcd.LCD;
import robotUtils.RobotRun;
import robotUtils.RobotStructure;
import robotUtils.RunHandler;

public class Test extends RobotRun {

	@Override
	public void runInstructions() {
		// TODO Auto-generated method stub

		while(RunHandler.getCurrentRun().isActive())
			LCD.drawString(Color.getLight(RobotStructure.getInstance().leftColorRedSampler) + "", 0, 0);
	}

}
