package laboratorio.app.fragments.forms.purchase;

import laboratorio.app.R;
import laboratorio.app.fragments.forms.MultiStepperFormFragment;

public class PurchaseFragment extends MultiStepperFormFragment {
    final private static int TOTAL_STEPS = 4;

    @Override
    public int getMenuId() {
        return R.menu.menu_purchase_stepform;
    }

    @Override
    public int getNavGraphId() {
        return R.menu.purchase_nav_stepper;
    }

    @Override
    public void onCompleted() {
        System.out.println("Complete!");
    }

    @Override
    public int getTotalSteps() {
        return TOTAL_STEPS;
    }
}
