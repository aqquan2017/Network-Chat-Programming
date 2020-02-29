package com.chatp.ChatProgramming;

import java.util.ArrayList;
import java.util.Collections;

public class UniqueID {
	private static int RANGE = 10000;
	private static int index = 0;
	
	private static ArrayList<Integer> listID = new ArrayList<Integer>();
	
	static {
		for(int i=0 ; i<RANGE ; i++) {
			listID.add(i);
		}
		Collections.shuffle(listID);
	}
	
	public static int getIdentifier() {
		if(index > RANGE) {
			index = 0;
		}
		return listID.get(index++);
	}
	
	private UniqueID() {
	}

}
