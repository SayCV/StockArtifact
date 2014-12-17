package org.saycv.android.ui.keyboard;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import com.example.saycv.stockartifact.R;

public class KeyBoardAdapter extends BaseAdapter {
    private final View.OnClickListener clickHandler;
    private final String[] keyNameArray;
    private final String[] keyTagArray;
    private Context mContext;

    public KeyBoardAdapter(Context paramContext, String[] paramArrayOfString1, String[] paramArrayOfString2, View.OnClickListener paramOnClickListener) {
        this.mContext = paramContext;
        this.keyNameArray = paramArrayOfString1;
        this.keyTagArray = paramArrayOfString2;
        this.clickHandler = paramOnClickListener;
    }

    public int getCount() {
        return this.keyNameArray.length;
    }

    public Object getItem(int paramInt) {
        return this.keyNameArray[paramInt];
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        Button localButton;
        if (paramView == null) {
            Resources localResources = this.mContext.getResources();
            localButton = new Button(this.mContext);
            localButton.setHeight(localResources.getDimensionPixelSize(R.dimen.keyboard_height));
            localButton.setBackgroundResource(R.drawable.keyboard_key);
            localButton.setTextSize(localResources.getDimension(R.dimen.keyboard_textsize));
            localButton.setTextColor(-1);
            localButton.setOnClickListener(this.clickHandler);
            return localButton;
        }
        localButton = (Button) paramView;
        localButton.setText(this.keyNameArray[paramInt]);
        localButton.setTag(this.keyTagArray[paramInt]);
        return localButton;
    }
}
