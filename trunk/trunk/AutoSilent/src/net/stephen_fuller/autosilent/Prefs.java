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


public class Prefs implements Serializable{

    
	private static final long serialVersionUID = 1049088579830108295L;
	boolean disabled;
    int mode;
    int vol3;
    int vol5;
    int vol8;
    String colour;

    public Prefs(boolean disabled, int mode, int vol3, int vol5, int vol8, String colour) {
        this.disabled = disabled;
        this.mode = mode;
        this.vol3 = vol3;
        this.vol5 = vol5;
        this.vol8 = vol8;
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }

    public int getMode() {
        return mode;
    }

    public int getVol3() {
        return vol3;
    }

    public int getVol5() {
        return vol5;
    }

    public int getVol8() {
        return vol8;
    }

}
