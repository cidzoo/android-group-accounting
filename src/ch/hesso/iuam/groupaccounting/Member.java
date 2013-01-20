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
package ch.hesso.iuam.groupaccounting;

import java.util.LinkedList;

public class Member {

	/*
	 * Static
	 */
	private static int memberCounter;
	private static LinkedList<Member> members = new LinkedList<Member>();

	public static void addMembers(Member ex) { members.add(ex); }
	
	public static String[] getDescriptionStringArray(){
		String[] nomArray = new String[members.size()];
		
		for(int i=0;i<members.size();i++){
			nomArray[i]=((Member)members.get(i)).name;
		}
		
		return nomArray;
	}
	
	public static LinkedList<Member> getMembers() { return members; }
	
	/*
	 * Dynamic
	 */
	private int memberId;
	private String name;
	
	public Member(String name){ 
		memberId = ++memberCounter;
		this.name = name;
	}

	public void setName(String string) { this.name=string; }
	public String getName(){ return name; }
	
	public void setParticipantId(int value) { memberId = value; }
	public int getParticipantId() { return memberId; }
}
