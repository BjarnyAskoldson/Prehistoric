package com.company;

import com.company.GUI.MainScreen;
import com.company.Gameplay.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

/**********************************
 This is the main class of a Java program to play a game based on hexagonal tiles.
 The mechanism of handling hexes is in the file hexmech.java.
 Written by: M.H.
 Date: December 2012
 ***********************************/

public class hexgame
{
    private hexgame() {
        initGame();
//        if (activeSettlement != null)
//            createAndShowGUI();
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new hexgame();
            }
         });
    }

    //constants and global variables
    final static Color COLOURGRID =  Color.BLACK;
    public final static int BSIZE = 10; //board size.
    public final static int HEXSIZE = 60;	//hex size in pixels
    final static int BORDERS = 15;
    final static int SCRSIZE = HEXSIZE * (BSIZE + 1) + BORDERS*3; //screen size (vertical dimension).
    final static int OPPONENTSNUMBER = 10;//number of AI tribes
    public final static int SETTLEMENTLOCALWILDGAME = 50;//Wildgame available at settlements' tiles
    public final static double ResourceRestore = 0.05;//speed of restore for renewable resources
    public final static double WorkersDeathTool = .05;//basic damage inflicted by working groups
    public final static double fairTaxLevel = .05;//basic damage inflicted by working groups

//    static Equipment defaultEquipment = Equipment.StoneBlades;
    //Percent of newborns
    public final static double BIRTHRATE = 0.05;
    //Percent of starvation per unsatisfied vital need
    public final static double STARVATIONRATE = 0.10;
    //employment rate that cause people to abandon profession
    public final static double unemploymentRate = 0.7;

    public static Image settlementImage = null;
    public static Image playerSettlementImage = null;
    public static Image playerCampImage = null;
    public static Image hunterImage = null;
    public static Image campImage = null;
    public static Image battleImage = null;
    public static Image armyImage = null;
    public static Image playerArmyImage = null;


    public static BufferedWriter logFile =  null;
    public static HashSet<Message> messageQueue = new HashSet<>();

//    public static Hex[][] board = new Hex[BSIZE][BSIZE];

    private static Tribe player;
    public static HexMap map = new HexMap(/*new Mediator()*/);
    public static MainScreen mainScreen;
    public static Stage mainStage;
//    public static Settlement activeSettlement = null;
    public static Object activeObject;
    private static HashSet <Settlement> settlements = new HashSet<>();
    public static HashSet<Tribe> tribes = new HashSet<>();
    public static HashSet<Settlement> getSettlements (){
        return settlements;
    }
    private static int turn;
    public static int getTurnNumber() {
        return turn;
    }
    private static long currentDay = 0;
//    public static MapScreen mapScreen = new MapScreen();

    public static void processArmies() {
        for (Tribe tbibe : tribes)
            for (Settlement settlement: tbibe.getSettlements())
                for (Army army: settlement.getArmies())
                    army.processDay();
    }
    public static void processHexes() {
        for (int x = 0; x<BSIZE; x++)
            for (int y = 0; y<BSIZE; y++) {
                HexMap.getBoard()[x][y].processDay();
//                Hex location = board[x][y];
//                if (location.getWorkingGroups().size()>1) {
//                    long tribesOnHex = location.getWorkingGroups().stream().map(WorkingGroup::getHomeBase).map(Settlement::getOwner).distinct().count();
//                    HashSet<WorkingGroup> groupsOnLocation = new HashSet<>(location.getWorkingGroups());
//                    for (WorkingGroup wg : groupsOnLocation)
//                        if (wg.isAggressive()&&tribesOnHex>1)
//                            location.battle();
//                            break;

//            }
        }

    }

    public static void depleteRenewableResources() {
        for (int x = 0; x < BSIZE; x++)
            for (int y = 0; y < BSIZE; y++) {
                Hex location = HexMap.getBoard()[x][y];
                if (location.getWorkingGroupsCamps().size() > 1)
                    location.depleteRenewableResources();
            }
    }

    public static HashSet<Tribe> getTribes() {
        return tribes;
    }
    private static Timer mainTimer = new Timer();
    public static void stopGame() {
        mainTimer.interrupt();
    }

    public static Timer getMainTimer() {
        return mainTimer;
    }

    public static Tribe getPlayer() {
        Tribe player = hexgame.tribes.stream().filter(a->a.isHuman()).findFirst().get();
        return player;

    }

    public static long getCurrentDay() {
        return currentDay;
    }
    public static void incCurrentDay() {
        currentDay++;
    }
    /**
     * Method to populate the map before the game starts, - generates terrain and place settlements
     */
    public static void initGame(){
        tribes.add(new Tribe("Mighty Donuts", "Donutwille", true));
        //activeSettlement = hexgame.getTribes().stream().filter(a->a.isHuman()).map(a->a.getSettlements().get(0)).findFirst().get();
        activeObject = hexgame.getTribes().stream().filter(a->a.isHuman()).map(a->a.getSettlements().get(0)).findFirst().get();

        try
        {
//            settlementImage = ImageIO.read(new File("C:\\Users\\Alex Miliukov\\IdeaProjects\\Prehistoric\\src\\Settlement.png"));
//            hunterImage = ImageIO.read(new File("C:\\Users\\Alex Miliukov\\IdeaProjects\\Prehistoric\\src\\hunter.png"));
            settlementImage = new Image("file:C:\\Users\\Alex Miliukov\\Documents\\IdeaProjects\\Prehistoric\\src\\com\\company\\Images\\simpleSettlement.png");
            playerSettlementImage =  new Image("file:C:\\Users\\Alex Miliukov\\Documents\\IdeaProjects\\Prehistoric\\src\\com\\company\\Images\\simplePlayerSettlement.png");
            armyImage = new Image("file:C:\\Users\\Alex Miliukov\\Documents\\IdeaProjects\\Prehistoric\\src\\com\\company\\Images\\simpleArmy.png");
            playerArmyImage =  new Image("file:C:\\Users\\Alex Miliukov\\Documents\\IdeaProjects\\Prehistoric\\src\\com\\company\\Images\\simplePlayerArmy.png");
            playerCampImage =  new Image("file:C:\\Users\\Alex Miliukov\\Documents\\IdeaProjects\\Prehistoric\\src\\com\\company\\Images\\simplePlayerCamp.png");
            hunterImage =  new Image("file:C:\\Users\\Alex Miliukov\\Documents\\IdeaProjects\\Prehistoric\\src\\com\\company\\Images\\simpleHunter.png");
            campImage =  new Image("file:C:\\Users\\Alex Miliukov\\Documents\\IdeaProjects\\Prehistoric\\src\\com\\company\\Images\\simpleCamp.png");
            battleImage =  new Image("file:C:\\Users\\Alex Miliukov\\Documents\\IdeaProjects\\Prehistoric\\src\\com\\company\\Images\\simpleBattle.png");
            logFile = new BufferedWriter(new FileWriter("C:\\Users\\Alex Miliukov\\Documents\\IdeaProjects\\Prehistoric\\out\\log.txt"));
        }
        catch (IOException e){}

        mainTimer.start();

        //now create computer players
        for (int i = 1; i<=OPPONENTSNUMBER; i++)
            tribes.add (new Tribe("Player "+i,"Capital "+i,false));

     }

}