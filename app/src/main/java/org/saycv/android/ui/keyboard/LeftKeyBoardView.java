package org.saycv.android.ui.keyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.widget.EditText;

public class LeftKeyBoardView extends KeyboardView {
    private Keyboard leftSymbolsKeyboard;
    private EditText mEditText;
    private int maxLength;

    public LeftKeyBoardView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public LeftKeyBoardView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    private void init() {
        if (getResources().getConfiguration().orientation == 2) return;
        /*for (this.leftSymbolsKeyboard = new Keyboard(getContext(), R.xml.left_symbos_land); ; this.leftSymbolsKeyboard = new Keyboard(getContext(), R.xml.left_symbos)) {
            setKeyboard(this.leftSymbolsKeyboard);
            setOnKeyboardActionListener(new LeftKeyBoardView .1 (this));

        }*/
    }

    public void setEditText(EditText paramEditText) {
        this.mEditText = paramEditText;
    }

    public void setMaxLength(int paramInt) {
        this.maxLength = paramInt;
    }
}
