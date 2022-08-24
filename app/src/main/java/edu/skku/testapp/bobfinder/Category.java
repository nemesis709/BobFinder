package edu.skku.testapp.bobfinder;

import java.util.ArrayList;

public class Category {
    private int id;
    private String Cat;

    private static ArrayList<Category> CatList;

    public Category(int id, String cat) {
        this.id = id;
        this.Cat = cat;
    }

    public static void initCat(){
        Category han = new Category(0,"한식");
        Category yan = new Category(1,"양식");
        Category zung = new Category(2,"중식");
        Category il = new Category(3,"일식");
        Category asi = new Category(4,"아시안");
        Category veg = new Category(5,"비건/샐러드");

        CatList= new ArrayList<>();
        CatList.add(han);
        CatList.add(yan);
        CatList.add(zung);
        CatList.add(il);
        CatList.add(asi);
        CatList.add(veg);
    }

    public static ArrayList<Category> getCatList() {
        return CatList;
    }

    public static String[] CatNames(){
        String[] Cats = new String[CatList.size()];
        for(int i=0;i<CatList.size();i++){
            Cats[i]=CatList.get(i).Cat;
        }
        return Cats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCat() {
        return Cat;
    }

    public void setCat(String cat) {
        Cat = cat;
    }
}
