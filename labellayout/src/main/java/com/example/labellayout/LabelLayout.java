package com.example.labellayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LabelLayout extends LinearLayout {

    private static final String TAG = "LabelLayout";
    private int SCREEN_WIDTH;
    private View view;
    private Context context;
    private LinearLayout label_layout;
    private View indicator;
    private List<String> label;
    private int currentTab,lastTab;
    private int posX1,posX2;
    private int labelColor,tagColor;
    private float tagSize;
    private float indicatorWidth,indicatorHeight;
    private float labelHeight;
    private int indicatorSpeed;

    public LabelLayout(Context context) {
        super(context);
        this.context=context;
        initView();
    }
    public LabelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        getValue(context,attrs);
        initView();
    }
    public LabelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        getValue(context,attrs);
        initView();
    }

    private void getValue(Context context, AttributeSet attrs){
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.label);
        indicatorWidth=typedArray.getDimension(R.styleable.label_ind_w,dp2px(15));
        indicatorHeight=typedArray.getDimension(R.styleable.label_ind_h,dp2px(5));
        labelHeight=typedArray.getDimension(R.styleable.label_label_h,dp2px(18));
        indicatorSpeed=typedArray.getInt(R.styleable.label_ind_spd,300);
        labelColor=typedArray.getColor(R.styleable.label_label_color,Color.GRAY);
        tagColor=typedArray.getColor(R.styleable.label_label_color,Color.WHITE);
        tagSize=typedArray.getDimension(R.styleable.label_label_h,dp2px(7));
        typedArray.recycle();
    }

    private void initView(){

        view=LayoutInflater.from(context).inflate(R.layout.label_layout,this,true);
        label_layout=view.findViewById(R.id.label);
        indicator=view.findViewById(R.id.indicator);
        label=new ArrayList<>();
        label.add("");
        currentTab=lastTab=0;
        posX1=posX2=0;
        SCREEN_WIDTH = getScreenWidth();
    }

    public LabelLayout addLabel(String... args){
        label.clear();
        label.addAll(Arrays.asList(args));
        return this;
    }
    public LabelLayout addLabel(List<String> label){
        this.label.clear();
        this.label=label;
        return this;
    }

    public LabelLayout setLabelColor(int labelColor){
        this.labelColor=labelColor;
        return this;
    }

    public LabelLayout setTagColor(int tagColor){
        this.tagColor=tagColor;
        return this;
    }

    public LabelLayout setTagSize(float tagSize){
        this.tagSize=tagSize;
        return this;
    }

    public LabelLayout setIndicatorWidth(float indicatorWidth){
        this.indicatorWidth=indicatorWidth;
        return this;
    }

    public LabelLayout setIndicatorHeight(float indicatorHeight){
        this.indicatorHeight=indicatorHeight;
        return this;
    }

    public LabelLayout setLabelHeight(float labelHeight){
        this.labelHeight=labelHeight;
        return this;
    }

    public LabelLayout setIndicatorSpeed(int indicatorSpeed){
        this.indicatorSpeed=indicatorSpeed;
        return this;
    }


    public void create(){
        view.setBackgroundColor(labelColor);
        indicator.setBackgroundColor(Color.WHITE);
        addTab();
        indicatorInitialization();
    }

    private void indicatorInitialization(){
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams((int)indicatorWidth,(int)indicatorHeight);
        params.addRule(RelativeLayout.BELOW,R.id.label);
        params.setMargins((int)((SCREEN_WIDTH/label.size()/2)-(indicatorWidth/2)),-(int)indicatorHeight,0,0);
        indicator.setLayoutParams(params);
    }

    private void addTab(){

        for(int i=0;i<label.size();i++) {
            TextView textView = new TextView(context);
            textView.setText(label.get(i));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(tagSize);
            textView.setTextColor(tagColor);
            textView.setTag(i);

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            params.setMargins(0, (int)labelHeight/2, 0, (int)(labelHeight/2));
            label_layout.addView(textView, params);

            label_layout.getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    if(pos!=currentTab){
                        lastTab=currentTab;
                        currentTab=pos;
                        calOffset(currentTab,lastTab);
                    }
                }
            });
        }
    }

    private void calOffset(int currentTab,int lastTab){

        posX1 = lastTab * SCREEN_WIDTH/(label.size());
        posX2 = currentTab * SCREEN_WIDTH/(label.size());

        startAnimator(posX1,posX2);
    }

    private void startAnimator(int posX1,int posX2) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(posX1,posX2);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams((int)indicatorWidth,(int)indicatorHeight);
                params.addRule(RelativeLayout.BELOW,R.id.label);
                params.setMargins((int)((SCREEN_WIDTH/label.size()/2)-(indicatorWidth/2))+value,-(int)indicatorHeight,0,0);
                indicator.setLayoutParams(params);
            }
        });
        valueAnimator.setDuration(indicatorSpeed);
        valueAnimator.start();
    }

    private int dp2px(float dipValue) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }

    private int getScreenWidth(){
        WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
//        int height = dm.heightPixels;
        return width;
    }
}
