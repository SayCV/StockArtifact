package org.saycv.android.ui;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.example.saycv.stockartifact.R;

public class PageIndicator extends LinearLayout
{
    private int currentPage = 0;
    private int pageCount = 3;

    public PageIndicator(Context paramContext)
    {
        super(paramContext);
        init(paramContext);
    }

    public PageIndicator(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        init(paramContext);
    }

    private void addPage(int paramInt)
    {
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
        int i = (int)TypedValue.applyDimension(2, 5.0F, getResources().getDisplayMetrics());
        localLayoutParams.leftMargin = i;
        localLayoutParams.rightMargin = i;
        for (int j = 0; j < paramInt; j++)
        {
            ImageView localImageView = new ImageView(getContext());
            localImageView.setImageResource(R.drawable.graydot);
            addView(localImageView, localLayoutParams);
        }
    }

    private void init(Context paramContext)
    {
        setMeasuredDimension(-2, -2);
        setGravity(17);
        addPage(this.pageCount);
        setCurrentPage(this.currentPage);
    }

    public void setCurrentPage(int paramInt)
    {
        this.currentPage = paramInt;
        int i = getChildCount();
        for(int j = 0; j < i; j++) {
            if (j == paramInt) ((ImageView)getChildAt(j)).setImageResource(R.drawable.whitedot);
            else ((ImageView)getChildAt(j)).setImageResource(R.drawable.graydot);
        }
    }

    public void setPageCount(int paramInt) {
        if (this.pageCount == paramInt)
            return;
        this.pageCount = paramInt;
        int i = getChildCount();
        if (paramInt < i) {
            removeViews(0, i - paramInt);

            setCurrentPage(this.currentPage);
        }else if (paramInt > i)
            addPage(paramInt - i);

    }
}