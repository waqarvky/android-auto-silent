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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Vector;

public class FilesManager<T> {

    File dir;

    public FilesManager(File directory) {
        dir = directory;
    }

    public Vector<Profile> getAllFiles() {
        File fileName;
        Profile readObject;
        String[] dirList = dir.list();
        Vector<Profile> allFiles = new Vector<Profile>();
        for (int i = 0; i < dirList.length; i++) {
            if (dirList[i].endsWith(".ASDat")) {
                fileName = new File(dir, dirList[i]);
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(new FileInputStream(fileName));
                    readObject = (Profile) ois.readObject();
                    ois.close();
                    allFiles.add(readObject);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                } catch (StreamCorruptedException e) {
                    // TODO Auto-generated catch block
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                }

            }

        }
        return allFiles;
    }

    public Vector<Profile> getOneFile(int fileIdent) {
        File fileName;
        Profile readObject;
        Vector<Profile> oneFile = new Vector<Profile>();
        String fileNameTxt = fileIdent + ".ASDat";
        fileName = new File(dir, fileNameTxt);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(fileName));
            readObject = (Profile) ois.readObject();
            ois.close();
            oneFile.add(readObject);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
        }
        return oneFile;
    }
    
    public Vector<Profile> getActiveFile(String fileIdent) {
        File fileName;
        Profile readObject;
        Vector<Profile> oneFile = new Vector<Profile>();
        //String fileNameTxt = fileIdent + ".ASdat";
        fileName = new File(dir, fileIdent);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(fileName));
            readObject = (Profile) ois.readObject();
            ois.close();
            oneFile.add(readObject);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
        }
        return oneFile;
    }

    public Vector<Prefs> getPrefs() {
        File fileName;
        Prefs readObject;
        Vector<Prefs> oneFile = new Vector<Prefs>();
        String fileNameTxt = "pref.conf";
        fileName = new File(dir, fileNameTxt);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(fileName));
            readObject = (Prefs) ois.readObject();
            ois.close();
            oneFile.add(readObject);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
        }
        return oneFile;
    }
    
    public void writeActiveFile(int id, int dayOfWeek, String day, int sHour, int sMin, int eHour, int eMin) {
    	String fileName = "ActiveProfile.ASdat";
        Profile proObject = new Profile(id, dayOfWeek, day, sHour, sMin, eHour, eMin);
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
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
