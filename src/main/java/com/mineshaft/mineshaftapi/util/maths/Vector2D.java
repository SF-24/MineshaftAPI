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

@Setter @Getter
public class Vector2D {

    double x;
    double y;
    double length;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
        updateLength();
    }

    public String toString() {
        return "Vector2D [x=" + x + ", y=" + y + ", length=" + length + "]";
    }

    private void updateLength() {
        this.length=Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
    }

    public Vector2D normalize() {
        this.x/=length;
        this.y/=length;
        updateLength();
        return this;
    }

    public void normalizeSelf() {
        this.x/=length;
        this.y/=length;
        updateLength();
    }

    public double getLengthSquared() {
        return (x * x) + (y * y);
    }

    public Vector2D add(Vector2D v) {
        this.x+=v.x;
        this.y+=v.y;
        updateLength();
        return this;
    }

    public Vector2D subtract(Vector2D v) {
        this.x-=v.x;
        this.y-=v.y;
        updateLength();
        return this;
    }

    public Vector2D zero() {
        this.x=0;
        this.y=0;
        updateLength();
        return this;
    }

}
