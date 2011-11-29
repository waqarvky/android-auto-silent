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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class AutoSilentMain extends Activity {

    public String fileName;
    FilesManager<Profile> fm;

    /** Called when the activity is first created.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        fm = new FilesManager<Profile>(getFilesDir());

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        Intent is = new Intent(AutoSilentMain.this, ProfileCheckService.class);
        startService(is);
        //Set layout
        final LinearLayout lv = (LinearLayout) findViewById(R.id.LayoutProfiles);
        TextView thelp = new TextView(this);
        TextView info = new TextView(this);
        for (Prefs prf : fm.getPrefs()) {
            if (prf.disabled == true) {
                info.setTextSize(16);
                info.setTextColor(Color.argb(255, 255, 0, 0));
                info.setText("Auto Silent is Disabled");
            } else {
                if (prf.mode == 0) {
                    info.setTextSize(16);
                    info.setTextColor(Color.argb(255, 50, 205, 50));
                    info.setText("Silent Mode Enabled");
                }
                if (prf.mode == 1) {
                    info.setTextSize(16);
                    info.setTextColor(Color.argb(255, 50, 205, 50));
                    info.setText("Vibrate Mode Enabled");
                }
            }
        }
        thelp.setTextSize(16);
        thelp.setText("Press Menu to Add a new Profile");
        ImageView ob = new ImageView(this);
        ob.setImageResource(R.drawable.obar);
        lv.addView(info);
        lv.addView(thelp);
        lv.addView(ob);

        String nme = "pref.conf";
        if (!getFileStreamPath(nme).exists()) {
            addToPrefs("conf");
        }

        //Dialog for when displayed profile is selected
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete Profile?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
            	for (Profile dp : fm.getActiveFile(fileName)){
            		for (Profile ac : fm.getActiveFile("ActiveProfile.ASdat")){
            			if (ac.getId() == dp.getId()){
            			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            	        audioManager.setRingerMode(2);
            	        int volMax3 = audioManager.getStreamMaxVolume(3);
            	        int volMax5 = audioManager.getStreamMaxVolume(5);
            	        int volMax8 = audioManager.getStreamMaxVolume(8);
            	        audioManager.setStreamVolume(3, volMax3, 0);
            	        audioManager.setStreamVolume(5, volMax5, 0);
            	        audioManager.setStreamVolume(8, volMax8, 0);
            	        String fileName = "ActiveProfile.ASdat";
            	        Profile proObject = new Profile(0, 0, "EMPTY", 0, 0, 0, 0);
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
            	}
                deleteFile(fileName);
                Intent i = new Intent(AutoSilentMain.this, AutoSilentMain.class);
                startActivity(i);
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        final AlertDialog alert = builder.create();

        //New vector to store sorted by day profiles
        Vector<Profile> display = new Vector<Profile>();

        //Layout the profiles to be displayed in day order
        for (int i = 0; i < 10; i++) {
            for (Profile sp : fm.getAllFiles()) {
                if (sp.getDayOfWeek() == i) {
                    display.add(sp);
                }
            }
        }
        //Create view
        for (Profile p : display) {
            final TextView tv = new TextView(this);
            tv.setId(p.getId());
            tv.setTextSize(28);
            tv.setText(p.getDay() + " " + (new StringBuilder().append(pad(p.getsHour())).append(":").append(pad(p.getsMin()))) + " - "
                    + (new StringBuilder().append(pad(p.geteHour())).append(":").append(pad(p.geteMin()))));
            ImageView ol = new ImageView(this);
            ol.setImageResource(R.drawable.oline);
            lv.addView(tv);
            lv.addView(ol);
            tv.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    fileName = tv.getId() + ".ASDat";
                    alert.show();
                }
            });
        }
    }

    //Format Time display
    private static String pad(int c) {
        if (c >= 10) {
            return String.valueOf(c);
        } else {
            return "0" + String.valueOf(c);
        }
    }

    //Create a unique ID
    private int createID() {
        int fileId = 0;
        int id = 1;
        int count = 0;
        int maxCount = fm.getAllFiles().size();
        if (fm.getAllFiles().isEmpty()) {
            fileId = 1;
        } else {
            while (fileId == 0) {
                for (Profile p : fm.getAllFiles()) {
                    if (id != p.getId()) {
                        count++;
                    }
                }
                if (count == maxCount) {
                    fileId = id;
                } else {
                    count = 0;
                }
                id++;
            }
        }
        return fileId;
    }

    private void addToPrefs(String pref) {
        boolean disable = false;
        int mode = 0;
        int vol3 = 0;
        int vol5 = 0;
        int vol8 = 0;
        String Colour = "Orange";

        for (Prefs tr : fm.getPrefs()) {
            disable = tr.disabled;
            mode = tr.mode;
            vol3 = tr.vol3;
            vol5 = tr.vol5;
            vol8 = tr.vol8;
            Colour = tr.colour;
        }

        if (pref.equals("Enable / Disable")) {
            for (Prefs prf : fm.getPrefs()) {
                if (prf.disabled == false) {
                    disable = true;
                } else {
                    disable = false;
                }
                for (Prefs pr : fm.getPrefs()) {
                    mode = pr.mode;
                }
            }
        }
        if (pref.equals("Silent / Vibrate")) {
            if (mode == 0) {
                mode = 1;
            } else {
                mode = 0;
            }
            for (Prefs pr : fm.getPrefs()) {
                disable = pr.disabled;
            }
        }
        String fileNameprf = "pref.conf";
        Prefs proObject = new Prefs(disable, mode, vol3, vol5, vol8, Colour);
        try {
            FileOutputStream fos = openFileOutput(fileNameprf, MODE_WORLD_READABLE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(proObject);
            oos.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        Intent i = new Intent(AutoSilentMain.this, AutoSilentMain.class);
        startActivity(i);
        finish();
    }

    //Menu Button Options create
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    //Menu Button Options Actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final CharSequence[] items = {"Enable / Disable", "Silent / Vibrate"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                String pref = items[item].toString();
                addToPrefs(pref);
            }
        });
        final AlertDialog alert = builder.create();
        switch (item.getItemId()) {
            case R.id.addProfileMnu:
                Toast.makeText(this, R.string.add_profile, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AutoSilentMain.this, AddProfile.class);
                Bundle bundle = new Bundle();
                createID();
                bundle.putInt("fileId", createID());
                i.putExtras(bundle);
                startActivityForResult(i, 0);
                finish();
                break;
            case R.id.optionsMnu:
                alert.show();
                break;
            case R.id.aboutMnu:
                Toast.makeText(this, R.string.about, Toast.LENGTH_SHORT).show();
                Intent iA = new Intent(AutoSilentMain.this, About.class);
                startActivity(iA);
                break;
        }
        return true;
    }
    
}
