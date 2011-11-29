/* Copyright (c) 2011, Stephen Kenneth Fuller <skflinux@gmail.com>  
** ...  
**  
** Permission to use, copy, modify, and/or distribute this software for  
** any purpose with or without fee is hereby granted, provided that the  
** above copyright notice and this permission notice appear in all copies.  
**  
** THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL  
** WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED  
** WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR  
** BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES  
** OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,  
** WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION,  
** ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS  
** SOFTWARE.  
*/


package net.stephen_fuller.autosilent;

import java.io.Serializable;

public class Profile implements Serializable{

	private static final long serialVersionUID = -9027902849325513093L;
	private int id;
	private int dayOfWeek;
	private String day;
	private int sHour;
	private int sMin;
	private int eHour;
	private int eMin;
	
	public Profile(int id, int dayOfWeek, String day, int sHour, int sMin, int eHour, int eMin) {
		this.id = id;
		this.dayOfWeek = dayOfWeek;
		this.day = day;
		this.sHour = sHour;
		this.sMin = sMin;
		this.eHour = eHour;
		this.eMin = eMin;
	}
	
	public int getId() {
		return id;
	}
	
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	
	public String getDay() {
		return day;
	}
	
	public int getsHour() {
		return sHour;
	}
	
	public int getsMin() {
		return sMin;
	}
	
	public int geteHour() {
		return eHour;
	}
	
	public int geteMin() {
		return eMin;
	}

}