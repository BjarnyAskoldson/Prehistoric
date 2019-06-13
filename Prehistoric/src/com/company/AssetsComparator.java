package com.company;

import java.util.Comparator;

import com.company.Enumerations.Asset;

/**
 * Returns order of assets by effectiveness descendant
 * @author Alex Miliukov
 *
 */
public class AssetsComparator implements Comparator<Asset>
{
    public int compare(Asset o1, Asset o2)
    {
        //return -o1.compareTo(o2); // this flips the order
        //return o1.sound.length() - o2.sound.length(); // this compares length
    	//
    	return (int) -(o1.getEffectiveness()*1000 - o2.getEffectiveness()*1000);
    }
}

