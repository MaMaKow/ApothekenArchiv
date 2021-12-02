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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin Mandelkow <mandelkow@apotheke-schwerin.de>
 */
public class StoffDatabaseWriter {

    public static void main(String[] args) {
        StoffDatabaseWriter stoffDatabaseWriter = new StoffDatabaseWriter();

    }

    public StoffDatabaseWriter() {

        DatabaseWrapper databaseWrapper = new DatabaseWrapper();
        PreparedStatement preparedStatement = databaseWrapper.prepareStatement("REPLACE INTO stoffe VALUES (?, ?)");

        StoffJsonParser stoffJsonParser = new StoffJsonParser();
        Map<Integer, String> stoffListe = stoffJsonParser.getStoffListe();
        for (var entry : stoffListe.entrySet()) {
            try {
                int stoffId = entry.getKey();
                String stoffName = entry.getValue();
                preparedStatement.setInt(1, stoffId);
                preparedStatement.setString(2, stoffName);
                preparedStatement.execute();
                System.out.println(stoffName);
            } catch (SQLException ex) {
                Logger.getLogger(StoffDatabaseWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        databaseWrapper.close();

    }
}
