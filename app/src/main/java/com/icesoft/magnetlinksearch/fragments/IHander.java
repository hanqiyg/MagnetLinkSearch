package com.icesoft.magnetlinksearch.fragments;

public interface IHander {
    void setBackPressListener(OnBackPressedListener listener);
    void showFragment(String fragmentTag);
}
