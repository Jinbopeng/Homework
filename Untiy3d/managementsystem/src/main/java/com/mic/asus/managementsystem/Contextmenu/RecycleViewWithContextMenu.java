package com.mic.asus.managementsystem.Contextmenu;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;

public class RecycleViewWithContextMenu extends RecyclerView {

    RecycleViewContextInf inf=new RecycleViewContextInf();
    public RecycleViewWithContextMenu(Context context) {
        super(context);
    }

    public RecycleViewWithContextMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecycleViewWithContextMenu(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean showContextMenuForChild(View originalView, float x, float y) {
        return super.showContextMenuForChild(originalView, x, y);
    }





    public static class RecycleViewContextInf implements ContextMenu.ContextMenuInfo{
        public View Targetview;
        public int position;
        public int id;

    }
}
