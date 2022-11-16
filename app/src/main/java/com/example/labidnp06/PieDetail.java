package com.example.labidnp06;

public class PieDetail {
    private float startDegree;
    private float endDegree;
    private float targetStartDegree;
    private float targetEndDegree;
    private String title;
    private int color;
    private float sweepDegree;

    int velocity = 5;


    public PieDetail(float percent){
        this(percent, null, 0);
    }

    public PieDetail(float percent, int color){
        this(percent, null, color);
    }


    PieDetail(float percent, String title){
        this(percent, title, 0);
    }


    PieDetail(float percent, String title, int color){
        this.sweepDegree = percent * 360 / 100;
        this.title = title;
        this.color = color;
    }


    PieDetail(float startDegree, float endDegree, PieDetail targetPie){
        this.startDegree = startDegree;
        this.endDegree = endDegree;
        targetStartDegree = targetPie.getStartDegree();
        targetEndDegree = targetPie.getEndDegree();
        this.sweepDegree = targetPie.getSweep();
        this.title = targetPie.getTitle();
        this.color = targetPie.getColor();
    }

    PieDetail setTarget(PieDetail targetPie){
        this.targetStartDegree = targetPie.getStartDegree();
        this.targetEndDegree = targetPie.getEndDegree();
        this.title = targetPie.getTitle();
        this.color = targetPie.getColor();
        this.sweepDegree = targetPie.getSweep();
        return this;
    }

    void setDegree(float startDegree, float endDegree){
        this.startDegree = startDegree;
        this.endDegree = endDegree;
    }

    boolean isColorSetted(){return color != 0;}

    boolean isAtRest(){
        return (startDegree==targetStartDegree)&&(endDegree==targetEndDegree);
    }

    void update(){
        this.startDegree = updateSelf(startDegree, targetStartDegree, velocity);
        this.endDegree = updateSelf(endDegree, targetEndDegree, velocity);
        this.sweepDegree = endDegree - startDegree;
    }

    String getPercentStr(){
        float percent = sweepDegree / 360 * 100;
        return String.valueOf((int)percent) + "%";
    }

    public int getColor(){ return color; }

    public String getTitle(){
        return title;
    }

    public float getSweep(){
        return sweepDegree;
    }

    public float getStartDegree(){
        return startDegree;
    }

    public float getEndDegree(){
        return endDegree;
    }

    private float updateSelf(float origin, float target, int velocity){
        if (origin < target) {
            origin += velocity;
        } else if (origin > target){
            origin-= velocity;
        }
        if(Math.abs(target-origin)<velocity){
            origin = target;
        }
        return origin;
    }
}
