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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin Mandelkow <mandelkow@apotheke-schwerin.de>
 */
public class StoffDatabaseReader {

    DatabaseWrapper databaseWrapper;

    public StoffDatabaseReader() {
        databaseWrapper = new DatabaseWrapper();
    }

    public Map<Integer, String> getStoffListe(String stoffNameTeil) {
        Map<Integer, String> stoffListe = new HashMap<>();
        try {
            //System.out.println("Stoffsuche mit Bestandteil:");
            //System.out.println(stoffNameTeil);
            PreparedStatement preparedStatement = databaseWrapper.prepareStatement("SELECT * FROM stoffe WHERE `stoffName` like ? LIMIT 50");
            preparedStatement.setString(1, '%' + stoffNameTeil + '%');
            ResultSet stoffeResultSet = preparedStatement.executeQuery();

            while (stoffeResultSet.next()) {
                Integer stoffId = Integer.parseInt(stoffeResultSet.getString("stoffId"));
                String stoffName = stoffeResultSet.getString("stoffName");
                stoffListe.put((stoffId), stoffName);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoffDatabaseReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stoffListe;
    }

    public void close() {
        databaseWrapper.close();
    }
}
