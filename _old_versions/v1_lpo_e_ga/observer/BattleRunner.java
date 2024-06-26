package autobot._old_versions.v1_lpo_e_ga.observer;

import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;

public class BattleRunner {

    public static void main(String[] args) {

        // Disable log messages from Robocode
        RobocodeEngine.setLogMessagesEnabled(true);

        // Create the RobocodeEngine
        RobocodeEngine engine = new RobocodeEngine(); // Run from current working directory
//        RobocodeEngine engine = new RobocodeEngine(new java.io.File("C:/robocode")); // Run from C:/Robocode

        // Add our own battle listener to the RobocodeEngine
        engine.addBattleListener(new BattleObserver());

        // Show the Robocode battle view
        engine.setVisible(false);

        // Setup the battle specification
        int numberOfRounds = 5;
        BattlefieldSpecification battlefield = new BattlefieldSpecification(800, 600); // 800x600
//        RobotSpecification[] selectedRobots = engine.getLocalRepository("sample.RamFire,sample.Corners");
        RobotSpecification[] selectedRobots = engine.getLocalRepository();

        BattleSpecification battleSpec = new BattleSpecification(numberOfRounds, battlefield, selectedRobots);

        // Run our specified battle and let it run till it is over
        engine.runBattle(battleSpec, true); // waits till the battle finishes

        // Cleanup our RobocodeEngine
        engine.close();

        // Make sure that the Java VM is shut down properly
        System.exit(0);
    }
}
