package mindustry.ui.dialogs;

import arc.Core;
import arc.scene.ui.TextButton;
import arc.util.Time;
import mindustry.core.GameState.State;
import mindustry.game.Saves.SaveSlot;
import mindustry.gen.*;

import static mindustry.Vars.*;

public class SaveDialog extends LoadDialog{

    public SaveDialog(){
        super("$savegame");

        update(() -> {
            if(state.isMenu() && isShown()){
                hide();
            }
        });
    }

    public void addSetup(){
        slots.row();
        slots.addImageTextButton("$save.new", Icon.add, () ->
        ui.showTextInput("$save", "$save.newslot", 30, "", text -> {
            ui.loadAnd("$saving", () -> {
                control.saves.addSave(text);
                Core.app.post(() -> Core.app.post(this::setup));
            });
        })
        ).fillX().margin(10f).minWidth(300f).height(70f).pad(4f).padRight(-4);
    }

    @Override
    public void modifyButton(TextButton button, SaveSlot slot){
        button.clicked(() -> {
            if(button.childrenPressed()) return;

            ui.showConfirm("$overwrite", "$save.overwrite", () -> save(slot));
        });
    }

    void save(SaveSlot slot){

        ui.loadfrag.show("$saveload");

        Time.runTask(5f, () -> {
            hide();
            ui.loadfrag.hide();
            try{
                slot.save();
            }catch(Throwable e){
                e.printStackTrace();

                ui.showException("[accent]" + Core.bundle.get("savefail"), e);
            }
        });
    }

}
