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

/*
 * This activity displays information about the app and copyright information to the user.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class About extends Activity {

    /**
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final LinearLayout lv = (LinearLayout) findViewById(R.id.LayoutProfiles);
        ImageView ob = new ImageView(this);
        ob.setImageResource(R.drawable.obar);
        lv.addView(ob);

        TextView tv = new TextView(this);
        tv.setTextSize(28);
        tv.setText(R.string.version);
        TextView tv2 = new TextView(this);
        tv2.setTextSize(16);
        tv2.setText(R.string.creator);
        TextView tv3 = new TextView(this);
        tv3.setTextSize(16);
        tv3.setText(R.string.bugsto);
        TextView tv4 = new TextView(this);
        tv4.setTextSize(16);
        tv4.setText(R.string.email);
        TextView tv5 = new TextView(this);
        tv5.setTextSize(16);
        tv5.setText(R.string.abouttxt);
        ImageView ol = new ImageView(this);
        ol.setImageResource(R.drawable.oline);
        ImageView icn = new ImageView(this);
        icn.setImageResource(R.drawable.icon);
        TextView tv6 = new TextView(this);
        tv6.setTextSize(12);
        tv6.setText(R.string.copyright);
        TextView tv7 = new TextView(this);
        tv7.setTextSize(16);
        tv7.setTextColor(Color.argb(255, 50, 205, 50));
        tv7.setText(R.string.Donate);
        lv.addView(tv);
        lv.addView(tv2);
        lv.addView(tv3);
        lv.addView(tv4);
        lv.addView(ol);
        lv.addView(tv5);
        lv.addView(icn);
        lv.addView(tv6);
        lv.addView(tv7);
        tv7.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
            	Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=NBXLSKRNJ8T2S"));
            	startActivity(myIntent);    
            }
        });
        
    }
}
