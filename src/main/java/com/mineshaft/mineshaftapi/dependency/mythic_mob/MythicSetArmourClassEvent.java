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

package com.mineshaft.mineshaftapi.dependency.mythic_mob;

import com.mineshaft.mineshaftapi.manager.entity.armour_class.ArmourManager;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.INoTargetSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;

public class MythicSetArmourClassEvent implements INoTargetSkill {

    protected final int value;

    public MythicSetArmourClassEvent(MythicLineConfig config) {
        this.value = config.getInteger(new String[] {"armour", "a", "value", "v"}, 0);
    }

    @Override
    public SkillResult cast(SkillMetadata data) {
        data.getParameters();
        ArmourManager.setArmourClass(data.getCaster().getEntity().getBukkitEntity(), value);
        return SkillResult.SUCCESS;
    }
}
