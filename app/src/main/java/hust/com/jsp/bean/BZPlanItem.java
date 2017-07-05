package hust.com.jsp.bean;

import android.graphics.Color;

/**
 * Created by hust on 2017/1/13.
 */

public class BZPlanItem {

    private Station station; //ZW
    private float spendTime;

    private boolean addGas;//油
    private  boolean addAir;//气
    private boolean addElectricity;//电
    private boolean addFluid;//液
    private boolean addWeapon;//弹
    private boolean addGuide;//导
    private  boolean addCool;//冷
    private boolean addOxygen;//氧

    public String[] getLabels() {
        return labels;
    }

    //    private String gasLabel="油";
//    private String airLabel="气";
//    private String electricityLabel="电";
//    private String fluidLabel="液";
//    private String weaponLabel="弹";
//    private String guideLabel="导";
//    private String coolLabel="冷";
//    private String oxygenLabel="氧";
    private String[] labels=new String[]{"油","气","电","液","弹","导","冷","氧",""};

    public int[] getColors() {
        return colors;
    }

    private int[] colors=new int[]{
            Color.parseColor("#AEEEEE"),
            Color.parseColor("#B4EEB4"),
            Color.parseColor("#FFB90F"),
            Color.parseColor("#C6E2FF"),
            Color.parseColor("#87CEFF"),
            Color.parseColor("#48D1CC"),
            Color.parseColor("#00FF00"),
            Color.parseColor("#FFBBFF"),
            Color.LTGRAY
    };

    public boolean[] getActions() {
        return actions;
    }

    private boolean[] actions=new boolean[]{false,false,false,false,false,false,false,false,false};


    public void setStation(Station station) {
        this.station = station;
    }

    public Station getStation() {
        return station;
    }

    public float getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(float spendTime) {
        this.spendTime = spendTime;
    }

    public boolean isAddGas() {
        return addGas;
    }

    public void setAddGas(boolean addGas) {
        actions[0]=addGas;
        this.addGas = addGas;
    }

    public boolean isAddAir() {
        return addAir;
    }

    public void setAddAir(boolean addAir) {
        actions[1]=addAir;
        this.addAir = addAir;
    }

    public boolean isAddElectricity() {
        return addElectricity;
    }

    public void setAddElectricity(boolean addElectricity) {
        actions[2]=addElectricity;
        this.addElectricity = addElectricity;
    }

    public boolean isAddFluid() {
        return addFluid;
    }

    public void setAddFluid(boolean addFluid) {
        actions[3]=addFluid;
        this.addFluid = addFluid;
    }

    public boolean isAddWeapon() {
        return addWeapon;
    }

    public void setAddWeapon(boolean addWeapon) {
        actions[4]=addWeapon;
        this.addWeapon = addWeapon;
    }

    public boolean isAddGuide() {
        return addGuide;
    }

    public void setAddGuide(boolean addGuide) {
        actions[5]=addGuide;
        this.addGuide = addGuide;
    }

    public boolean isAddCool() {
        return addCool;
    }

    public void setAddCool(boolean addCool) {
        actions[6]=addCool;
        this.addCool = addCool;
    }

    public boolean isAddOxygen() {
        return addOxygen;
    }

    public void setAddOxygen(boolean addOxygen) {
        actions[7]=addOxygen;
        this.addOxygen = addOxygen;
    }
//    public String getGasLabel() {
//        return gasLabel;
//    }
//
//    public String getAirLabel() {
//        return airLabel;
//    }
//
//    public String getElectricityLabel() {
//        return electricityLabel;
//    }
//
//    public String getFluidLabel() {
//        return fluidLabel;
//    }
//
//    public String getWeaponLabel() {
//        return weaponLabel;
//    }
//
//    public String getGuideLabel() {
//        return guideLabel;
//    }
//
//    public String getCoolLabel() {
//        return coolLabel;
//    }
//
//    public String getOxygenLabel() {
//        return oxygenLabel;
//    }






}
