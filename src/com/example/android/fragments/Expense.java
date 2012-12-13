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

	/*
	 * Static
	 */
	private static int expenseCounter;
	private static LinkedList<Expense> expenses = new LinkedList<Expense>();

	public static void addExpense(Expense ex) { getExpenses().add(ex); }
	
	public static String[] getDescriptionStringArray(){
		String[] nomArray = new String[getExpenses().size()];
		
		for(int i=0;i<getExpenses().size();i++){
			nomArray[i]=((Expense)getExpenses().get(i)).description;
		}
		
		return nomArray;
	}
	
	/*
	 * Dynamic
	 */
	Time time;
	CharSequence payer;
	int price;
	String description;
	
	public Expense(){ 
		expenseCounter++; 
		
		time = new Time();
		time.setToNow();
		this.description = "Expense " + expenseCounter;
	}
	
	public Expense(CharSequence payer, int price, String description){
		this();
		this.payer=payer;
		this.price=price;
		this.description=description;
	}
	
	public String getDescription(){
		return description;
	}
	
	public int getPrice(){
		return price;
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

	public static LinkedList<Expense> getExpenses() {
		return expenses;
	}

	public static void setExpenses(LinkedList<Expense> expenses) {
		Expense.expenses = expenses;
	}

	public void setDescription(String string) {
		this.description=string;
	}

	public void setPrice(Integer valueOf) {
		this.price=valueOf;		
	}
}
