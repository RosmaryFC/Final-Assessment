package nyc.c4q;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ListActivity extends Activity {

    public ListView list;
    ListActivityAdapter adapter;

    Button nameBtn;
    Button colorBtn;

    public static final Person[] PEOPLE = {
        new Person("Hannah",    "Abbott",          House.Hufflepuff),
        new Person("Katie",     "Bell",            House.Gryffindor),
        new Person("Susan",     "Bones",           House.Hufflepuff),
        new Person("Terry",     "Boot",            House.Ravenclaw),
        new Person("Lavender",  "Brown",           House.Gryffindor),
        new Person("Cho",       "Chang",           House.Ravenclaw),
        new Person("Michael",   "Corner",          House.Ravenclaw),
        new Person("Colin",     "Creevey",         House.Gryffindor),
        new Person("Marietta",  "Edgecombe",       House.Ravenclaw),
        new Person("Justin",    "Finch-Fletchley", House.Hufflepuff),
        new Person("Seamus",    "Finnigan",        House.Gryffindor),
        new Person("Anthony",   "Goldstein",       House.Ravenclaw),
        new Person("Hermione",  "Granger",         House.Gryffindor),
        new Person("Angelina",  "Johnson",         House.Gryffindor),
        new Person("Lee",       "Jordan",          House.Gryffindor),
        new Person("Neville",   "Longbottom",      House.Gryffindor),
        new Person("Luna",      "Lovegood",        House.Ravenclaw),
        new Person("Ernie",     "Macmillan",       House.Hufflepuff),
        new Person("Parvati",   "Patil",           House.Gryffindor),
        new Person("Padma",     "Patil",           House.Ravenclaw),
        new Person("Harry",     "Potter",          House.Gryffindor),
        new Person("Zacharias", "Smith",           House.Hufflepuff),
        new Person("Alicia",    "Spinnet",         House.Gryffindor),
        new Person("Dean",      "Thomas",          House.Gryffindor),
        new Person("Fred",      "Weasley",         House.Gryffindor),
        new Person("George",    "Weasley",         House.Gryffindor),
        new Person("Ginny",     "Weasley",         House.Gryffindor),
        new Person("Ron",       "Weasley",         House.Gryffindor)
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        sortAlphabetically(PEOPLE);

        adapter = new ListActivityAdapter(this, R.layout.listitem_member, PEOPLE);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    public static void sortAlphabetically (Person [] people ){

        ArrayList <Person> listOfPeople = new ArrayList<>();
        for(int i =0; i<people.length; i++){
            listOfPeople.add(people[i]);
        }
        Collections.sort(listOfPeople, new Comparator<Person>() {
            @Override
            public int compare(Person person, Person otherPerson) {
                return ;
            }
        });




//        for(int i = 0 ; i < people.length; i++) {
//
//            Person currentPerson = people[i];
//            String currentPersonName = currentPerson.firstName;
//
//            for(int x = 1 ; x < people.length -1; x++){
//
//                Person comparedPerson = people[x];
//                String comparedPersonName = comparedPerson.firstName;
//
//                if(currentPersonName.)
//
//            }
//
//        }
//
//
//
//
//        for(int i = 0; i < people.length; i++){
//
//
//            String log = "First Name: " + people[i].firstName + "Last Name: " + people[i].lastName + "House: " + people[i].house;
//
//            Log.d("APLHA ORDER PPL: ", log);
//        }

    }



}
