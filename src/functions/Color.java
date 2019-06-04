package functions;

import lejos.robotics.SampleProvider;
import robotUtils.RobotStructure;

public class Color {

	public static float[] lightValues = new float[10];

	/**
	 * Detects the current light level below the front of the robot using the latest 10 samples from
	 * the color sensor
	 * @return current light level below the front of the robot (float)
	 */
	public static float getLight(SampleProvider sample) {
		float avg = 0;
		for (int i = 0; i < lightValues.length; i++) {
			sample.fetchSample(lightValues, i);
			avg += lightValues[i];
		}
		avg /= lightValues.length;
		return avg;
	}

	/**
	 * Straightens the robot on a line of a certain light level.
	 * One of the sensors must be on the line in order for this function to work
	 * @param wheelSpeed The speed the wheels should move in while straightening
	 * @param color The light level the sensors should be looking for
	 (The light level of the line; 0-black, 1-white)
	 */
	public static void straighten(int wheelSpeed, float color) {
		straighten(wheelSpeed, color, true);
	}
	
	/**
	 * Straightens the robot on a line of a certain light level.
	 * One of the sensors must be on the line in order for this function to work
	 * @param wheelSpeed The speed the wheels should move in while straightening
	 * @param color The light level the sensors should be looking for
	 (The light level of the line; 0-black, 1-white)
	 * @param forward Whether or not the robot should straighten forwards(false- straighten backwards) (optional)
	 */
	public static void straighten(int wheelSpeed, float color, boolean forward) {
		final float maxColor = color + 0.05f;
		final float minColor = color - 0.05f;

		if (getLight(RobotStructure.getInstance().leftColorRedSampler) < maxColor
				|| getLight(RobotStructure.getInstance().leftColorRedSampler) > minColor
						&& !(getLight(RobotStructure.getInstance().rightColorRedSampler) < maxColor
								|| getLight(RobotStructure.getInstance().rightColorRedSampler) > minColor)) {
			RobotStructure.getInstance().rightWheel.setSpeed(wheelSpeed);
			if (forward)
				RobotStructure.getInstance().rightWheel.forward();
			else
				RobotStructure.getInstance().rightWheel.backward();
			while (getLight(RobotStructure.getInstance().rightColorRedSampler) > maxColor
					|| getLight(RobotStructure.getInstance().rightColorRedSampler) < minColor)
				;
			RobotStructure.getInstance().rightWheel.stop();
		} else if (getLight(RobotStructure.getInstance().rightColorRedSampler) < maxColor
				|| getLight(RobotStructure.getInstance().rightColorRedSampler) > minColor
						&& !(getLight(RobotStructure.getInstance().leftColorRedSampler) < maxColor
								|| getLight(RobotStructure.getInstance().leftColorRedSampler) > minColor)) {
			RobotStructure.getInstance().leftWheel.setSpeed(wheelSpeed);
			if (forward)
				RobotStructure.getInstance().leftWheel.forward();
			else
				RobotStructure.getInstance().leftWheel.backward();
			while (getLight(RobotStructure.getInstance().leftColorRedSampler) > maxColor
					|| getLight(RobotStructure.getInstance().leftColorRedSampler) < minColor)
				;
			RobotStructure.getInstance().leftWheel.stop();
		}
	}

}
