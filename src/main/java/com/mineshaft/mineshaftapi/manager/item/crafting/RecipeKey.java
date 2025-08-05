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

package com.mineshaft.mineshaftapi.manager.item.crafting;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.NamespacedKey;

@Getter @Setter
public class RecipeKey {

    String namespace;
    String id;

    public RecipeKey(NamespacedKey key) {
        this.namespace = key.getNamespace();
        this.id=key.getKey();
    }

    public RecipeKey(String namespace, String id) {
        this.namespace = namespace;
        this.id = id;
    }

    public NamespacedKey getNamespacedKey() {
        return new NamespacedKey(namespace, id);
    }

}
