package com.company;

import com.company.Gameplay.Tribe;
import com.company.Interfaces.Initialisable;
import javafx.application.Platform;

import java.io.IOException;
import java.util.HashSet;

/**
 * Main timer thread; calls the status updates for all the objects changed by time
 */
public class Timer extends Thread{
    private int timeSpeed = 10;
    private int animationSubtick = 0;
    private boolean pause = true;
    private HashSet<Initialisable> screensToRefresh = new HashSet<>();

    public HashSet<Initialisable> getScreensToRefresh() {
        return screensToRefresh;
    }

    public int getAnimationSubtick() {
        return animationSubtick;
    }

    public void setTimeSpeed(int timeSpeed) {
        this.timeSpeed = timeSpeed;
     }

    public int getTimeSpeed() {
        return timeSpeed;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean getPause() {
        return pause;
    }

    public void run() {
        for (int i = 0; i < 1000; i++) {
            try {
                while (pause) {
                    sleep(1);
                }
            } catch (InterruptedException e) {
                break;
            }
            hexgame.incCurrentDay();
//            TimerScreen.updateTimerScreen();

            hexgame.processArmies();
            //do hex-level actions, - battles, resource restores etc.
            hexgame.processHexes();

            //do all the actions in tribes, - settlements&workgroups economic day etc.
            for (Tribe tribe : hexgame.getTribes()) {
                tribe.processDay();
            }
            hexgame.depleteRenewableResources();

            try {
                hexgame.logFile.flush();
            } catch (IOException e) {

            }
            try {
                //economic tick
                int totalSleep = 10000/timeSpeed;
                int numberOfAnimationTicks = hexgame.HEXSIZE;
                animationSubtick = 0;
                //animation ticks, - affect visual representation of moving objects only (armies, hunters etc.)
                for (int j = 0;  j<numberOfAnimationTicks;j+=1) {
                    Thread.sleep(totalSleep/numberOfAnimationTicks);
                    animationSubtick++;
                    Platform.runLater(()->hexgame.map.updateMapPanel());
                }
                //Refresh the screens open, - settlements etc.
                for (Initialisable screenToRefresh: screensToRefresh)
                    Platform.runLater(()->screenToRefresh.initData(screenToRefresh.getEntity(), null));

            } catch (InterruptedException e) {
                // Interrupted exception will occur if
                // the Worker object's interrupt() method
                // is called. interrupt() is inherited
                // from the Thread class.
                break;

            }
        }

    }
}
