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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;

public class DisableService extends Service {

    /**
     * @see android.app.Service#onBind(Intent)
     */
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Put your code here
        return null;
    }

    /**
     * @see android.app.Service#onCreate()
     */
    @Override
    public void onCreate() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(2);
        int volMax3 = audioManager.getStreamMaxVolume(3);
        int volMax5 = audioManager.getStreamMaxVolume(5);
        int volMax8 = audioManager.getStreamMaxVolume(8);
        audioManager.setStreamVolume(3, volMax3, 0);
        audioManager.setStreamVolume(5, volMax5, 0);
        audioManager.setStreamVolume(8, volMax8, 0);
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
        Intent i = new Intent();
        i.setClass(getApplicationContext(), ProfileCheckService.class);
        startService(i);
        stopSelf();
    }
}
