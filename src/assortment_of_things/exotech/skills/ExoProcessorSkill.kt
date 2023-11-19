package assortment_of_things.exotech.skills

import assortment_of_things.campaign.skills.RATBaseShipSkill
import com.fs.starfarer.api.characters.LevelBasedEffect
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI
import com.fs.starfarer.api.characters.SkillSpecAPI
import com.fs.starfarer.api.combat.MutableShipStatsAPI
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.impl.hullmods.AdaptivePhaseCoils
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc

class ExoProcessorSkill : RATBaseShipSkill() {

    val missileAmmoBonus = 25f
    val missileSpeedMult = 1.33f
    var modID = "rat_exo_processor"

    override fun getScopeDescription(): LevelBasedEffect.ScopeDescription {
        return LevelBasedEffect.ScopeDescription.PILOTED_SHIP
    }

    override fun createCustomDescription(stats: MutableCharacterStatsAPI?,  skill: SkillSpecAPI?, info: TooltipMakerAPI?,  width: Float) {
        info!!.addSpacer(2f)

        info.addPara("+200%% more hardflux required to reach minimum phase-speed.", 0f, Misc.getHighlightColor(), Misc.getHighlightColor())
        info.addPara("+25%% missile weapon ammo capacity", 0f, Misc.getHighlightColor(), Misc.getHighlightColor())
        info.addPara("+33%% missile speed", 0f, Misc.getHighlightColor(), Misc.getHighlightColor())

        info.addSpacer(2f)
    }

    override fun apply(stats: MutableShipStatsAPI?, hullSize: ShipAPI.HullSize?, id: String?, level: Float) {
        stats!!.missileAmmoBonus.modifyPercent(id, missileAmmoBonus)

        stats.dynamic.getMod(Stats.PHASE_CLOAK_FLUX_LEVEL_FOR_MIN_SPEED_MOD)
            .modifyPercent(id, 200f)

        stats!!.missileMaxSpeedBonus.modifyMult(id, missileSpeedMult)
        stats!!.missileAccelerationBonus.modifyMult(id, missileSpeedMult)
        stats!!.missileMaxTurnRateBonus.modifyMult(id, missileSpeedMult)
        stats!!.missileTurnAccelerationBonus.modifyMult(id, missileSpeedMult)

    }

    override fun unapply(stats: MutableShipStatsAPI?, hullSize: ShipAPI.HullSize?, id: String?) {

    }

}