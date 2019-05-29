package com.company;

import com.company.Enumerations.TerrainType;
import com.company.Gameplay.*;
import com.company.Interfaces.IMoveable;
import com.company.Interfaces.Initialisable;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

//import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class HexMap/* extends Application*/ {
    private static Hex[][] board = new Hex[hexgame.BSIZE][hexgame.BSIZE];
    private static Group mapPanel = new Group();
    public Stage primaryStage;

//    public HexMap (Mediator mediator){
//        this.primaryStage = mediator.primaryStage;
//    }
//
//    public void start(Stage stage) throws Exception {
//        initMap();
//        drawMapPanel();
//    }

    public static Hex[][] getBoard() {
        return board;
    }

    public static void setBoard(Hex[][] board) {
        HexMap.board = board;
    }
//    private static int x = hexgame.BSIZE;
//    private static int y = hexgame.BSIZE;

    /**
     * Method to generate the terrain, - northern regions have more forest tiles, southern, - more desert
     */
    public static void initMap(){

        TerrainType curTerrainType;
        for (int i=0;i<hexgame.BSIZE;i++) {
            for (int j=0;j<hexgame.BSIZE;j++) {
                int TerType = (int)(Math.random()*3*j/hexgame.BSIZE);
                switch (TerType) {
                    case 0:
                        curTerrainType=TerrainType.Forest;
                        break;
                    case 1:
                        curTerrainType=TerrainType.Plain;
                        break;
                    default:
                        curTerrainType=TerrainType.Desert;
                        break;
                }
                board[i][j] = new Hex(curTerrainType,i,j);
            }
        }

    }

    private static int[] getXYAnimationOffsets(IMoveable movingGroup) {
        Hex n = movingGroup.getLocation();
        Hex newLocation = (HexMap.getPath(movingGroup.getLocation(), movingGroup.getDestination()).size() > 1) ? HexMap.getPath(movingGroup.getLocation(), movingGroup.getDestination()).get(1) : n;//getting the nex step in the group path

        int[] result = new int[2];
        int wgXOffset = 0;
        int wgYOffset = 0;


        //            \|/
        //             O
        //            /|\ - possible directions;

        //            //from columns with even X bottom corners of 3X3 square around the current location are unreachable;
        //            //from columns with odd X top corners of 3X3 square around the current location are unreachable;
        //            //Thus, we can move horizontally only if we either didn't move vertically or the corner needed is reachable
        //            if doing Y offset together with X, going vertically for half hex only
        //            /*
        //            even X: +++      Odd X:  -+-
        //                    +O+              +O+
        //                    -+-              +++
        //             */

        if (!newLocation.equals(n)) {
            if (n.getX() > newLocation.getX())
                wgXOffset = -hexgame.getMainTimer().getAnimationSubtick();

            if (n.getX() < newLocation.getX())
                wgXOffset = hexgame.getMainTimer().getAnimationSubtick();

            if (n.getX() == newLocation.getX()) {
                if (n.getY() > newLocation.getY())
                    wgYOffset = -hexgame.getMainTimer().getAnimationSubtick();
                else if (n.getY() < newLocation.getY())
                    wgYOffset = hexgame.getMainTimer().getAnimationSubtick();
            } else if (n.getX() % 2 == 1) {
                if (n.getY() == newLocation.getY())
                    wgYOffset = -hexgame.getMainTimer().getAnimationSubtick() / 2;
                else if (n.getY() < newLocation.getY())
                    wgYOffset = hexgame.getMainTimer().getAnimationSubtick() / 2;

            } else if (n.getX() % 2 == 0) {
                if (n.getY() == newLocation.getY())
                    wgYOffset = hexgame.getMainTimer().getAnimationSubtick() / 2;
                else if (n.getY() > newLocation.getY())
                    wgYOffset = -hexgame.getMainTimer().getAnimationSubtick() / 2;

            }

        }
        result[0] = wgXOffset;
        result[1] = wgYOffset;
        return result;

    }
    /**
     * method to add/refresh dynamic graphic objects on map (camps, working groups, armies etc.)
     */
    public static void updateMapPanel() {
        int height = hexgame.HEXSIZE;
        //1.73205 = sqrt(3) = 2*cos(30)
        double xOffsetPerTile = 1.73205*height/2.0;
        double side =height/1.73205;
        double halfSide = side/2;
        double halfHeight = (double) height/2;

        hexgame.mainScreen.updateControls();

            for (Tribe tribe: hexgame.getTribes())
                //Add images of settlements, working groups, armies etc.
                for (Settlement settlement: tribe.getSettlements()) {

                    //Firstly, add images for settlements
                    ImageView image = new ImageView();
                    image.setUserData(settlement);
                    image.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (hexgame.activeObject.equals(settlement) || !settlement.getOwner().isHuman()) {
                                //if this settlement is active already, or don't belong to us, open its details screen
                                try {
                                    FXMLLoader fxmlLoader;
                                    if (settlement.getOwner().isHuman())
                                        fxmlLoader = new FXMLLoader(getClass().getResource("GUI/SettlementScreen.fxml"));
                                    else
                                        fxmlLoader = new FXMLLoader(getClass().getResource("GUI/EnemySettlement.fxml"));

                                    Parent root = fxmlLoader.load();
                                    ((Initialisable) (fxmlLoader.getController())).initData(settlement, null);
                                    //add details screen to collection of screens dynamically refreshed
                                    hexgame.getMainTimer().getScreensToRefresh().add(fxmlLoader.getController());
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(root));
                                    stage.setTitle(settlement.getName());
                                    stage.show();
                                } catch (IOException e) {
                                    e.getLocalizedMessage();
                                }
                                ;
                            } else {
                                //hexgame.activeSettlement = settlement;
                                hexgame.activeObject = settlement;
                                hexgame.mainScreen.updateControls();
                            }
                        }
                    });

                    if (!settlement.getOwner().isHuman())
                        image.setImage(hexgame.settlementImage);
                    else
                        image.setImage(hexgame.playerSettlementImage);

                    image.setX(halfSide + settlement.getLocation().getX() * xOffsetPerTile - image.getFitWidth() / 2);
                    double verticalOffset = height * (settlement.getLocation().getY()) + (settlement.getLocation().getX() % 2 == 1 ? halfHeight : 0);
                    image.setY(verticalOffset + halfHeight / 2 + image.getFitHeight() / 2);
                    if (settlement.getImageView()==null) {
                        mapPanel.getChildren().add(image);
                        settlement.reflectedOnMap = true;
                        settlement.setImageView(image);
                    }

                    //Now, mark working hexes (camps)
                    HashSet<Hex> workingHexes = new HashSet<Hex>(settlement.getWorkingHexes());
                    for (Hex workingHex : workingHexes) {
                        if (workingHex.getImageView()==null && workingHex.getSettlement()==null) {
                            image = new ImageView();
                            image.setUserData(settlement);
                            image.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                	//If hex belongs to currently active settlement, remove the patrol
                                	if (hexgame.activeObject.equals(settlement)) {
	                                    mapPanel.getChildren().remove(workingHex.getImageView());
	                                    workingHex.setImageView(null);
	                                    settlement.abandonWorkingHex(workingHex);
                                	} else
                                	//If active object is army, and we are clicking foreign hex, launch the attack
                                	if (hexgame.activeObject.getClass().getName().contains("Army"))
                                		if(!settlement.getOwner().equals(((Army)hexgame.activeObject).getOwner()))
                                			((Army)hexgame.activeObject).launchAttack(workingHex);
                                		
                                    //settlement.getWorkingHexes().remove(workingHex);
                               }
                            });

                            if (!settlement.getOwner().isHuman())
                                image.setImage(hexgame.campImage);
                            else
                                image.setImage(hexgame.playerCampImage);

                            verticalOffset = height *  workingHex.getY() + ( workingHex.getX()%2 ==1 ? halfHeight : 0);
                            image.setX(halfSide + workingHex.getX() * xOffsetPerTile/* - image.getFitWidth() / 2*/);
                            image.setY(verticalOffset + halfHeight/2 /*+ image.getFitHeight() / 2*/);
                            if (workingHex.getImageView()==null) {
                                mapPanel.getChildren().add(image);
                                workingHex.setImageView(image);
                            }
                        }
                    }

                    //Now, add images for working groups
                    HashSet<WorkingGroup> workingGroups = new HashSet<>(settlement.getWorkingGroups());
                    for (WorkingGroup workingGroup : workingGroups) {
                        if (workingGroup.getPeople()==0)
                            continue;
                        ImageView imageView;
                        if (workingGroup.getImageView() == null) {
                            imageView = new ImageView();
                            workingGroup.setImageView(imageView);
                            imageView.setImage(hexgame.hunterImage);
                            mapPanel.getChildren().add(imageView);
                        } else
                            imageView = workingGroup.getImageView();

                        Hex n = workingGroup.getLocation();

                        verticalOffset = height * (n.getY()) + (n.getX() % 2 == 1 ? halfHeight : 0);
                        int[] animationOffsets = getXYAnimationOffsets(workingGroup);
                        imageView.relocate(halfSide + (n.getX()) * xOffsetPerTile + animationOffsets[0], verticalOffset + animationOffsets[1] + halfHeight / 2 );
                    }
                    HashSet<Army> armies = new HashSet<>(settlement.getArmies());
                    for (Army army: armies) {
                        ImageView imageView;
                        if (army.getImageView() == null) {
                            imageView = new ImageView();
                            imageView.setUserData(army);
                            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                	hexgame.mainScreen.setActiveArmy(army);
                               }
                            });
                            army.setImageView(imageView);
                            imageView.setImage(hexgame.armyImage);
                            mapPanel.getChildren().add(imageView);
                        } else
                            imageView = army.getImageView();

                        Hex n = army.getLocation();

                        verticalOffset = height * (n.getY()) + (n.getX() % 2 == 1 ? halfHeight : 0);
                        int[] animationOffsets = getXYAnimationOffsets(army);
                        imageView.relocate(halfSide + (n.getX()) * xOffsetPerTile + animationOffsets[0], verticalOffset + animationOffsets[1] + halfHeight / 2 );

                    }

                }
            showMessages();

    }
    public static void drawMapPanel() {
        mapPanel = new Group();
        int height = hexgame.HEXSIZE;
        //1.73205 = sqrt(3) = 2*cos(30)
        double xOffsetPerTile = 1.73205*height/2.0;
        double side =height/1.73205;
        double halfSide = side/2;
        double halfHeight = (double) height/2;
        //For every hex in a board array generate polygon object, with a corresponding fill (by terrain type) and objects
        for (int i = 0; i<hexgame.BSIZE; i++)
            for (int j = 0; j<hexgame.BSIZE; j++) {
                Polygon hex = new Polygon();
                double verticalOffset = height * j + (i%2 ==1 ? halfHeight : 0);
                //now list all vertices of the hexagon starting from the top left clockwise
                hex.getPoints().addAll(new Double[]{
                        halfSide+i*xOffsetPerTile, verticalOffset,
                        halfSide+side+i*xOffsetPerTile, verticalOffset,
                        side*2 + i*xOffsetPerTile, verticalOffset+halfHeight,
                        halfSide+side+i*xOffsetPerTile, verticalOffset+height,
                        halfSide+i*xOffsetPerTile, verticalOffset+height,
                        i*xOffsetPerTile, verticalOffset+halfHeight,
                });
                Hex hexObject = board[i][j];
                hex.setUserData(hexObject);
                hex.setFill (hexObject.getTerrain().getColor());
                hex.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (hexgame.mainScreen.isNewSettlementMode()) {
                            hexgame.getPlayer().getSettlements().add(new Settlement("New Settlement",hexObject,hexgame.getPlayer()));
                            hexgame.mainScreen.setNewSettlementMode(false);
                            updateMapPanel();
                        }else {
                            if (hexgame.activeObject != null && hexgame.activeObject.getClass().getName().contains("Settlement") && hexObject.getWorkingGroupsCamps().size()==0)
                            	if (hexObject.getWorkingGroupsCamps().isEmpty())
                            	if (((Settlement)hexgame.activeObject).hasTroopsToPatrol())
                            		((Settlement)hexgame.activeObject).sendExpedition(hexObject, 0, 0);
                        }
                    }
                });

                hex.setOnMouseEntered(e->(hexObject.getHexScreen()/*.getController()*/).show());
                hex.setOnMouseExited(e->(hexObject.getHexScreen()/*.getController()*/).close());
                mapPanel.getChildren().add(hex);
        };
       if (hexgame.mainScreen!=null)
        hexgame.mainScreen.updateMapPanel(mapPanel);


    }
    public static Group getMapPanel() {
        drawMapPanel();
        //updateMapPanel();
        Group result = mapPanel;

        return result;
    }
    /**
     * method to show messages from the message queue
     */
    public static void showMessages() {
    	int counter = 0;
	    for (Message message : hexgame.messageQueue) {
	    	Circle messageBubble = new Circle (50);
	    	messageBubble.setCenterX(50);
	    	messageBubble.setCenterY(50 + counter*100);
	    	messageBubble.setUserData(message);
	    	messageBubble.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
                    Stage stage = new Stage();
                    stage.setTitle("Battle report");
                    stage.show();
                    //hexgame.messageQueue.remove(messageBubble)
	            	mapPanel.getChildren().remove(messageBubble);
	            }
	    	}
	    	);
	    	mapPanel.getChildren().add(messageBubble);
	    	hexgame.messageQueue.remove(message);
	    	counter++;
	    	//Platform.runLater(()->hexgame.map.updateMapPanel());
	    }
    }
    	
    public static int getPathLength (Hex start, Hex finish) {
        int  result = 0;
        Hex curLocation = start;
        int curX = curLocation.getX();
        int curY = curLocation.getY();
        while (!curLocation.equals(finish)) {
            if (curX < finish.getX())
                curX++;
            else if (curX > finish.getX())
                curX--;

            //from columns with even X bottom corners of 3X3 square around the current location are unreachable;
            //from columns with odd X top corners of 3X3 square around the current location are unreachable;
            //Thus, we can move horizontally only if we either didn't move vertically or the corner needed is reachable

            /*
            even X: +++      Odd X:  -+-
                    +O+              +O+
                    -+-              +++
             */

            if (curY < finish.getY() && (curX == finish.getX() || curX % 2 == 0))
                curY++;

            if (curY > finish.getY() && (curX == finish.getX() || curX % 2 == 1))
                curY--;

            curLocation = HexMap.getBoard()[curX][curY];
            result++;
        }
        return result;
    }
    public static List<Hex> getPath (Hex start, Hex finish) {
        LinkedList<Hex> result = new LinkedList<>();
        result.add(start);
        Hex curLocation = start;
        int curX = curLocation.getX();
        int curY = curLocation.getY();
        while (!curLocation.equals(finish)) {
            if (curX < finish.getX())
                curX++;
            else if (curX > finish.getX())
                curX--;

            //from columns with even X bottom corners of 3X3 square around the current location are unreachable;
            //from columns with odd X top corners of 3X3 square around the current location are unreachable;
            //Thus, we can move horizontally only if we either didn't move vertically or the corner needed is reachable

            /*
            even X: +++      Odd X:  -+-
                    +O+              +O+
                    -+-              +++
             */

            if (curY < finish.getY() && (curX == finish.getX() || curX % 2 == 0))
                curY++;

            if (curY > finish.getY() && (curX == finish.getX() || curX % 2 == 1))
                curY--;

            curLocation = HexMap.getBoard()[curX][curY];
            result.add(curLocation);
        }

        return result;    }
}
