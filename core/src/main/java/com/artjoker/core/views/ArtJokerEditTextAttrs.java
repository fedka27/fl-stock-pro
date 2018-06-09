package com.artjoker.core.views;

public class ArtJokerEditTextAttrs {
    private String errorMessageIfLess;
    private String errorMessageIfMore ;
    private int minLengthOfText ;
    private int maxLengthOfText ;
    private String errorMessageForRegExp ;
    private String regExp ;

    public String getErrorMessageIfLess() {
        return errorMessageIfLess;
    }

    public void setErrorMessageIfLess(String errorMessageIfLess) {
        this.errorMessageIfLess = errorMessageIfLess;
    }

    public String getErrorMessageIfMore() {
        return errorMessageIfMore;
    }

    public void setErrorMessageIfMore(String errorMessageIfMore) {
        this.errorMessageIfMore = errorMessageIfMore;
    }

    public int getMinLengthOfText() {
        return minLengthOfText;
    }

    public void setMinLengthOfText(int minLengthOfText) {
        this.minLengthOfText = minLengthOfText;
    }

    public int getMaxLengthOfText() {
        return maxLengthOfText;
    }

    public void setMaxLengthOfText(int maxLengthOfText) {
        this.maxLengthOfText = maxLengthOfText;
    }

    public String getErrorMessageForRegExp() {
        return errorMessageForRegExp;
    }

    public void setErrorMessageForRegExp(String errorMessageForRegExp) {
        this.errorMessageForRegExp = errorMessageForRegExp;
    }

    public String getRegExp() {
        return regExp;
    }

    public void setRegExp(String regExp) {
        this.regExp = regExp;
    }
}
