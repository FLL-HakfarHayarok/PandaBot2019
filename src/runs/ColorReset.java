package runs;

import functions.Color;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import robotUtils.RobotRun;
import robotUtils.RobotStructure;

public class ColorReset extends RobotRun {
	public static float black = 0;
	public static float white = 0.4f;

	@Override
	public void runInstructions() {
		// TODO Auto-generated method stub

		LCD.drawString("Right on Black", 0, 0);
		Button.waitForAnyPress();
		black = Color.getLight(RobotStructure.getInstance().rightColorRedSampler);
		
		LCD.clear();
		LCD.drawString("Right on White", 0, 0);
		Button.waitForAnyPress();
		white = Color.getLight(RobotStructure.getInstance().rightColorRedSampler);
	}

}
