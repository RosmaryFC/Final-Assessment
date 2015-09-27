package nyc.c4q;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ListActivity extends Activity {

    public ListView list;
    ListActivityAdapter adapter;

    ArrayList<Person> orderedPersons;

    Button nameBtn;
    Button colorBtn;

    AdapterBoolean isSortedByFirstName;
    AdapterBoolean colorIsShown;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String SHOW_COLOR = "showColor";
    private static final String SORT_BY_FIRST_NAME = "sortByFirstName";

    public static final Person[] PEOPLE = {
            new Person("Hannah", "Abbott", House.Hufflepuff),
            new Person("Katie", "Bell", House.Gryffindor),
            new Person("Susan", "Bones", House.Hufflepuff),
            new Person("Terry", "Boot", House.Ravenclaw),
            new Person("Lavender", "Brown", House.Gryffindor),
            new Person("Cho", "Chang", House.Ravenclaw),
            new Person("Michael", "Corner", House.Ravenclaw),
            new Person("Colin", "Creevey", House.Gryffindor),
            new Person("Marietta", "Edgecombe", House.Ravenclaw),
            new Person("Justin", "Finch-Fletchley", House.Hufflepuff),
            new Person("Seamus", "Finnigan", House.Gryffindor),
            new Person("Anthony", "Goldstein", House.Ravenclaw),
            new Person("Hermione", "Granger", House.Gryffindor),
            new Person("Angelina", "Johnson", House.Gryffindor),
            new Person("Lee", "Jordan", House.Gryffindor),
            new Person("Neville", "Longbottom", House.Gryffindor),
            new Person("Luna", "Lovegood", House.Ravenclaw),
            new Person("Ernie", "Macmillan", House.Hufflepuff),
            new Person("Parvati", "Patil", House.Gryffindor),
            new Person("Padma", "Patil", House.Ravenclaw),
            new Person("Harry", "Potter", House.Gryffindor),
            new Person("Zacharias", "Smith", House.Hufflepuff),
            new Person("Alicia", "Spinnet", House.Gryffindor),
            new Person("Dean", "Thomas", House.Gryffindor),
            new Person("Fred", "Weasley", House.Gryffindor),
            new Person("George", "Weasley", House.Gryffindor),
            new Person("Ginny", "Weasley", House.Gryffindor),
            new Person("Ron", "Weasley", House.Gryffindor)
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        prefs = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        nameBtn = (Button) findViewById(R.id.button_name);
        nameBtn.setOnClickListener(nameListener);

        colorBtn = (Button) findViewById(R.id.button_color);
        colorBtn.setOnClickListener(colorListener);


        ArrayList<Person> listOfPeople = new ArrayList<>();
        for (int i = 0; i < PEOPLE.length; i++) {
            listOfPeople.add(PEOPLE[i]);
        }
        orderedPersons = listOfPeople;


        isSortedByFirstName = new AdapterBoolean(prefs.getBoolean(SORT_BY_FIRST_NAME, true));
        if(isSortedByFirstName.getBooleanValue()){
            sortAlphabeticallyFirstName(orderedPersons);
            nameBtn.setText("Last, First");
        }else{
            sortAlphabeticallyLastName(orderedPersons);
            nameBtn.setText("First Last");
        }

        colorIsShown = new AdapterBoolean(prefs.getBoolean(SHOW_COLOR, false));
        if(colorIsShown.getBooleanValue()){
            colorBtn.setText("Hide Color");
        }else{
            colorBtn.setText("Show Color");
        }

        adapter = new ListActivityAdapter(this, R.layout.listitem_member, orderedPersons, isSortedByFirstName, colorIsShown);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

    }

    public ArrayList<Person> sortAlphabeticallyFirstName(ArrayList<Person> listOfPeople) {

        Collections.sort(listOfPeople, new Comparator<Person>() {
            @Override
            public int compare(Person primaryPerson, Person otherPerson) {

                return primaryPerson.firstName.compareTo(otherPerson.firstName);
            }
        });

        return listOfPeople;

    }

    public ArrayList<Person> sortAlphabeticallyLastName(ArrayList<Person> listOfPeople) {

        Collections.sort(listOfPeople, new Comparator<Person>() {
            @Override
            public int compare(Person primaryPerson, Person otherPerson) {

                if (primaryPerson.lastName.equals(otherPerson.lastName)) {
                    return primaryPerson.firstName.compareTo(otherPerson.firstName);
                }

                return primaryPerson.lastName.compareTo(otherPerson.lastName);
            }
        });

        return listOfPeople;

    }

    public View.OnClickListener nameListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(ListActivity.this, "name Btn clicked", Toast.LENGTH_SHORT).show();

            String nameBtnText = nameBtn.getText().toString();

            if (nameBtnText.equals("Last, First")) {
                editor = prefs.edit();
                editor.putBoolean(SORT_BY_FIRST_NAME, false);
                editor.apply();

                sortAlphabeticallyLastName(orderedPersons);
                isSortedByFirstName.setBooleanValue(prefs.getBoolean(SORT_BY_FIRST_NAME, false));

                adapter.notifyDataSetChanged();

                nameBtn.setText("First Last");
            }

            if (nameBtnText.equals("First Last")) {
                editor = prefs.edit();
                editor.putBoolean(SORT_BY_FIRST_NAME, true);
                editor.apply();

                sortAlphabeticallyFirstName(orderedPersons);
                isSortedByFirstName.setBooleanValue(prefs.getBoolean(SORT_BY_FIRST_NAME, true));

                //list.refreshDrawableState();
                //list.invalidate();
                //list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //((ArrayAdapter<Person>) list.getAdapter()).notifyDataSetChanged();

                nameBtn.setText("Last, First");
            }

        }
    };

    public View.OnClickListener colorListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Toast.makeText(ListActivity.this, "color Btn clicked", Toast.LENGTH_SHORT).show();

            String colorBtnText = colorBtn.getText().toString();

            if (colorBtnText.equals("Show Color")) {
                editor = prefs.edit();
                editor.putBoolean(SHOW_COLOR, true);
                editor.apply();

                colorIsShown.setBooleanValue(prefs.getBoolean(SHOW_COLOR, true));
                adapter.notifyDataSetChanged();

                colorBtn.setText("Hide Color");
            }
            if (colorBtnText.equals("Hide Color")) {
                editor = prefs.edit();
                editor.putBoolean(SHOW_COLOR, false);
                editor.apply();

                colorIsShown.setBooleanValue(prefs.getBoolean(SHOW_COLOR, false));
                adapter.notifyDataSetChanged();

                colorBtn.setText("Show Color");
            }
        }

    };



}