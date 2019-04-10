package com.company;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class Message {
    private String header = new String();
    private List<String> body = new LinkedList<>();

    public List<String> getBody() {
        return body;
    }

    public String getHeader() {
        return header;
    }

    public void setBody(List<String> body) {
        this.body = body;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void showOnScreen() {
    	hexgame.messageQueue.add(this);
 /*   	Circle messageBubble = new Circle (50);
    	messageBubble.setCenterX(50);
    	messageBubble.setCenterY(50);
    	messageBubble.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	HexMap.getMapPanel().getChildren().remove(messageBubble);
            }
    	}
    	);
    	Platform.runLater(()->HexMap.getMapPanel().getChildren().add(messageBubble));
    	Platform.runLater(()->hexgame.map.updateMapPanel());*/
    }

    public void flushToLog() {
        try {
            hexgame.logFile.write(header);
            hexgame.logFile.newLine();
            for (String row : body) {
                hexgame.logFile.write(row);
                hexgame.logFile.newLine();
            }
        } catch (IOException e) {

        }
    }
}
