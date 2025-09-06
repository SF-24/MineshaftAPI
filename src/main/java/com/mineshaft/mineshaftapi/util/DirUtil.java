/*
 * Copyright (c) 2025. Sebastian Frynas
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.mineshaft.mineshaftapi.util;

import java.io.File;

public class DirUtil {

    public static String getDirPathFromFilePath(String text) {
        if(text.contains("/")) {
            int index=text.lastIndexOf('/');
            text=text.substring(0,index);
            return text.replace("/", File.separator);
        }
        return "";
    }

    public static String getFileFromFilePath(String text) {
        if(!text.contains("/")) {
            return text;
        }
        int index=text.lastIndexOf('/')+1;
        return text.substring(index).replace("/","");
    }

}
