package ics4u.ics4u_final_project;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    String name;
    String age;
    int photoId;

    Recipe(String name, String age, int photoId) {
        this.name = name;
        this.age = age;
        this.photoId = photoId;
    }


private List<Recipe> persons;
    private void initializeData(){
        persons = new ArrayList<>();
        persons.add(new Recipe("Emma Wilson", "23 years old", R.drawable.shaq));
        persons.add(new Recipe("Lavery Maiss", "25 years old", R.drawable.shaq));
        persons.add(new Recipe("Lillie Watts", "35 years old", R.drawable.shaq));
    }
}
