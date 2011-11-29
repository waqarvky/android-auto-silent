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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;

public class EnableService extends Service {

    /**
     * @param intent
     * @return
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
        int Mode = 0;
        FilesManager<Prefs> fm = new FilesManager<Prefs>(getFilesDir());
        for(Prefs prf : fm.getPrefs()){
        Mode = prf.mode;
        }
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(3, 0, 0);
        audioManager.setStreamVolume(5, 0, 0);
        audioManager.setStreamVolume(8, 0, 0);
        audioManager.setRingerMode(Mode);
        stopSelf();
    }
}
