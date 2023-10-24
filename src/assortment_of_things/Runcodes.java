package assortment_of_things;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.impl.campaign.fleets.PersonalFleetHoracioCaden;
import com.fs.starfarer.api.impl.campaign.fleets.PersonalFleetScript;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;

public class Runcodes {

    public void Example() {

        SectorAPI sector = Global.getSector();


        if (sector.hasScript(PersonalFleetHoracioCaden.class)) {
            PersonalFleetScript fleetScript = null;

            for (EveryFrameScript script : Global.getSector().getScripts()) {
                if (script instanceof PersonalFleetHoracioCaden) {
                    fleetScript = (PersonalFleetHoracioCaden) script;
                }
            }

            if (fleetScript != null) {
                fleetScript.getFleet().despawn();
                sector.removeScript(fleetScript);
            }
        }

    }
}
