/*
 * Copyright (c) 2001-2023 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://robocode.sourceforge.io/license/epl-v10.html
 */
package autobot._old_versions.v0_heuristicas;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import static java.awt.event.KeyEvent.*;
import static robocode.util.Utils.normalAbsoluteAngle;
import static robocode.util.Utils.normalRelativeAngle;

/**
 * Interactive - a sample robot by Flemming N. Larsen.
 * <p>
 * This is a robot that is controlled using the arrow keys and mouse only.
 * <p>
 * Keys: - W or arrow up: Move forward - S or arrow down: Move backward - A or
 * arrow right: Turn right - D or arrow left: Turn left Mouse: - Moving: Moves
 * the aim, which the gun will follow - Wheel up: Move forward - Wheel down:
 * Move backward - Button 1: Fire a bullet with power = 1 - Button 2: Fire a
 * bullet with power = 2 - Button 3: Fire a bullet with power = 3
 * <p>
 * The bullet color depends on the fire power: - Power = 1: Yellow - Power = 2:
 * Orange - Power = 3: Red
 * <p>
 * Note that the robot will continue firing as long as the mouse button is
 * pressed down.
 * <p>
 * By enabling the "Paint" button on the robot console window for this robot, a
 * cross hair will be painted for the robots current aim (controlled by the
 * mouse).
 *
 * @author Flemming N. Larsen (original)
 * @version 1.2
 * @since 1.3.4
 */
public class Edit_Interactive extends AdvancedRobot {

    // Move direction: 1 = move forward, 0 = stand still, -1 = move backward
    int moveDirection;

    // Turn direction: 1 = turn right, 0 = no turning, -1 = turn left
    int turnDirection;

    // Amount of pixels/units to move
    double moveAmount;

    // The coordinate of the aim (x,y)
    int aimX, aimY;

    // Fire power, where 0 = don't fire
    int firePower;

    Point2D robotLocation;

    // Called when the robot must run
    public void run() {

        // Sets the colors of the robot
        // body = black, gun = white, radar = red
        setColors(Color.BLACK, Color.WHITE, Color.RED);

        // Loop forever
        for (; ; ) {
            robotLocation = new Point2D.Double(getX(), getY());
            turnRange();
            // Sets the robot to move forward, backward or stop moving depending
            // on the move direction and amount of pixels to move
            setAhead(moveAmount * moveDirection);

            // Decrement the amount of pixels to move until we reach 0 pixels
            // This way the robot will automatically stop if the mouse wheel
            // has stopped it's rotation
            moveAmount = Math.max(0, moveAmount - 1);

            // Sets the robot to turn right or turn left (at maximum speed) or
            // stop turning depending on the turn direction
            setTurnRight(45 * turnDirection); // degrees

            // Turns the gun toward the current aim coordinate (x,y) controlled by
            // the current mouse coordinate
            double angle = normalAbsoluteAngle(Math.atan2(aimX - getX(), aimY - getY()));

            setTurnGunRightRadians(normalRelativeAngle(angle - getGunHeadingRadians()));

            // Fire the gun with the specified fire power, unless the fire power = 0
            if (firePower > 0) {
                setFire(firePower);
            }

            // Execute all pending set-statements
            execute();

            // Next turn is processed in this loop..
        }
    }

    public void turnRange() {

        // get Relative location
        double xLimit = getBattleFieldWidth() / 2;
        double yLimit = getBattleFieldHeight() / 2;

        double x = robotLocation.getX() - xLimit;
        double y = robotLocation.getY() - yLimit;

        double xBase = Math.toDegrees(Math.asin(-1 * Math.signum(x)));  // 90 ~ -90
        double yBase = Math.toDegrees(Math.acos(-1 * Math.signum(y)));  // 0 ~ 180
        yBase *= Math.signum(x);    // 1Q 180 // 2Q -180

        double xDist = Math.signum(x) * (xLimit - Math.abs(x));
        double yDist = Math.signum(y) * (yLimit - Math.abs(y));

        double xExtraAngle = Math.toDegrees(Math.atan(xDist / y));
        double yExtraAngle = Math.toDegrees(Math.atan(yDist / x));

        out.print("x: " + xExtraAngle);
        out.println(" y: " + yExtraAngle);

//        double xAngle = xBase + xExtraAngle;
//        double yAngle = yBase + yExtraAngle;

//        out.print("x: " + xAngle);
//        out.println(" y: " + yAngle);
//        out.println();

        //Base
        //Q1: 90 ~ 180  Q2: -90 ~ 180 *
        //Q3: 90 ~ 0 *  Q4: -90 ~ 0

        //Extra
        //Q1: 90-y ~ 180+x  Q2: -90+y ~ 180-x *
        //Q3: 90+y ~ 0-x *  Q4: -90-X ~ 0+Y


    }

    public void onScannedRobot(ScannedRobotEvent e) {

//        out.print("H: " + getHeading() + " ");
//        out.println("B: " + e.getBearing());

    }

    public double getDistance(Point2D A, Point2D B) {
        return Point2D.distance(A.getX(), A.getY(), B.getX(), B.getY());
    }

    // Called when a key has been pressed
    public void onKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case VK_UP:
            case VK_W:
                // Arrow up key: move direction = forward (infinitely)
                moveDirection = 1;
                moveAmount = Double.POSITIVE_INFINITY;
                break;

            case VK_DOWN:
            case VK_S:
                // Arrow down key: move direction = backward (infinitely)
                moveDirection = -1;
                moveAmount = Double.POSITIVE_INFINITY;
                break;

            case VK_RIGHT:
            case VK_D:
                // Arrow right key: turn direction = right
                turnDirection = 1;
                break;

            case VK_LEFT:
            case VK_A:
                // Arrow left key: turn direction = left
                turnDirection = -1;
                break;
        }
    }

    // Called when a key has been released (after being pressed)
    public void onKeyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case VK_UP:
            case VK_W:
            case VK_DOWN:
            case VK_S:
                // Arrow up and down keys: move direction = stand still
                moveDirection = 0;
                moveAmount = 0;
                break;

            case VK_RIGHT:
            case VK_D:
            case VK_LEFT:
            case VK_A:
                // Arrow right and left keys: turn direction = stop turning
                turnDirection = 0;
                break;
        }
    }

    // Called when the mouse wheel is rotated
    public void onMouseWheelMoved(MouseWheelEvent e) {
        // If the wheel rotation is negative it means that it is moved forward.
        // Set move direction = forward, if wheel is moved forward.
        // Otherwise, set move direction = backward
        moveDirection = (e.getWheelRotation() < 0) ? 1 : -1;

        // Set the amount to move = absolute wheel rotation amount * 5 (speed)
        // Here 5 means 5 pixels per wheel rotation step. The higher value, the
        // more speed
        moveAmount += Math.abs(e.getWheelRotation()) * 5;
    }

    // Called when the mouse has been moved
    public void onMouseMoved(MouseEvent e) {
        // Set the aim coordinate = the mouse pointer coordinate
        aimX = e.getX();
        aimY = e.getY();
    }

    // Called when a mouse button has been pressed
    public void onMousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            // Button 3: fire power = 3 energy points, bullet color = red
            firePower = 3;
            setBulletColor(Color.RED);
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            // Button 2: fire power = 2 energy points, bullet color = orange
            firePower = 2;
            setBulletColor(Color.ORANGE);
        } else {
            // Button 1 or unknown button:
            // fire power = 1 energy points, bullet color = yellow
            firePower = 1;
            setBulletColor(Color.YELLOW);
        }
    }

    // Called when a mouse button has been released (after being pressed)
    public void onMouseReleased(MouseEvent e) {
        // Fire power = 0, which means "don't fire"
        firePower = 0;
    }

    // Called in order to paint graphics for this robot.
    // "Paint" button on the robot console window for this robot must be
    // enabled in order to see the paintings.
    public void onPaint(Graphics2D g) {
        // Draw a red cross hair with the center at the current aim
        // coordinate (x,y)
        g.setColor(Color.RED);
        g.drawOval(aimX - 15, aimY - 15, 30, 30);
        g.drawLine(aimX, aimY - 4, aimX, aimY + 4);
        g.drawLine(aimX - 4, aimY, aimX + 4, aimY);
    }
}
