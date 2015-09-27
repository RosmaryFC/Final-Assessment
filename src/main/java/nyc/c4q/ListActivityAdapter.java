package nyc.c4q;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by c4q-rosmary on 8/30/15.
 */
public class ListActivityAdapter extends ArrayAdapter<Person> {

    Context context;
    int layoutResourceId;
    ArrayList <Person> data;
    AdapterBoolean isSortedByFirstName;
    AdapterBoolean colorIsShown;
    final Map<String, Integer> HOUSE_COLORS;


    public ListActivityAdapter(Context context, int layoutResourceId, ArrayList<Person> data, AdapterBoolean isSortedByFirstName, AdapterBoolean colorIsShown) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.isSortedByFirstName = isSortedByFirstName;
        this.colorIsShown = colorIsShown;

        HOUSE_COLORS = new TreeMap<>();
        HOUSE_COLORS.put("Gryffindor", R.color.gryffindor_red);
        HOUSE_COLORS.put("Ravenclaw",  R.color.ravenclaw_blue);
        HOUSE_COLORS.put("Hufflepuff", R.color.hufflepuff_yellow);
        HOUSE_COLORS.put("Slytherin",  R.color.slytherin_green);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PersonHolder holder = null;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PersonHolder();
            holder.mHouse = (TextView) row.findViewById(R.id.text_house);
            holder.mName = (TextView) row.findViewById(R.id.text_name);

            row.setTag(holder);
        } else {
            holder = (PersonHolder) row.getTag();
        }

        Person currentPerson = data.get(position);
        holder.mHouse.setText(currentPerson.house.toString());

        if(isSortedByFirstName.getBooleanValue()){
            holder.mName.setText(currentPerson.firstName + " " + currentPerson.lastName);
        }else{
            holder.mName.setText(currentPerson.lastName + ", " + currentPerson.firstName);
        }

        String personHouse = holder.mHouse.getText().toString();
        if(colorIsShown.getBooleanValue()){
            row.setBackgroundResource(HOUSE_COLORS.get(personHouse));
        }else {
            row.setBackgroundResource(0);
        }

        return row;
    }

    public static class PersonHolder {
        TextView mHouse;
        TextView mName;
    }
}
