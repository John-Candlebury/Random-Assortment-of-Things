package assortment_of_things.modular_weapons.effects

import assortment_of_things.modular_weapons.data.SectorWeaponData
import com.fs.starfarer.api.ui.TooltipMakerAPI


class StatQuickloader : ModularWeaponEffect() {
    override fun getName(): String {
        return "Quickloader"
    }

    override fun getCost(): Int {
        return 20
    }

    override fun getIcon(): String {
        return ""
    }

    override fun getTooltip(tooltip: TooltipMakerAPI) {
        tooltip.addPara("Increases the Weapons Firerate by 25%.", 0f)
    }

    override fun getResourceCost(): MutableMap<String, Float> {
        return hashMapOf()
    }

    override fun getType(): ModularEffectType {
        return ModularEffectType.Stat
    }

    override fun addStats(stats: SectorWeaponData) {
        super.addStats(stats)

        stats.burstDelay.addMult(getName(), 0.75f)
        stats.chargeDown.addMult(getName(), 0.75f)
        stats.chargeUp.addMult(getName(), 0.75f)
    }

}