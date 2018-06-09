package com.artjoker.core.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Patterns;
import android.widget.EditText;

import com.artjoker.core.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ArtJokerEditText extends EditText implements TextWatcher {


    private final static int MAX_LENGHT_DEFAULT = 100;
    private final static int MIN_LENGHT_DEFAULT = 0;
    private ArtJokerEditTextAttrs editTextAttrs;
    private boolean isValidateEmail = false;
    private boolean isValidatePhone = false;

    public ArtJokerEditText(Context context) {
        super(context);
    }

    public ArtJokerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        getEditTextAttrs(context, attrs);
        setCustomFont(context, attrs);
    }

    public ArtJokerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getEditTextAttrs(context, attrs);
        setCustomFont(context, attrs);
    }

    protected void getEditTextAttrs(Context ctx, AttributeSet attrs) {
        addTextChangedListener(this);
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.ArtJokerEditText);

        editTextAttrs = new ArtJokerEditTextAttrs();
        editTextAttrs.setErrorMessageIfLess(a.getString(R.styleable.ArtJokerEditText_errorMessageIfLenghtLessThen));
        editTextAttrs.setErrorMessageIfMore(a.getString(R.styleable.ArtJokerEditText_errorMessageIfLenghtMoreThen));
        editTextAttrs.setMaxLengthOfText(a.getInt(R.styleable.ArtJokerEditText_showErrorMessageIfLenghtMoreThen, MAX_LENGHT_DEFAULT));
        editTextAttrs.setMinLengthOfText(a.getInt(R.styleable.ArtJokerEditText_showErrorMessageIfLenghtLessThen, MIN_LENGHT_DEFAULT));
        editTextAttrs.setErrorMessageForRegExp(a.getString(R.styleable.ArtJokerEditText_errorMessageForRegExp));
        editTextAttrs.setRegExp(a.getString(R.styleable.ArtJokerEditText_showErrorMessageForRegExp));

        a.recycle();


    }

    public boolean isValid() {

        return isValid(this.getText().toString());
    }

    private boolean isValid(String text) {
        String errorMess = null;

        if (text.length() > editTextAttrs.getMaxLengthOfText()) {
            errorMess = editTextAttrs.getErrorMessageIfMore();

        }
        if (text.length() < editTextAttrs.getMinLengthOfText()) {
            errorMess = editTextAttrs.getErrorMessageIfLess();

        }
        if (editTextAttrs.getRegExp() != null) {
            if (!editTextAttrs.getRegExp().equals("")) {
                Pattern pattern = Pattern.compile(editTextAttrs.getRegExp());
                Matcher matcher = pattern.matcher(text);
                if (!matcher.matches()) {
                    errorMess = editTextAttrs.getErrorMessageForRegExp();

                }
            }
        }
        if (isValidateEmail) {
            if (!Patterns.EMAIL_ADDRESS.matcher(text).matches())
                errorMess = getResources().getString(R.string.email_error);
        }
        if (isValidatePhone) {
            if (!Patterns.PHONE.matcher(text).matches())
                errorMess = getResources().getString(R.string.phone_error);
        }
        setError(errorMess);
        if (errorMess != null)
            return false;
        else
            return true;
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.ArtJokerEditText);
        String customFont = a.getString(R.styleable.ArtJokerEditText_customFont);
        ArtJokerViewsUtils.setCustomFont(ctx, this, customFont);
        a.recycle();
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (editTextAttrs != null)
            isValid(s.toString());

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void setValidateEmail(boolean isNeedToValidate) {
        isValidateEmail = isNeedToValidate;
    }

    public void setValidatePhone(boolean isNeedToValidate) {
        isValidatePhone = isNeedToValidate;
    }


    public void invalidateError(String text) {
        if ("".equals(text)) setError(null);
    }
}
