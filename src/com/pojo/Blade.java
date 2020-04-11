package com.pojo;

public class Blade {

    public Blade(String bladeAttr, String bladeAttrCd) {
        this.setBladeAttr(bladeAttr);
        this.setBladeAttrCd(bladeAttrCd);
    }

    private String bladeName;

    private String bladeAttr;

    private String bladeAttrCd;

    private String[] bladeSkill;

    public String getBladeAttrCd() {
        return bladeAttrCd;
    }

    public void setBladeAttrCd(String bladeAttrCd) {
        this.bladeAttrCd = bladeAttrCd;
    }

    public String getBladeName() {
        return bladeName;
    }

    public void setBladeName(String bladeName) {
        this.bladeName = bladeName;
    }

    public String getBladeAttr() {
        return bladeAttr;
    }

    public void setBladeAttr(String bladeAttr) {
        this.bladeAttr = bladeAttr;
    }

    public String[] getBladeSkill() {
        return bladeSkill;
    }

    public void setBladeSkill(String[] bladeSkill) {
        this.bladeSkill = bladeSkill;
    }

    @Override
    public String toString() {
        return bladeAttr;
    }

}
