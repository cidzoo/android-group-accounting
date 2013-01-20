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

import android.text.format.Time;

public class Expense {

	/*
	 * Static
	 */
	private static int unamedExpenseCounter;
	private static LinkedList<Expense> expenses = new LinkedList<Expense>();

	public static void addExpense(Expense ex) { expenses.add(ex); }
	
	public static String[] getDescriptionStringArray(){
		String[] nomArray = new String[expenses.size()];
		
		for(int i=0;i<expenses.size();i++){
			nomArray[i]=((Expense)expenses.get(i)).description;
		}
		
		return nomArray;
	}
	
	public static LinkedList<Expense> getExpenses() { return expenses; }
	
	/*
	 * Dynamic
	 */
	Time time;
	int payerId;
	int price;
	String description;
	String payerName;

	public Expense(){ 
		unamedExpenseCounter++; 
		
		time = new Time();
		time.setToNow();
		this.description = "Expense " + unamedExpenseCounter;
	}
	
	public Expense(int payerId, String payerName, int price, String description){
		this();
		this.payerId=payerId;
		this.payerName=payerName;
		this.price=price;
		this.description=description;
	}
	
	public String getDateToString(){
		return time.month + "/" + time.monthDay + "/" + time.year;
	}
	
	public String getTimeToString(){
		String strHours = String.valueOf(time.hour);
		strHours = strHours.length()==1 ? "0" + strHours : strHours;
		String strMinutes = String.valueOf(time.minute);
		strMinutes = strMinutes.length()==1 ? "0" + strMinutes : strMinutes;
		return strHours + ":" + strMinutes;
	}
	
	public String toString(){
		return description + "\n"
        		+ "Price \t: " + price + ".- CHF\n"
        		+ "Payer \t:" + payerId + "\n"
        		+ "Time \t: " + time.toString();
	}

	public Time getTime() { return time; }
	
	public void setDescription(String string) { this.description=string; }
	public String getDescription(){ return description; }

	public void setPrice(Integer valueOf) { this.price=valueOf; }
	public int getPrice(){ return price; }
	
	public void setPayerId(int value) { payerId = value; }
	public int getPayerId() { return payerId; }
	public String getPayerName() { return payerName; }
	public void setPayerName(String string) { payerName = string; }

}
