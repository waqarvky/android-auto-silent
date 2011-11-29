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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddProfile extends Activity {
	private TextView startTime;
	private TextView endTime;
	private TextView dayPickTXT;
	private Button dayPickBTN;
	private Button pickStartTime;
	private Button pickEndTime;
	private Button save;
	
	private int fileId;
	private String dayPicked;
	private int dayOfWeek;
	private int sHour;
	private int sMin;
	private int eHour;
	private int eMin;
	
	static final int START_TIME_DIALOG_ID = 0;
	static final int END_TIME_DIALOG_ID = 1;
	static final int DAY_PICK_DIALOG_ID = 2;
	/**
         * @param savedInstanceState
         * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addprofile);
		Bundle extras = getIntent().getExtras();
		fileId = extras.getInt("fileId");
		startTime = (TextView) findViewById(R.id.StartTimeTXT);
		endTime = (TextView) findViewById(R.id.EndTimeTXT);
		dayPickTXT = (TextView) findViewById(R.id.AddDayTXT);
		dayPickBTN = (Button) findViewById(R.id.AddDayBTN);
		pickStartTime = (Button) findViewById(R.id.StartTimeBTN);
		pickEndTime = (Button) findViewById(R.id.EndTimeBTN);
		save = (Button) findViewById(R.id.SaveBTN);
		
		final CharSequence[] items = {"Monday", "Tuesday", "Wednesday", "Thursday",
				"Friday", "Saturday", "Sunday", "EveryDay", "Week Days"};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.addDay);
		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        dayPickTXT.setText(items[item]);
		        dayPicked = items[item].toString();
		        dayOfWeekSetter();
		    }
		});
		final AlertDialog alert = builder.create();		
		
		dayPickBTN.setOnClickListener(new OnClickListener() {
					
			public void onClick(View v) {
				alert.show();
			}
		});
		
		pickStartTime.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				showDialog(START_TIME_DIALOG_ID);
			}
		});
		
		pickEndTime.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				showDialog(END_TIME_DIALOG_ID);
			}
		});
		
		save.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (dayPicked != null) {
				String fileName = fileId + ".ASDat";
				Profile proObject = new Profile(fileId, dayOfWeek, dayPicked, sHour, sMin, eHour, eMin);
				try {
				FileOutputStream fos = openFileOutput(fileName, MODE_WORLD_READABLE);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(proObject);
					oos.close();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block			
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
				
				String actFileName = "ActiveProfile.ASdat";
		        Profile actProObject = new Profile(0, 0, "EMPTY", 0, 0, 0, 0);
		        try {
		            FileOutputStream fos = openFileOutput(actFileName, MODE_WORLD_READABLE);
		            ObjectOutputStream oos = new ObjectOutputStream(fos);
		            oos.writeObject(actProObject);
		            oos.close();

		        } catch (FileNotFoundException e) {
		            // TODO Auto-generated catch block
		        } catch (IOException e) {
		            // TODO Auto-generated catch block
		        }
				
				Intent itent = new Intent(AddProfile.this, AutoSilentMain.class);
				startActivity(itent);
				finish();
				} else {
					Toast.makeText(getApplicationContext(), R.string.enterDay, Toast.LENGTH_SHORT).show();
				}
			}
						
		});
		
		 //Default Time Displayed
        sHour = 9;
        sMin = 0;
        eHour = 17;
        eMin = 0;
        
        updateStartDisplay();
        updateEndDisplay();
       
		
	}
	
	
	// updates the time we display in the TextView
	private void updateStartDisplay() {
	    startTime.setText(
	        new StringBuilder()
	                .append(pad(sHour)).append(":")
	                .append(pad(sMin)));
	}
	
	private void updateEndDisplay() {
	    endTime.setText(
	        new StringBuilder()
	                .append(pad(eHour)).append(":")
	                .append(pad(eMin)));
	}

	//Format Time display
	private static String pad(int c) {
	    if (c >= 10)
	        return String.valueOf(c);
	    else
	        return "0" + String.valueOf(c);
	}
	
	
	
	
	//Time Picker for Start Time
	 private TimePickerDialog.OnTimeSetListener myStartTimeListener = new TimePickerDialog.OnTimeSetListener() {
			
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
             sHour = hourOfDay;
             sMin = minute;
             updateStartDisplay();	
         }
		};
		
		//Time picker for End Time
		private TimePickerDialog.OnTimeSetListener myEndTimeListener = new TimePickerDialog.OnTimeSetListener() {			
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
             eHour = hourOfDay;
             eMin = minute;
             updateEndDisplay();	
         }
		};
		

		
		//Return user choice
		@Override
		protected Dialog onCreateDialog(int id) {
		    switch (id) {
		    case START_TIME_DIALOG_ID:
		        return new TimePickerDialog(this,
		                myStartTimeListener, sHour, sMin, false);
		    case END_TIME_DIALOG_ID:
		    	return new TimePickerDialog(this, myEndTimeListener, eHour, eMin, false);
		   
			}
		
		    return null;
		}
		//Set day value
		protected void dayOfWeekSetter() {
			if (dayPicked.equals("Sunday")){
				dayOfWeek = 1;
			}
			if (dayPicked.equals("Monday")){
				dayOfWeek = 2;
			}
			if (dayPicked.equals("Tuesday")){
				dayOfWeek = 3;
			}
			if (dayPicked.equals("Wednesday")){
				dayOfWeek = 4;
			}
			if (dayPicked.equals("Thursday")) {
				dayOfWeek = 5;
			}
			if (dayPicked.equals("Friday")) {
				dayOfWeek = 6;
			}
			if (dayPicked.equals("Saturday")) {
				dayOfWeek = 7;
			}
			if (dayPicked.equals("EveryDay")) {
				dayOfWeek = 8;
			}
			if (dayPicked.equals("Week Days")) {
				dayOfWeek = 9;
			}
		}
		
	
}