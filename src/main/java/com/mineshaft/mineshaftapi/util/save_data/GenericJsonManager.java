/*
 * Copyright (c) 2026. Sebastian Frynas
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

package com.mineshaft.mineshaftapi.util.save_data;

import com.mineshaft.mineshaftapi.manager.player.json.JsonSaveObject;

public abstract class GenericJsonManager {

    public abstract Class<?> getSaveClass();
    public abstract String getFileName();
    public abstract String getPath();

    public void saveFile(JsonSaveObject data) {
        new GenericJsonSaveLoader(getPath(),getFileName(), getSaveClass()).saveFile(data);
    }

    public JsonSaveObject getData() {
        if(new GenericJsonSaveLoader(getPath(), getFileName(), getSaveClass()).loadData() != null) {
            return new GenericJsonSaveLoader(getPath(), getFileName(), getSaveClass()).loadData();
        }
        return null;
    }
}
