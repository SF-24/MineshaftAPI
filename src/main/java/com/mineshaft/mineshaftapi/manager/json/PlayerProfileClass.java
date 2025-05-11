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

package com.mineshaft.mineshaftapi.manager.json;

import java.util.ArrayList;

public class PlayerProfileClass {

    String currentProfile = "Default";
    ArrayList<String> profiles = new ArrayList<>();

    public void setCurrentProfile(String currentProfile) {this.currentProfile=currentProfile;}
    public String getCurrentProfile() {return this.currentProfile;}

    public void setProfiles(ArrayList<String> profiles) {this.profiles = profiles;}
    public ArrayList<String> getProfiles() {return this.profiles;}

    public void addProfile(String profile) {this.profiles.add(profile);}
    public void removeProfile(String profile) {this.profiles.remove(profile);}
}
