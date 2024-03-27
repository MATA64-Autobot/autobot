package autobot;

import robocode.*;
import robocode.util.Utils;
import java.awt.*;

public class PaintingRobot extends AdvancedRobot {

	int scannedX = 0;
	int scannedY = 0;
	boolean scannedBot = false;

	public void run() {
		turnRadarRight(360); // ? e se n encontrar depois de 360º?
		while (true) {
			scan();
			/*
			 * There are 2 reasons to call scan() manually: 1. to scan after you stop
			 * moving. 2. to interrupt the onScannedRobot event.
			 */
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {

		// Calculate target position in degrees
		double targetPos = getHeading() + e.getBearing();
		double radarTurn = Utils.normalRelativeAngle(targetPos - getRadarHeading());
		// the normalized angle that will be in the range of [-180,180[
		

		setTurnRadarRight(radarTurn); // will not execute until you call execute() or take an action that executes.

		// Calculate the angle to the scanned robot
		double angle = Math.toRadians((targetPos) % 360);

		// Calculate the coordinates of target robot
		scannedBot = true;
		scannedX = (int) (getX() + Math.sin(angle) * e.getDistance());
		scannedY = (int) (getY() + Math.cos(angle) * e.getDistance());
	}

	// paint position of target robot
	public void onPaint(Graphics2D g) {
		if (scannedBot) {

			// Set the paint color to a red half transparent color
			g.setColor(new Color(0xff, 0x00, 0x00, 0x80));

			// Draw a line from our robot to the scanned robot
			g.drawLine(scannedX, scannedY, (int) getX(), (int) getY());

			// Draw a filled square on top of the scanned robot that covers it
			g.fillRect(scannedX - 20, scannedY - 20, 40, 40);
		}
	}
}