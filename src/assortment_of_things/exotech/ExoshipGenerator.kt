package assortment_of_things.exotech

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.SectorEntityToken
import com.fs.starfarer.api.campaign.StarSystemAPI
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.impl.campaign.ids.Conditions
import com.fs.starfarer.api.impl.campaign.ids.Industries
import com.fs.starfarer.api.impl.campaign.ids.Submarkets
import com.fs.starfarer.api.impl.campaign.ids.Tags
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.EntityLocation
import com.fs.starfarer.api.util.Misc
import java.util.*
import kotlin.collections.ArrayList

object ExoshipGenerator {


    fun generate(name: String) {
        var system = Global.getSector().starSystems.filter { it.planets.any { planet -> !planet.isStar } && !it.hasBlackHole() && !it.hasPulsar() && !it.hasTag(
            Tags.THEME_HIDDEN) && !it.hasTag(Tags.THEME_CORE)}.random()
        var location = BaseThemeGenerator.getLocations(Random(), system, 100f, linkedMapOf(BaseThemeGenerator.LocationType.PLANET_ORBIT to 1000f, BaseThemeGenerator.LocationType.STAR_ORBIT to 0.1f)).pick()

        if (location == null) return

        var exoshipEntity = system.addCustomEntity("exoship_${Misc.genUID()}", "$name (Exoship)", "rat_exoship", "rat_exotech")
        exoshipEntity.orbit = location.orbit

        var market = addMarketplace("rat_exotech", exoshipEntity, arrayListOf(), "$name", 4,
        arrayListOf(Conditions.OUTPOST),
        arrayListOf(Submarkets.SUBMARKET_OPEN),
        arrayListOf(Industries.MEGAPORT, Industries.WAYSTATION, Industries.MILITARYBASE, Industries.ORBITALWORKS),
        0.3f, false, false)
        market.isHidden = true
    }



    //Utility Method from Tahlan-Shipworks by NiaTahl
    fun addMarketplace(factionID: String?, primaryEntity: SectorEntityToken, connectedEntities: ArrayList<SectorEntityToken>?, name: String?,
                       size: Int, marketConditions: ArrayList<String>, submarkets: ArrayList<String>?, industries: ArrayList<String>, tarrif: Float,
                       freePort: Boolean, withJunkAndChatter: Boolean): MarketAPI {

        val globalEconomy = Global.getSector().economy
        val planetID = primaryEntity.id
        val marketID = planetID + "_market"
        val newMarket = Global.getFactory().createMarket(marketID, name, size)
        newMarket.factionId = factionID
        newMarket.primaryEntity = primaryEntity
        newMarket.tariff.modifyFlat("generator", tarrif)

        //Adds submarkets
        if (null != submarkets) {
            for (market in submarkets) {
                newMarket.addSubmarket(market)
            }
        }

        //Adds market conditions
        for (condition in marketConditions) {
            newMarket.addCondition(condition)
        }

        //Add market industries
        for (industry in industries) {
            newMarket.addIndustry(industry)
        }

        //Sets us to a free port, if we should
        newMarket.isFreePort = freePort

        //Adds our connected entities, if any
        if (null != connectedEntities) {
            for (entity in connectedEntities) {
                newMarket.connectedEntities.add(entity)
            }
        }
        //globalEconomy.addMarket(newMarket, withJunkAndChatter)
        primaryEntity.market = newMarket
        primaryEntity.setFaction(factionID)
        if (null != connectedEntities) {
            for (entity in connectedEntities) {
                entity.market = newMarket
                entity.setFaction(factionID)
            }
        }

        //Finally, return the newly-generated market
        return newMarket
    }
}