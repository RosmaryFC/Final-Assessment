package nyc.c4q;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class PaceCalculatorActivity extends FragmentActivity {

    PaceCalcFragment firstFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pace_calculator);

        // Check that the activity is using the layout version with the fragment_container FrameLayout
        if (findViewById(R.id.activity_pace_calculator) != null) {
            // if we are being restored from a previous state, then we dont need to do anything and should
            // return or else we could end up with overlapping fragments.
            if (savedInstanceState != null)
                return;

            // Create an instance of editorFrag
            firstFrag = new PaceCalcFragment();

            // add fragment to the fragment container layout
            getFragmentManager().beginTransaction().add(R.id.activity_pace_calculator, firstFrag).commit();
        }

    }




}
