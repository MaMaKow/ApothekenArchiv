/*
 * Copyright (C) 2021 Martin Mandelkow <mandelkow@apotheke-schwerin.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.apothekeammarienplatz.apothekenarchiv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin Mandelkow <mandelkow@apotheke-schwerin.de>
 */
public class CryptoWrapper {

    private String fileNameString;
    private String pathToArchive;
    private String targetPathString;

    private HashMap cryptoCommandOutputStringsMap;

    public CryptoWrapper(String fileName) {
        this(fileName, false);
    }

    public CryptoWrapper(String fileName, Boolean storeEncryptedCopyBoolean) {
        fileNameString = fileName;
        //pathToArchive = "C:\\Users\\Apothekenadmin\\Documents\\Qsync\\Rezeptur\\Dokumentation\\";
        pathToArchive = "";

        targetPathString = pathToArchive + fileNameString;
        String gpgCommandOnlySign = "gpg --output " + targetPathString + ".sig --detach-sig " + targetPathString;
        String gpgCommandSignAndEncrypt = "gpg --output " + targetPathString + ".gpg --sign " + targetPathString;

        runProcess(gpgCommandOnlySign);
        if (storeEncryptedCopyBoolean) {
            runProcess(gpgCommandSignAndEncrypt);
        }
    }

    private HashMap runProcess(String command) {
        System.out.println("Trying to run the following command:");
        System.out.println(command);
        try {
            Runtime run = Runtime.getRuntime();
            Process process;
            process = run.exec(command);
            process.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));

            cryptoCommandOutputStringsMap = new HashMap<Integer, String>();
            String line;
            while ((line = buf.readLine()) != null) {
                cryptoCommandOutputStringsMap.put(buf.hashCode(), line);
            }
            return cryptoCommandOutputStringsMap;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CryptoWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getCommandOutput() {
        if (null == cryptoCommandOutputStringsMap) {
            return null;
        }
        String outputString = convertWithIteration(cryptoCommandOutputStringsMap);
        return outputString;
    }

    private String convertWithIteration(HashMap<Integer, ?> map) {
        StringBuilder mapAsString = new StringBuilder("{");
        for (Integer key : map.keySet()) {
            mapAsString.append(key + "=" + map.get(key) + ", ");
        }
        mapAsString.append("}");
        return mapAsString.toString();
    }
}