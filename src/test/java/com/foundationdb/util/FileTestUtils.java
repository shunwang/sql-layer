/**
 * Copyright (C) 2009-2013 FoundationDB, LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.foundationdb.util;

public class FileTestUtils {

    public static void printClickableFile(String filename, String suffix, int lineNumber) {
        if (filename != null) {
            System.err.println("  at " + filename.replace("src/test/resources/", "").
                    replaceFirst("/([^/]+.)$", "($1." + suffix + ":" + lineNumber + ")").replaceAll("/", "."));
            // for those running from maven or elsewhere
            System.err.println("  aka: " + filename + "." + suffix + ":" + lineNumber);
        } else {
            System.err.println("NULL filename");
        }
    }
}
