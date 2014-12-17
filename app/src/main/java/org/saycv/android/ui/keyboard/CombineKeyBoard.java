package org.saycv.android.ui.keyboard;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;

import com.example.saycv.stockartifact.Engine;

import org.saycv.sgs.SgsApplication;
import org.saycv.sgs.SgsEngine;

public class CombineKeyBoard extends GridView {
    private static String[] numKeyName = {
            "600",
            "1",
            "2",
            "3",
            "601",
            "4",
            "5",
            "6",
            "000",
            "7",
            "8",
            "9",
            "002",
            "00",
            "0",
            "退格",
            "300",
            "abc",
            "隐藏",
            "确定",
    };
    String[] numKeyTag = {
    "600",
            "1",
            "2",
            "3",
            "601",
            "4",
            "5",
            "6",
            "000",
            "7",
            "8",
            "9",
            "002",
            "00",
            "0",
            "delete",
            "300",
            "en",
            "hide",
            "ok"
};

    String[] enKeyName = {
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "I",
            "J",
            "K",
            "L",
            "M",
            "N",
            "O",
            "P",
            "Q",
            "R",
            "S",
            "T",
            "U",
            "V",
            "W",
            "X",
            "Y",
            "Z",
            "123",
            "退格",
            "隐藏",
            "确定"
    };

    String[] enKeyTag = {
            "a",
            "b",
            "c",
            "d",
            "e",
            "f",
            "g",
            "h",
            "i",
            "j",
            "k",
            "l",
            "m",
            "n",
            "o",
            "p",
            "q",
            "r",
            "s",
            "t",
            "u",
            "v",
            "w",
            "x",
            "y",
            "z",
            "num",
            "delete",
            "hide",
            "ok"
    };
  public static final byte TYPE_EN = 0;
  public static final byte TYPE_NUM = 1;

  private final View.OnClickListener clickHandler = null;
  private final Handler handler = null;//new CombineKeyBoardHandler(this);
  private EditText inputArea;
  private int maxLength = -1;
  private Handler queryHandler;



  public CombineKeyBoard(Context paramContext)
  {
    super(paramContext);
    init();
  }

  public CombineKeyBoard(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  private void init()
  {
    setFocusable(false);
    setFocusableInTouchMode(false);
    setMeasuredDimension(-1, -2);
    showNumKeyboard();
  }

  private void show()
  {
    setVisibility(VISIBLE);
  }

  private void showEnKeyboard()
  {
    setNumColumns(6);
    setAdapter(new KeyBoardAdapter(getContext(), enKeyName, enKeyTag, this.clickHandler));
  }

  private void showNumKeyboard()
  {
    setNumColumns(4);
    setAdapter(new KeyBoardAdapter(getContext(), numKeyName, numKeyTag, this.clickHandler));
  }

  public void hide()
  {
    setVisibility(INVISIBLE);
  }

  protected void onDetachedFromWindow()
  {
    this.inputArea = null;
    this.queryHandler = null;
    super.onDetachedFromWindow();
  }

  public void setInputArea(EditText paramEditText)
  {
    this.inputArea = paramEditText;
    paramEditText.setCursorVisible(true);
    paramEditText.setFocusable(true);
    paramEditText.setFocusableInTouchMode(true);
    paramEditText.requestFocus();
    paramEditText.setInputType(0);
    paramEditText.setLongClickable(false);
    paramEditText.setOnClickListener(null);
  }

  public void setKeyBoardType(byte paramByte)
  {
    if (paramByte == 0)
      showEnKeyboard();

      if (paramByte == 1)
        showNumKeyboard();
  }

  public void setMaxLength(int paramInt)
  {
    this.maxLength = paramInt;
  }

  public void setQueryHandler(Handler paramHandler)
  {
    this.queryHandler = paramHandler;
  }
}