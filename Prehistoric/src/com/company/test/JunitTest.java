package com.company.test;
import org.junit.Test;

import com.company.HexMap;

import static org.junit.Assert.assertEquals;

public class JunitTest {
    @Test
    public void testAdd() {
        String str = "Junit is working fine";
        assertEquals("Junit is working fine",str);
    }
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



