package com.xej.xhjy.ui.view;

import android.content.Context;
import android.view.View;

public interface ViewCreator<T> {

    View createView(Context context, int position);

    void updateUI(Context context, View view, int position, T t);
}