package com.stylingandroid.textinputlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by jin on 2017/6/16.
 */

public class PasswordEditText extends LinearLayout {
    private int num;
    private int backDrawableRes;
    private int width;
    private int space;
    private LinearLayout.LayoutParams params;
    private EditText[] texts;
    private StringBuilder stringBuilder;
    private Context context;

    public PasswordEditText(Context context) {
        super(context);
        init(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context=context;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText);
            backDrawableRes = typedArray.getResourceId(R.styleable.PasswordEditText_backDrawable, -1);
            width = typedArray.getDimensionPixelSize(R.styleable.PasswordEditText_squareWidth, getResources().getDimensionPixelSize(R.dimen.default_width));
            space = typedArray.getDimensionPixelSize(R.styleable.PasswordEditText_squareWidth, getResources().getDimensionPixelSize(R.dimen.default_space));
            num = typedArray.getInteger(R.styleable.PasswordEditText_num, 4);
            typedArray.recycle();

        }
        initView(context);
    }

    private void initView(Context context) {
        EditText tempEditText = null;
        texts = new EditText[num];
        params = new LayoutParams(width, width);
        for (int i = 0; i < num; i++) {
            final int finalJ = i;
            if (i > 0 && i < num - 1) {
                params.leftMargin = space;
            }
            tempEditText = new EditText(context);
            tempEditText.setLayoutParams(params);
            // tempEditText.setCursorVisible(false);
            tempEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)}); //最大输入长度
            tempEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            if (backDrawableRes != -1) {
                tempEditText.setBackgroundResource(backDrawableRes);
            } else {
                tempEditText.setBackgroundResource(R.drawable.line);
            }

            tempEditText.setGravity(Gravity.CENTER);
            texts[i] = tempEditText;
            texts[finalJ].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.length() == 1 && finalJ < num - 1 && texts[finalJ + 1] != null) {
                        texts[finalJ + 1].setFocusable(true);
                        texts[finalJ + 1].setFocusableInTouchMode(true);
                        texts[finalJ + 1].requestFocus();
                    } else if (editable.length() == 0 && finalJ > 0 && texts[finalJ - 1] != null) {
                        texts[finalJ - 1].setFocusable(true);
                        texts[finalJ - 1].setFocusableInTouchMode(true);
                        texts[finalJ - 1].requestFocus();
                    }
                }
            });

            texts[finalJ].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (texts[finalJ].getText().length() == 0 && finalJ > 0 && texts[finalJ - 1] != null) {
                            texts[finalJ - 1].setFocusable(true);
                            texts[finalJ - 1].setFocusableInTouchMode(true);
                            texts[finalJ - 1].requestFocus();
                            texts[finalJ - 1].setText("");
                        }
                    }
                    return false;
                }
            });
            if (finalJ == 0) {
                texts[finalJ].setFocusable(true);
                texts[finalJ].setFocusableInTouchMode(true);
                texts[finalJ].requestFocus();
            }
            addView(texts[finalJ]);
        }
    }

    public String getText() {
        joinString();
        return stringBuilder.toString();
    }

    private void joinString() {
        if (stringBuilder == null)
            stringBuilder = new StringBuilder();
        else {
            stringBuilder.setLength(0);
        }
        for (int i = 0; i < num; i++) {
            stringBuilder.append(texts[i].getText().toString());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            joinString();
            if (stringBuilder.length() == 0) {
                texts[0].setFocusable(true);
                texts[0].setFocusableInTouchMode(true);
                texts[0].requestFocus();
                KeyBoardUtil.openKeybord(texts[0],context );
            }else if (stringBuilder.length() == num) {
                texts[num-1].setFocusable(true);
                texts[num-1].setFocusableInTouchMode(true);
                texts[num-1].requestFocus();
                KeyBoardUtil.openKeybord(texts[num-1],context );
            }else{
                texts[stringBuilder.length()].setFocusable(true);
                texts[stringBuilder.length()].setFocusableInTouchMode(true);
                texts[stringBuilder.length()].requestFocus();
                KeyBoardUtil.openKeybord(texts[stringBuilder.length()],context );
            }
        }
        return super.onTouchEvent(event);
    }
}
