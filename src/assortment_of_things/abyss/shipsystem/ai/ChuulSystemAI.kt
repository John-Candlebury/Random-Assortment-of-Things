package assortment_of_things.abyss.shipsystem.ai

import assortment_of_things.abyss.hullmods.abyssals.AbyssalsCoreHullmod
import com.fs.starfarer.api.combat.*
import com.fs.starfarer.api.combat.ShipwideAIFlags.AIFlags
import org.lazywizard.lazylib.MathUtils
import org.lazywizard.lazylib.combat.AIUtils
import org.lazywizard.lazylib.combat.CombatUtils
import org.lwjgl.util.vector.Vector2f

class ChuulSystemAI : ShipSystemAIScript {

    var ship: ShipAPI? = null

    override fun init(ship: ShipAPI?, system: ShipSystemAPI?, flags: ShipwideAIFlags?, engine: CombatEngineAPI?) {
        this.ship = ship
    }

    override fun advance(amount: Float, missileDangerDir: Vector2f?, collisionDangerDir: Vector2f?, target: ShipAPI?) {
        if (ship == null) return
        var flags = ship!!.aiFlags
        var system = ship!!.system

        if (target == null) return

        if (!AbyssalsCoreHullmod.isChronosCore(ship!!) && !AbyssalsCoreHullmod.isCosmosCore(ship!!)) return
        if (system.isCoolingDown) return
        if (system.isActive)  return
        if (!ship!!.areAnyEnemiesInRange()) return
        if (ship!!.fluxLevel > 0.9f) return

        var range = 0f
        for (weapon in ship!!.allWeapons) {
            if (weapon.range > range)
            {
                range = weapon.range
            }
        }
        if (MathUtils.getDistance(ship!!, target) > range + 50) return

        if (flags.hasFlag(AIFlags.BACKING_OFF)) return
        if (flags.hasFlag(AIFlags.BACK_OFF)) return
        if (!flags.hasFlag(AIFlags.MANEUVER_TARGET)) return

        ship!!.useSystem()

    }
}