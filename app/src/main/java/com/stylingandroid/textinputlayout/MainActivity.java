package com.stylingandroid.textinputlayout;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends Activity implements TextWatcher {

    private static final int MIN_TEXT_LENGTH = 4;
    private static final String EMPTY_STRING = "";

    private TextInputLayout textInputLayout;
    private EditText editText;
    private TextInputLayout textInputLayout1;
    private EditText editText1;

    private LinearLayout linearLayout;
    private EditText[] texts;
    private int childCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout);
        editText = (EditText) findViewById(R.id.edit_text);

        textInputLayout1 = (TextInputLayout) findViewById(R.id.text_input_layout1);
        editText1 = (EditText) findViewById(R.id.edit_text1);

        textInputLayout.setHint(getString(R.string.hint));
        textInputLayout1.setHint("密码");
        editText.setOnEditorActionListener(ActionListener.newInstance(this));

        linearLayout = (LinearLayout) findViewById(R.id.ll);
        childCount = linearLayout.getChildCount();
        texts = new EditText[childCount];
        for (int j = 0; j < childCount; j++) {
            texts[j] = ((EditText) linearLayout.getChildAt(j));
            final int finalJ = j;
            texts[j].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    if (editable.length() == 1  && finalJ < childCount - 1 &&texts[finalJ + 1] != null) {
                        texts[finalJ + 1].setFocusable(true);
                        texts[finalJ + 1].setFocusableInTouchMode(true);
                        texts[finalJ + 1].requestFocus();
                    } else if (editable.length() == 0  && finalJ > 0 && texts[finalJ - 1] != null) {
                        texts[finalJ - 1].setFocusable(true);
                        texts[finalJ - 1].setFocusableInTouchMode(true);
                        texts[finalJ - 1].requestFocus();
                    }
                }
            });
            texts[finalJ].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                    if (keyCode==KeyEvent.KEYCODE_DEL){
                        if ( texts[finalJ].getText().length() == 0  && finalJ > 0 && texts[finalJ - 1] != null) {
                            texts[finalJ - 1].setFocusable(true);
                            texts[finalJ - 1].setFocusableInTouchMode(true);
                            texts[finalJ - 1].requestFocus();
                            texts[finalJ-1].setText("");
                        }
                    }
                    return false;
                }
            });
        }

    }

    private boolean shouldShowError() {
        int textLength = editText.getText().length();
        return textLength > 0 && textLength < MIN_TEXT_LENGTH;
    }

    private void showError() {
        textInputLayout.setError(" ");
        textInputLayout.setErrorEnabled(true);
        editText.setAnimation(AnimationUtils.loadAnimation(this, R.anim.sharkle));

    }

    private void hideError() {
        textInputLayout.setError(EMPTY_STRING);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void btn(View view) {
        textInputLayout.setError(" ");
        textInputLayout.setErrorEnabled(true);
        editText.setAnimation(AnimationUtils.loadAnimation(this, R.anim.sharkle));
    }

    private static final class ActionListener implements TextView.OnEditorActionListener {
        private final WeakReference<MainActivity> mainActivityWeakReference;

        private ActionListener(WeakReference<MainActivity> mainActivityWeakReference) {
            this.mainActivityWeakReference = mainActivityWeakReference;
        }

        public static ActionListener newInstance(MainActivity mainActivity) {
            WeakReference<MainActivity> mainActivityWeakReference = new WeakReference<>(mainActivity);
            return new ActionListener(mainActivityWeakReference);
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (mainActivity != null) {
                if (actionId == EditorInfo.IME_ACTION_GO && mainActivity.shouldShowError()) {
                    mainActivity.showError();
                } else {
                    mainActivity.hideError();
                }
            }
            return true;
        }
    }
}
