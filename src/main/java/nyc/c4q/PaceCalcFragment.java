package nyc.c4q;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by c4q-rosmary on 8/30/15.
 */
public class PaceCalcFragment extends Fragment {

    EditText distanceEt;
    EditText timeMinEt;
    EditText timeSecEt;
    EditText paceMinEt;
    EditText paceSecEt;

    Button calculateBtn;

    View paceCalcView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        paceCalcView = inflater.inflate(R.layout.fragment_pace_calculator, container, false);

        initializeViews();

        calculateBtn.setOnClickListener(calculateListener);

        return paceCalcView;
    }


    public void initializeViews () {
        distanceEt = (EditText) paceCalcView.findViewById(R.id.input_distance);

        timeMinEt = (EditText) paceCalcView.findViewById(R.id.input_time_min);
        timeSecEt = (EditText) paceCalcView.findViewById(R.id.input_time_sec);

        paceMinEt = (EditText) paceCalcView.findViewById(R.id.input_pace_min);
        paceSecEt = (EditText) paceCalcView.findViewById(R.id.input_pace_sec);

        calculateBtn = (Button) paceCalcView.findViewById(R.id.button_calculate);
    }

    public View.OnClickListener calculateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Toast.makeText(paceCalcView.getContext(), "calculating...", Toast.LENGTH_SHORT).show();

            String timeMinString = timeMinEt.getText().toString();
            String timeSecString = timeSecEt.getText().toString();

            String paceMinString = paceMinEt.getText().toString();
            String paceSecString = paceSecEt.getText().toString();

            String distanceString = distanceEt.getText().toString();

            double timeSec;
            double paceSec;
            double distance;

            if (timeMinString.equals("") && timeSecString.equals("") && paceMinString.equals("") && paceSecString.equals("") && distanceString.equals("")) {

                timeMinEt.setText("");
                timeSecEt.setText("");
                paceMinEt.setText("");
                paceSecEt.setText("");
                distanceEt.setText("");

            } else if (containsLetters(distanceString) || containsLetters(paceSecString) || containsLetters(paceMinString) || containsLetters(timeSecString) || containsLetters(timeMinString)) {

                timeMinEt.setText("");
                timeSecEt.setText("");
                paceMinEt.setText("");
                paceSecEt.setText("");
                distanceEt.setText("");

            } else if (distanceString.equals("")) {

                timeSec = ((Integer.parseInt(timeMinString) * 60) + Integer.parseInt(timeSecString));
                paceSec = ((Integer.parseInt(paceMinString) * 60) + Integer.parseInt(paceSecString));

                double distanceResult = timeSec / paceSec;
                distanceEt.setText(distanceResult + "");

            } else if (timeMinString.equals("") & timeSecString.equals("")) {

                paceSec = ((Integer.parseInt(paceMinString) * 60) + Integer.parseInt(paceSecString));
                distance = Double.parseDouble(distanceString);

                double timeResultSec = (paceSec * distance);
                double resultSecToMin = (timeResultSec / 60);

                String[] arr = String.valueOf(resultSecToMin).split("\\.");

                int timeMinResult = Integer.parseInt(arr[0]);//whole number
                int timeSecResult = Integer.parseInt(arr[1].substring(0, 1));// first decimal only

                timeMinEt.setText(timeMinResult + "");
                timeSecEt.setText(timeSecResult + "0");

            } else if (paceMinString.equals("") & paceSecString.equals("")) {

                //I believe there is an error in the test04.
                // time 23 min and 22 sec(1402 sec) / distance 3.1 should equal 7 min 53 sec (452.25806 sec)

                timeSec = ((Integer.parseInt(timeMinString) * 60) + Integer.parseInt(timeSecString));
                distance = Double.parseDouble(distanceString);

                double paceResultSec = (timeSec / distance);
                double resultSecToMin = (paceResultSec / 60);

                String[] arr = String.valueOf(resultSecToMin).split("\\.");

                int paceMinResult = Integer.parseInt(arr[0]);//whole number
                int paceSecResult = Integer.parseInt(arr[1].substring(0, 2));//first two decimals only

                paceMinEt.setText(paceMinResult + "");
                paceSecEt.setText(paceSecResult + "");

            }
        }
    };


    public boolean containsLetters(String value) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < alphabet.length(); i++) {
            String currentChar = String.valueOf(alphabet.charAt(i));
            if (value.contains(currentChar)) {
                return true;
            }
        }
        return false;
    }



}
