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

import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.Vector;

@Setter
@Getter
public class PlanarVectorBounds {

    double planeMin;
    double planeMax;
    double yMin;
    double yMax;

    public PlanarVectorBounds() {
        // Create empty vector bounds
        setAllValues(-1);
    }

    public PlanarVectorBounds(double yMin, double yMax) {
        // inversion checks
        if(yMin > yMax) {
            this.yMin=yMax;
            this.yMax=yMin;
        } else {
            this.yMin = yMin;
            this.yMax = yMax;
        }
        this.planeMin=-1;
        this.planeMax=-1;
    }

    // Plane refers to in the plane of the player
    public PlanarVectorBounds(double planeMin, double yMin, double planeMax, double yMax) {
        // inversion checks
        if(yMin > yMax) {
            this.yMin=yMax;
            this.yMax=yMin;
        } else {
            this.yMin = yMin;
            this.yMax = yMax;
        }
        if(planeMin>planeMax) {
            this.planeMin=planeMax;
            this.planeMax=planeMin;
        } else {
            this.planeMin = planeMin;
            this.planeMax = planeMax;
        }
    }

    public void setAllValues(int value) {
        this.yMin=value;
        this.yMax=value;
        this.planeMin=value;
        this.planeMax=value;
    }

    public void trimVector(Vector vector) {
        trimVectorPlanar(vector);
        trimVectorY(vector);
    }

    public void trimVectorY(Vector vector) {
        if(yMin>=0&&yMax>0&&yMax>yMin) {
            if (vector.getY() < yMin) {vector.setY(yMax);}
            if(vector.getY()>yMax) {vector.setY(yMax);}
        } else if(yMin==yMax) {
            vector.setY(yMax);
        }
    }

    public void trimVectorPlanar(Vector vector) {
        if(planeMax>=0 && planeMin>=0 && planeMax>=planeMin) {
            double lengthSquared = vector.getX() * vector.getX() + vector.getY() * vector.getY();
            double ratio = 0;
            if (lengthSquared > planeMax * planeMax) {
                ratio = Math.sqrt(lengthSquared / planeMax);
            } else if (lengthSquared < planeMin * planeMin) {
                ratio = Math.sqrt(lengthSquared / planeMin);
            }
            if (ratio != 0) {
                vector.setX(vector.getX() / ratio);
                vector.setZ(vector.getZ() / ratio);
            }
        }
    }

}
