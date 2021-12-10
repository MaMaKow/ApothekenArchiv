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
import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin Mandelkow <mandelkow@apotheke-schwerin.de>
 */
public class ScannerWrapper {

    String targetPathString;
    String napsScanProfile;
    String pathToArchive;
    String naps_scan_bin;
    /**
     * <p lang=de>Dateiname</p>
     */
    String fileNameString;
    /**
     *
     * The output of the NAPS command:
     */
    HashMap scanCommandOutputStringsMap;

    public ScannerWrapper(String fileName, String subDirectoryString) throws Exception {
        switch (subDirectoryString) {
            case "Herstellungsanweisungen":
            case "Herstellungsprotokolle":
            case "Plausibilitätsprüfungen":
            case "Prüfprotokolle":
                break;
            default:
                throw new Exception("Kein gültiges Verzeichnis. Es sind nur ausgewählte Verzeichnisse erlaubt.");
        }
        ReadPropertyFile readPropertyFile = new ReadPropertyFile();
        pathToArchive = readPropertyFile.getPathToArchive();
        fileNameString = fileName;
        napsScanProfile = "fi-7160_grau";
        naps_scan_bin = "C:\\Program Files (x86)\\NAPS2\\NAPS2.Console.exe";
        targetPathString = pathToArchive + subDirectoryString + "\\" + fileNameString;
        File file = new File(targetPathString);
        if (file.exists()) {
            throw new FileAlreadyExistsException(targetPathString);
        }
        String scanCommandString = naps_scan_bin + " -o \"" + targetPathString + "\" --enableocr --profile " + napsScanProfile + " --ocrlang deu --verbose";
        scanCommandOutputStringsMap = runProcess(scanCommandString);
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
            HashMap<Integer, String> scanCommandOutputStrings;
            scanCommandOutputStrings = new HashMap<>();
            String line;
            while ((line = buf.readLine()) != null) {
                scanCommandOutputStrings.put(buf.hashCode(), line);
            }
            return scanCommandOutputStrings;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CryptoWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getCommandOutput() {
        if (null == scanCommandOutputStringsMap) {
            return null;
        }
        String outputString = convertWithIteration(scanCommandOutputStringsMap);
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

    public File getFileWithPath() {
        File file = new File(targetPathString);
        return file;
    }

    public static boolean fileExists(String fileName) {
        ReadPropertyFile readPropertyFile = new ReadPropertyFile();
        String pathToArchive = readPropertyFile.getPathToArchive();
        File file = new File(pathToArchive + fileName);
        return file.exists();
    }

    public static boolean deleteFile(String fileName) {
        ReadPropertyFile readPropertyFile = new ReadPropertyFile();
        String pathToArchive = readPropertyFile.getPathToArchive();
        File file = new File(pathToArchive + fileName);
        CryptoWrapper.deleteFile(fileName);
        return file.delete();
    }

}
