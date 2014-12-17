package org.saycv.android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.saycv.stockartifact.R;

public class PointTips extends LinearLayout {
    private int current;
    private int pointHeight;
    private int pointMargin;
    private int pointSize;
    private View[] pointViews;
    private int resPointNormal;
    private int resPointSelected;

    public PointTips(Context paramContext) {
        super(paramContext);
        init();
    }

    public PointTips(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    private void init() {
        this.resPointNormal = R.drawable.point_normal;
        this.resPointSelected = R.drawable.point_selected;
        this.pointSize = 0;
        this.current = 0;
        this.pointHeight = 18;
        this.pointMargin = 15;
    }

    public void setCurrent(int paramInt) {
        if ((paramInt > this.pointSize) || (paramInt < 0)) return;

        this.pointViews[this.current].setBackgroundResource(this.resPointNormal);
        this.current = paramInt;
        this.pointViews[this.current].setBackgroundResource(this.resPointSelected);
    }

    public void setPointHeight(int paramInt) {
        this.pointHeight = paramInt;
    }

    public void setPointMargin(int paramInt) {
        this.pointMargin = paramInt;
    }

    public void setPointSize(int paramInt) {
        if (paramInt < 0) return;
        this.pointSize = paramInt;
        removeAllViews();
        this.pointViews = new View[paramInt];
        for (int i = 0; i < paramInt; i++) {
            View localView = new View(getContext());
            localView.setBackgroundResource(this.resPointNormal);
            LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(this.pointHeight, this.pointHeight);
            localLayoutParams.leftMargin = this.pointMargin;
            localView.setLayoutParams(localLayoutParams);
            this.pointViews[i] = localView;
            addView(localView);
        }
    }

    public void setResPointNormal(int paramInt) {
        this.resPointNormal = paramInt;
    }

    public void setResPointSelected(int paramInt) {
        this.resPointSelected = paramInt;
    }
}