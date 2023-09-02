package assortment_of_things.relics.interactions

import assortment_of_things.misc.RATInteractionPlugin
import com.fs.starfarer.api.Global
import com.fs.starfarer.api.characters.SkillSpecAPI
import com.fs.starfarer.api.impl.campaign.ids.Sounds
import com.fs.starfarer.api.loading.Description
import com.fs.starfarer.api.ui.BaseTooltipCreator
import com.fs.starfarer.api.ui.Fonts
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import lunalib.backend.ui.components.util.TooltipHelper
import lunalib.lunaExtensions.addLunaElement
import lunalib.lunaExtensions.openLunaCustomPanel
import lunalib.lunaUI.panel.LunaBaseCustomPanelPlugin
import org.lwjgl.input.Keyboard

class SkillStationInteraction : RATInteractionPlugin() {



    override fun init() {

        var spec = interactionTarget.customEntitySpec
        var skillSpec = when(spec.id) {
            "rat_bioengineering_station" -> Global.getSettings().getSkillSpec("rat_biomutant")
            "rat_augmentation_station" -> Global.getSettings().getSkillSpec("rat_augmented")
            "rat_neural_laboratory" -> Global.getSettings().getSkillSpec("rat_mass_interface")
            else -> Global.getSettings().getSkillSpec("rat_biomutant")
        }

        textPanel.addPara("Your fleet approaches a ${spec.defaultName.lowercase()}")

        textPanel.addPara(Global.getSettings().getDescription(interactionTarget.customDescriptionId, Description.Type.CUSTOM).text1)

        createOption("Explore") {
            clearOptions()

            textPanel.addPara("Your salvage team works through the facility, shifting through offices oppressed with inert and burnt out personal computers, stalking through laboratories littered with fine but broken scientific equipment, and passing through patient rooms rotted with mold and dust.\n\n" +
                    "Suddenly there's a ripple through the comms, first one salvor, then another chirping loudly in amazement at their discovery. You watch as the telemetry feed is put onto the main screen and...there it is.\n\n" +
                    " A bio-reconstructor. ", Misc.getTextColor(), Misc.getHighlightColor(), "bio-reconstructor")

            createOption("Continue") {
                clearOptions()

                textPanel.addPara("You scarcely believe that you have stumbled upon it - these miraculous devices were nothing more than myth during the time of the Domain, and after its fall these devices were consigned firmly to drunken rumor and magical cure-alls used in movie-holos.\n\n" +
                        "But here one sits, active and functional on your telemetry feed.\n\n" +
                        "You consider your options as the salvors gawk at the device, and your Sensor Officer records every detail they can of the device under the badgering of your head Engineer.",
                    Misc.getTextColor(), Misc.getHighlightColor(), "active and functional")

                textPanel.addPara("This specific device can perform the following change.")

                var tooltip = textPanel.beginTooltip()

                tooltip.setParaFont(Fonts.ORBITRON_12)
                tooltip.addPara("(Hover over the icon for a detailed description)", 0f, Misc.getGrayColor(), Misc.getGrayColor())
                var fake = Global.getFactory().createPerson()
                fake.stats.setSkillLevel(skillSpec.id, 1f)
                tooltip.addSkillPanel(fake, 0f)

                textPanel.addTooltip()

                textPanel.addPara("Any officer can be selected for the procedure. This change is permanent and can only applied to a single person.")

                addOfficers(skillSpec)
                addLeaveOption()
            }



        }

        addLeaveOption()
    }

    fun addOfficers(skillSpec: SkillSpecAPI) {
        var officers = listOf(Global.getSector().playerPerson) + Global.getSector().playerFleet.fleetData.officersCopy.map { it.person }

        var panel = visualPanel.showCustomPanel(400f, 450f, null)

        var element = panel.createUIElement(400f, 450f, true)

        element.addSpacer(10f)
        for (officer in officers) {
            var img = element.beginImageWithText(officer.portraitSprite, 64f)

            var unapplicable = officer == Global.getSector().playerPerson && skillSpec.id == "rat_augmented"



            if (unapplicable) {

                img.addLunaElement(280f, 40f).apply {
                    addText("Can not be applied to the player", Misc.getNegativeHighlightColor())
                    centerText()

                    borderAlpha = 0.6f
                    backgroundColor = Misc.getDarkPlayerColor().darker().darker()
                    enableTransparency = true

                }

                element.addImageWithText(0f)

                element.addSpacer(15f)
                continue
            }

            img.addLunaElement(280f, 40f).apply {
                addText("Choose \"${officer.nameString}\"", Misc.getBasePlayerColor())
                centerText()

                borderAlpha = 0.6f
                backgroundColor = Misc.getDarkPlayerColor().darker().darker()
                enableTransparency = true

                onHoverEnter {
                    playScrollSound()
                    borderAlpha = 1f
                }
                onHoverExit {
                    borderAlpha = 0.6f
                }

                onClick {
                    playClickSound()
                    Global.getSoundPlayer().playUISound(Sounds.STORY_POINT_SPEND, 1f, 1f)

                    clearOptions()

                    panel.removeComponent(element.externalScroller)
                    panel.removeComponent(element)

                    textPanel.addPara("Choosen ${officer.nameString}", Misc.getBasePlayerColor(), Misc.getBasePlayerColor())

                    textPanel.addPara("Youve choosen ${officer.nameString} as the participant of the procedure, and hours later, they awaken as something new.")

                    textPanel.addPara("> ${officer.nameString} acquired the ${skillSpec.name} skill", Misc.getPositiveHighlightColor(), Misc.getPositiveHighlightColor())

                    officer.stats.setSkillLevel(skillSpec.id, 1f)

                    createOption("Leave") {
                        closeDialog()
                        Misc.fadeAndExpire(interactionTarget)
                    }
                    optionPanel.setShortcut("Leave", Keyboard.KEY_ESCAPE, false, false, false, true);
                }
            }

            img.addTooltipToPrevious(object : BaseTooltipCreator() {

                override fun createTooltip(tooltip: TooltipMakerAPI?, expanded: Boolean, tooltipParam: Any?) {
                    tooltip!!.addSkillPanel(officer, 0f)
                }

            }, TooltipMakerAPI.TooltipLocation.BELOW)

            element.addImageWithText(0f)

            element.addSpacer(15f)
        }


        panel.addUIElement(element)

    }
}
