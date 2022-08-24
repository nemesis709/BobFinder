package edu.skku.testapp.bobfinder;

import java.util.ArrayList;

public class Price {
    private int id;
    private String Prc;

    private static ArrayList<Price> PrcList;

    public Price(int id, String Prc){
        this.id = id;
        this.Prc = Prc;
    }

    public static void initPrice(){
        Price six = new Price(0,"6,000");
        Price eit = new Price(0,"8,000");
        Price ten = new Price(0,"10,000");
        Price twv = new Price(0,"12,000");
        Price fft = new Price(0,"15,000");

        PrcList = new ArrayList<>();
        PrcList.add(six);
        PrcList.add(eit);
        PrcList.add(ten);
        PrcList.add(twv);
        PrcList.add(fft);
    }

    public static ArrayList<Price> getPrcList(){
        return PrcList;
    }

    public static String[] PrcsNames(){
        String[] Prcs = new String[PrcList.size()];
        for(int i=0;i<PrcList.size();i++){
            Prcs[i]=PrcList.get(i).Prc;
        }
        return Prcs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrc() {
        return Prc;
    }

    public void setPrc(String prc) {
        Prc = prc;
    }
}
