/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.fragments;

import java.util.LinkedList;

import android.text.format.Time;

public class Participant {

	/*
	 * Static
	 */
	private static int participantCounter;
	private static LinkedList<Participant> participants = new LinkedList<Participant>();

	public static void addParticipants(Participant ex) { participants.add(ex); }
	
	public static String[] getDescriptionStringArray(){
		String[] nomArray = new String[participants.size()];
		
		for(int i=0;i<participants.size();i++){
			nomArray[i]=((Participant)participants.get(i)).name;
		}
		
		return nomArray;
	}
	
	public static LinkedList<Participant> getParticipants() { return participants; }
	
	/*
	 * Dynamic
	 */
	private int participantId;
	private String name;
	
	public Participant(String name){ 
		participantId = ++participantCounter;
		this.name = name;
	}

	//public static LinkedList<Participant> getExpenses() { return participants; }

	public void setName(String string) { this.name=string; }
	public String getName(){ return name; }
	
	public void setParticipantId(int value) { participantId = value; }
	public int getParticipantId() { return participantId; }
}
