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

package com.mineshaft.mineshaftapi.util.maths;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class VectorUtil {

    public static double dotProduct(@NotNull Vector vector1, @NotNull Vector vector2) {
        return vector1.getX()*vector2.getX()+vector1.getY()*vector2.getY()+vector1.getZ()*vector2.getZ();
    }

    public static double dotProduct(Vector2D vector1, Vector2D vector2) {
        return vector1.getX()*vector2.getX()+vector1.getY()*vector2.getY();
    }

    public static double getAngle(Vector vector1, Vector vector2) {
        return Math.acos(dotProduct(vector1.normalize(), vector2.normalize()));
    }

    public static double getTopDownAngle(Vector vector1, Vector vector2) {
        return Math.acos(dotProduct(constructTopDownVector(vector1).normalize(), constructTopDownVector(vector2).normalize()));
    }

    public static double getTopDownAngleDegrees(Vector vector1, Vector vector2) {
        return Math.toDegrees(Math.acos(dotProduct(constructTopDownVector(vector1).normalize(), constructTopDownVector(vector2).normalize())));
    }

    public static double getAngle(Vector2D vector1, Vector2D vector2) {
        return Math.acos(dotProduct(vector1.normalize(), vector2.normalize()));
    }

    public static double getAngleDegrees(Vector2D vector1, Vector2D vector2) {
        return Math.toDegrees(Math.acos(dotProduct(vector1.normalize(), vector2.normalize())));
    }

    public static Vector2D constructVector(double x, double y) {
        return new Vector2D(x,y);
    }

    public static Vector2D constructTopDownVector(Location loc1) {
        return new Vector2D(loc1.getX(),loc1.getZ());
    }

    public static Vector2D constructTopDownVector(Vector vector) {
        return new Vector2D(vector.getX(),vector.getZ());
    }

    public static Vector2D getTopdownRelativeVector(Vector2D originVector, Vector2D relativeVector) {
        return new Vector2D(relativeVector.getX()-originVector.getX(),relativeVector.getY()-originVector.getY());
    }
}
