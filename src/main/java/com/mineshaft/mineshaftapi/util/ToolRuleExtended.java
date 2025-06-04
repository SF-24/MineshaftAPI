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

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ToolRuleExtended implements ToolComponent.ToolRule {
    @Override
    public @NotNull Collection<Material> getBlocks() {
        return List.of();
    }

    @Override
    public void setBlocks(@NotNull Material material) {

    }

    @Override
    public void setBlocks(@NotNull Collection<Material> collection) {

    }

    @Override
    public void setBlocks(@NotNull Tag<Material> tag) {

    }

    @Override
    public @Nullable Float getSpeed() {
        return 0f;
    }

    @Override
    public void setSpeed(@Nullable Float aFloat) {

    }

    @Override
    public @Nullable Boolean isCorrectForDrops() {
        return null;
    }

    @Override
    public void setCorrectForDrops(@Nullable Boolean aBoolean) {

    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of();
    }
}
