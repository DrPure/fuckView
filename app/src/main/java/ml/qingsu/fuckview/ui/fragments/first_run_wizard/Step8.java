package ml.qingsu.fuckview.ui.fragments.first_run_wizard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ml.qingsu.fuckview.utils.wizard.WizardStep;


/**
 * Created by w568w on 2017-7-6.
 */

public class Step8 extends WizardStep {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return Helper.getTextView(inflater.getContext(), ":)", "That's all.\n\n好好享受它!~");
    }
}
