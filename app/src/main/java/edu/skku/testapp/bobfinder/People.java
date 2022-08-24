package edu.skku.testapp.bobfinder;

import java.util.ArrayList;

public class People {

    private int id2;
    private String number;

    private static ArrayList<People> ManList;

    public People(int id2, String peo){
        this.id2 = id2;
        this.number = peo;
    }

    public static void initMan(){
        People P1= new People(0,"1");
        People P2= new People(1,"2");
        People P3= new People(2,"3");
        People P4= new People(3,"4");

        ManList = new ArrayList<>();
        ManList.add(P1);
        ManList.add(P2);
        ManList.add(P3);
        ManList.add(P4);


    }

    public static ArrayList<People> getPeopleList() {
        return ManList;
    }

    public static String[] Men(){
        String[] Men = new String[(ManList.size())];
        for(int i=0;i<ManList.size();i++){
            Men[i]=ManList.get(i).number;
        }
        return Men;
    }

    public int getId() {
        return id2;
    }

    public void setId(int id2) {
        this.id2 = id2;
    }

    public String getPeople() {
        return number;
    }

    public void setPeople(String people) {
        number = people;
    }
}
