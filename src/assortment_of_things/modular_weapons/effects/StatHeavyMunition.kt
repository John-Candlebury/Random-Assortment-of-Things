package assortment_of_things.modular_weapons.effects

import assortment_of_things.modular_weapons.data.SectorWeaponData
import com.fs.starfarer.api.ui.TooltipMakerAPI


class StatHeavyMunition : ModularWeaponEffect() {
    override fun getName(): String {
        return "Heavy Munition"
    }

    override fun getCost(): Int {
        return 30
    }

    override fun getIcon(): String {
        return ""
    }

    override fun getTooltip(tooltip: TooltipMakerAPI) {
        tooltip.addPara("Increases the weapons damage and emp damage by 50%. Also doubles the projectiles width and length. In turn it decreases the weapons flux efficiency and firerate by 20%", 0f)
    }

    override fun getResourceCost(): MutableMap<String, Float> {
        return hashMapOf()
    }

    override fun getType(): ModularEffectType {
        return ModularEffectType.Stat
    }

    override fun addStats(stats: SectorWeaponData) {
        super.addStats(stats)

        stats.damagePerShot.addMult(getName(), 1.5f)
        stats.empDamage.addMult(getName(), 1.5f)

        stats.projectileWidth.addMult(getName(), 2f)
        stats.projectileLength.addMult(getName(), 2f)

        stats.energyPerShot.addMult(getName(), 1.2f)

        stats.burstDelay.addMult(getName(), 1.20f)
        stats.chargeDown.addMult(getName(), 1.20f)
        stats.chargeUp.addMult(getName(), 1.20f)
    }
}