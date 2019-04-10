package com.company.test;

import org.junit.Test;
import org.junit.Test;
import static org.junit.Assert.assertEquals;



import com.company.HexMap;

public class HexMapTest {
    @Test
    public void constructorTest() {
    	boolean hexmapCreated = false; 
    	try {
	    	HexMap hexmap = new HexMap();
	    	hexmapCreated = true;
    	} catch (Exception e) {
    	}
    		assertEquals(hexmapCreated, true);
    	
    }
    @Test
    public void updatePanelTest() {
    	boolean testSuccessful = false; 
    	try {
	    	HexMap.updateMapPanel();
	    	testSuccessful = true;
    	} catch (Exception e) {
    	}
    		assertEquals(testSuccessful, true);
    	
    }
}
