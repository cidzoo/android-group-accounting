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

public class Expense {

	public static LinkedList<Expense> expenses = new LinkedList<Expense>();
	
	Time time;
	CharSequence payer;
	int price;
	String description;
	
	public static void addExpense(Time time, CharSequence payer, int price){
		addExpense(time, payer, price, "Expense " + (expenses.size()+1));
	}
	
	public static void addExpense(Time time, CharSequence payer, int price, String description){
		Expense d = new Expense();
		
		d.time = time;
		d.payer = payer;
		d.price = price;
		d.description = description;
			
		expenses.add(d);
	}
	
	public String getDescription(){
		return description;
	}
	
	public int getPrice(){
		return price;
	}
	
	public static String[] getDescriptionStringArray(){
		String[] nomArray = new String[expenses.size()];
		
		for(int i=0;i<expenses.size();i++){
			nomArray[i]=((Expense)expenses.get(i)).description;
		}
		
		return nomArray;
	}

	public Time getTime() {
		return time;
	}
	
	public String getTimeToString(){
		return time.month + "/" + time.monthDay + "/" + time.year + " - " + time.hour + ":" + time.minute;
	}

	public String getPayer() {
		return payer.toString();
	}
	
	public String toString(){
		return description + "\n"
        		+ "Price \t: " + price + ".- CHF\n"
        		+ "Payer \t:" + payer + "\n"
        		+ "Time \t: " + time.toString();
	}
}
