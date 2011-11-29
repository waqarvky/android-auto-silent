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
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import java.util.Vector;

public class ProfileCheckService extends Service {

    FilesManager<Profile> fm;
   
    /**
     * @param intent
     * @return
     * @see android.app.Service#onBind(Intent)
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * @see android.app.Service#onCreate()
     */
    @Override
    public void onCreate() {

        fm = new FilesManager<Profile>(getFilesDir());
        /*Code for a later version
         * AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volMax3 = audioManager.getStreamMaxVolume(3);
        volMax5 = audioManager.getStreamMaxVolume(5);
        volMax8 = audioManager.getStreamMaxVolume(8);
         */

        for (Prefs prf : fm.getPrefs()) { // get details from the preferences file

            if (prf.disabled == true || fm.getAllFiles().isEmpty()) {
            	writeActiveFile(0, 0, "EMPTY", 0, 0, 0, 0);
                stopSelf(); // if preferences say disabled or there are no profiles to check stop here
            }
            
            if (prf.disabled == false) {
            	
            	
            Calendar cal = Calendar.getInstance();
           
                Vector<Profile> profEnable = new Vector<Profile>();
                //get data from profile files and check against this information
                for (Profile p : fm.getAllFiles()) {
                    	
                    	 //If day saved in file equals today add details to vector
                        if (p.getDayOfWeek() == cal.get(Calendar.DAY_OF_WEEK)) {
                            profEnable.add(p);
                        }
                        //If day saved in file equals everyday add details to vector
                        if (p.getDayOfWeek() == 8) {
                            profEnable.add(p);
                        }
                        //If day saved in file equals weekdays and today is a weekday add details to vector
                        if (p.getDayOfWeek() == 9 && (cal.get(Calendar.DAY_OF_WEEK) > 1
                                && cal.get(Calendar.DAY_OF_WEEK) < 7)) {
                            profEnable.add(p);
                        }
                    	
                }
                //if vector has something in it call do check sending
                //current day and vector contents
                if (profEnable.size() > 0) {
                   int day = cal.get(Calendar.DAY_OF_WEEK);
                   if (doActiveFileCheck().equals(false)){
               		for (Profile pd : fm.getOneFile(doCheck(day, profEnable))){
               			writeActiveFile(pd.getId(), pd.getDayOfWeek(), pd.getDay(), 
               					pd.getsHour(), pd.getsMin(), pd.geteHour(), pd.geteMin());
               			doActiveFileCheck();
               		}
               	} else {
               		doActiveFileCheck();
               	}
                
                   }
                

                int setNextCheck = 0;
                int setDay;
                //if current hour is less than 9pm set the next profile check for 3hrs from now
                if (cal.get(Calendar.HOUR_OF_DAY) < 21) {
                    setNextCheck = cal.get(Calendar.HOUR_OF_DAY) + 3;
                    setServiceTime(setNextCheck, cal.get(Calendar.MINUTE), cal.get(Calendar.DAY_OF_WEEK), "check");
                    stopSelf();
                } else {

                        //else set setday to equal the next day
                        setDay = cal.get(Calendar.DAY_OF_WEEK) + 1;
                    
                    setServiceTime(1, cal.get(Calendar.MINUTE), setDay, "check");
                    stopSelf();
                }
            }
        }
        stopSelf();
    }

    private long getDiffInTime(int hour, int min, int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();

        Calendar calendarTimetoStart = (Calendar) calendar.clone();

        
            calendarTimetoStart.set(Calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK));
            if(dayOfWeek != calendar.get(Calendar.DAY_OF_WEEK)){
            	calendarTimetoStart.add(Calendar.DAY_OF_WEEK , 1);
            }
        calendarTimetoStart.set(Calendar.HOUR_OF_DAY, hour);
        calendarTimetoStart.set(Calendar.MINUTE, min);
        calendarTimetoStart.set(Calendar.SECOND, 0);

        long diffInTime = calendarTimetoStart.getTimeInMillis() - calendar.getTimeInMillis();
        return diffInTime;
    }

    private Boolean setServiceTime(int hour, int min, int dayOfWeek, String service) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent();

        if (service.equals("enable")) {
            i.setClass(getApplicationContext(), EnableService.class);
        }
        if (service.equals("disable")) {
            i.setClass(getApplicationContext(), DisableService.class);
        }
        if (service.equals("check")) {
            i.setClass(getApplicationContext(), ProfileCheckService.class);
        }

        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, i, 0);

        Calendar calendar = Calendar.getInstance();

        long diffInTime = getDiffInTime(hour, min, dayOfWeek);

        long firstWake = calendar.getTimeInMillis() + diffInTime;

        am.set(AlarmManager.RTC_WAKEUP, firstWake, pendingIntent);
        return true;
    }

    private int doCheck(int day, Vector<Profile> prof) {
    	
    	int pkdHourId = 0;
    	int pkdHour = 0;
        for (Profile p : prof) {
            long startDiffTime = getDiffInTime(p.getsHour(), p.getsMin(), day);
            long endDiffTime = getDiffInTime(p.geteHour(), p.geteMin(), day);

            if (startDiffTime < 0 && endDiffTime > 0) {
                pkdHourId = p.getId();
            }
            if (startDiffTime < 0 && (p.geteHour() < p.getsHour())) {
                pkdHourId = p.getId();
            }
            if (startDiffTime > 0) {
                if (pkdHourId == 0) {
                    pkdHourId = p.getId();
                    pkdHour = p.getsHour();
                }
                for (Profile sp : fm.getOneFile(pkdHourId)) {
                    if (sp.getsHour() < pkdHour) {
                        pkdHourId = sp.getId();
                    }
                }

            }
        }
        return pkdHourId;
    }
    
    public Boolean doActiveFileCheck(){
    	for (Profile activeProf : fm.getActiveFile("ActiveProfile.ASdat")) {
    		if (activeProf.getDay().equalsIgnoreCase("EMPTY")){
    			return(false);
    		} else {
        		Calendar cal = Calendar.getInstance();
        		int day = cal.get(Calendar.DAY_OF_WEEK);
        		// if start time is in the future set enable alarm
        		if (getDiffInTime(activeProf.getsHour(), activeProf.getsMin(), day) > 0){
        		setServiceTime(activeProf.getsHour(), activeProf.getsMin(), day, "enable");
        			// if the end time is in the future today empty active file and set disable alarm
        			if (getDiffInTime(activeProf.geteHour(), activeProf.geteMin(), day) > 0){
        				writeActiveFile(activeProf.getId(), 0, "EMPTY", 0, 0, 0, 0);
        				setServiceTime(activeProf.geteHour(), activeProf.geteMin(), day, "disable");
        				return(false);
        			} else {
        				// else the end time must be tommorrow so check then
        				setServiceTime(activeProf.geteHour(), activeProf.geteMin(), activeProf.getDayOfWeek(), "disable");
        			}
        		return(true);
        		}
        		// in the event that the alarms set have been lost and the active file contains a profile set disable alarm here
        		if (getDiffInTime(activeProf.geteHour(), activeProf.geteMin(), day) > 0){
        			if (getDiffInTime(activeProf.getsHour(), activeProf.getsMin(), day) < 0){
        				setServiceTime(activeProf.getsHour(), activeProf.getsMin(), day, "enable");
        			}
        			writeActiveFile(activeProf.getId(), 0, "EMPTY", 0, 0, 0, 0);
        			setServiceTime(activeProf.geteHour(), activeProf.geteMin(), day, "disable");
        			return(false);
        		}
        		if (getDiffInTime(activeProf.geteHour(), activeProf.geteMin(), day) < 0){
        			writeActiveFile(0, 0, "EMPTY", 0, 0, 0, 0);
        			setServiceTime(activeProf.geteHour(), activeProf.geteMin(), day, "disable");
        			return(false);
        		}
    		}
    	}
    	return(false);
    }
    
    public void writeActiveFile(int id, int dayOfWeek, String day, int sHour, int sMin, int eHour, int eMin) {
    	String fileName = "ActiveProfile.ASdat";
        Profile proObject = new Profile(id, dayOfWeek, day, sHour, sMin, eHour, eMin);
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
    }
}
