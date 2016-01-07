package ics4u.ics4u_final_project;

public class Recipe {
    String name;
    String age;
    int photoId;

    Recipe(String name, String age, int photoId) {
        this.name = name;
        this.age = age;
        this.photoId = photoId;
    }
    public String getName(){
        return this.name;
    }
}
