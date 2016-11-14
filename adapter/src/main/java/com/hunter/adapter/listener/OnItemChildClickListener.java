package com.hunter.adapter.listener;

import android.view.View;

import com.hunter.adapter.BaseQuickAdapter;

public abstract class OnItemChildClickListener extends SimpleClickListener {

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        SimpleOnItemChildClick(adapter, view, position);
    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    public abstract void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int position);

}
