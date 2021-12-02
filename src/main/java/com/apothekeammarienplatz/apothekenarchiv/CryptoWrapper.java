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
import java.io.File;
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

    private final String fileNameString;
    private final String targetPathString;

    public static final String FILENAME_EXTENSION_SIGNATURE = ".sig";
    public static final String FILENAME_EXTENSION_ENCRYPTED = ".gpg";

    private HashMap cryptoCommandOutputStringsMap;

    public CryptoWrapper(String fileName, String subDirectoryString, Boolean storeEncryptedCopyBoolean) throws Exception {
        System.out.println("inside CryptoWrapper");
        switch (subDirectoryString) {
            case "Herstellungsanweisungen":
            case "Herstellungsprotokolle":
            case "Plausibilitätsprüfungen":
            case "Prüfprotokolle":
                break;
            default:
                throw new Exception("Kein gültiges Verzeichnis. Es sind nur ausgewählte Verzeichnisse erlaubt.");
        }
        fileNameString = fileName;
        ReadPropertyFile readPropertyFile = new ReadPropertyFile();
        String pathToArchive = readPropertyFile.getPathToArchive();

        targetPathString = pathToArchive + subDirectoryString + "\\" + fileNameString;
        String gpgCommandOnlySign = "gpg --verbose --output \"" + targetPathString + FILENAME_EXTENSION_SIGNATURE + "\" --detach-sig \"" + targetPathString + "\"";
        String gpgCommandSignAndEncrypt = "gpg --verbose --output \"" + targetPathString + FILENAME_EXTENSION_ENCRYPTED + "\" --sign \"" + targetPathString + "\"";

        cryptoCommandOutputStringsMap = runProcess(gpgCommandOnlySign);
        if (storeEncryptedCopyBoolean) {
            cryptoCommandOutputStringsMap.putAll(runProcess(gpgCommandSignAndEncrypt));
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
            //BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getErrorStream()));

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

    public static boolean signatureFileExists(String fileName) {
        ReadPropertyFile readPropertyFile = new ReadPropertyFile();
        String pathToArchive = readPropertyFile.getPathToArchive();
        File file = new File(pathToArchive + fileName + FILENAME_EXTENSION_SIGNATURE);
        return file.exists();
    }

    public static boolean encryptedFileExists(String fileName) {
        ReadPropertyFile readPropertyFile = new ReadPropertyFile();
        String pathToArchive = readPropertyFile.getPathToArchive();
        File file = new File(pathToArchive + fileName + FILENAME_EXTENSION_ENCRYPTED);
        return file.exists();
    }

    public static boolean deleteFile(String fileName) {
        boolean success = true;
        ReadPropertyFile readPropertyFile = new ReadPropertyFile();
        String pathToArchive = readPropertyFile.getPathToArchive();
        File signatureFile = new File(pathToArchive + fileName + FILENAME_EXTENSION_ENCRYPTED);
        File encryptedFile = new File(pathToArchive + fileName + FILENAME_EXTENSION_SIGNATURE);
        if (!signatureFile.delete()) {
            /**
             * Wir geben nur zurück, ob die Signaturdatei erfolgreich gelöscht
             * wurde. Die verschlüsselte Datei ist optional.
             */
            success = false;
        }
        encryptedFile.delete();
        return success;
    }
}
