package com.drmangotea.createindustry.base;

import com.drmangotea.createindustry.CreateTFMG;
import com.google.common.base.Supplier;
import com.google.gson.JsonElement;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.data.LangPartial;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.simibubi.create.foundation.utility.Lang;

public enum TFMGLangPartials implements LangPartial {
    INTERFACE("UI & Messages"),

    TOOLTIPS("Tooltips"),
    PONDERS("Ponders");

    ;

    private final String display;
    private final Supplier<JsonElement> provider;

    private TFMGLangPartials(String display) {
        this.display = display;
        this.provider = this::fromResource;
    }


    public String getDisplayName() {
        return display;
    }

    public JsonElement provide() {
        return provider.get();
    }

    private JsonElement fromResource() {
        String fileName = Lang.asId(name());
        String filepath = "assets/" + CreateTFMG.MOD_ID + "/lang/default/" + fileName + ".json";
        JsonElement element = FilesHelper.loadJsonResource(filepath);
        return element;
    }

}