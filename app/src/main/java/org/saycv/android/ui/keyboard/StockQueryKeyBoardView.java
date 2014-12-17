/*
package org.saycv.android.ui.keyboard;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

public class StockQueryKeyBoardView extends KeyboardView
{
  private boolean bShowing = false;
  private boolean hasSetUp = false;
  private LeftKeyBoardView leftKeyBoardView;
  private EditText mEditText;
  private int maxLength;
  private Handler queryHandler;
  private Keyboard stockKeyboard;
  private Keyboard symbolsKeyboard;

  public StockQueryKeyBoardView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  public StockQueryKeyBoardView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }

  private void init()
  {
    if (getResources().getConfiguration().orientation == 2)
      this.stockKeyboard = new Keyboard(getContext(), R.xml.stock_land);
    for (this.symbolsKeyboard = new Keyboard(getContext(), R.xml.symbols_land); ; this.symbolsKeyboard = new Keyboard(getContext(), R.xml.symbols))
    {
      setKeyboard(this.symbolsKeyboard);
      setOnKeyboardActionListener(new StockQueryKeyBoardView.1(this));
      return;
      this.stockKeyboard = new Keyboard(getContext(), R.xml.stock);
    }
  }

  public void hide()
  {
    this.bShowing = false;
    if ((getVisibility() != 0) && (this.leftKeyBoardView.getVisibility() != 0));
    while (true)
    {
      return;
      TranslateAnimation localTranslateAnimation = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 0.0F, 1, 1.0F);
      localTranslateAnimation.setDuration(100L);
      localTranslateAnimation.setInterpolator(new DecelerateInterpolator());
      localTranslateAnimation.setAnimationListener(new StockQueryKeyBoardView.5(this));
      try
      {
        ((ViewGroup)getParent()).startAnimation(localTranslateAnimation);
      }
      catch (Exception localException)
      {
        setVisibility(8);
        this.leftKeyBoardView.setVisibility(8);
      }
    }
  }

  public boolean isShowing()
  {
    return this.bShowing;
  }

  public void onDetachedFromWindow()
  {
    this.mEditText = null;
    this.queryHandler = null;
    super.onDetachedFromWindow();
  }

  public void setEditText(EditText paramEditText)
  {
    this.mEditText = paramEditText;
    this.mEditText.setLongClickable(false);
    this.mEditText.setCursorVisible(true);
    paramEditText.setOnTouchListener(new StockQueryKeyBoardView.2(this));
    ((Activity)this.mEditText.getContext()).getWindow().setSoftInputMode(3);
    this.mEditText.postDelayed(new StockQueryKeyBoardView.3(this), 500L);
    this.mEditText.setOnEditorActionListener(new StockQueryKeyBoardView.4(this));
  }

  public void setLeftKeyBoardView(LeftKeyBoardView paramLeftKeyBoardView)
  {
    this.leftKeyBoardView = paramLeftKeyBoardView;
    this.leftKeyBoardView.setEditText(this.mEditText);
    this.leftKeyBoardView.setMaxLength(this.maxLength);
  }

  public void setMaxLength(int paramInt)
  {
    this.maxLength = paramInt;
  }

  public void setQueryHandler(Handler paramHandler)
  {
    this.queryHandler = paramHandler;
  }

  public void show()
  {
    this.bShowing = true;
    if (getVisibility() == 0);
    while (true)
    {
      return;
      TranslateAnimation localTranslateAnimation = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 1.0F, 1, 0.0F);
      localTranslateAnimation.setDuration(100L);
      localTranslateAnimation.setInterpolator(new DecelerateInterpolator());
      localTranslateAnimation.setAnimationListener(new StockQueryKeyBoardView.6(this));
      setVisibility(4);
      if (getKeyboard() == this.stockKeyboard)
        break label126;
      this.leftKeyBoardView.setVisibility(4);
      try
      {
        label83: ((ViewGroup)getParent()).startAnimation(localTranslateAnimation);
      }
      catch (Exception localException)
      {
        if (getKeyboard() != this.stockKeyboard)
          break label138;
      }
    }
    this.leftKeyBoardView.setVisibility(8);
    while (true)
    {
      setVisibility(0);
      break;
      label126: this.leftKeyBoardView.setVisibility(8);
      break label83;
      label138: this.leftKeyBoardView.setVisibility(0);
    }
  }
}

*/

