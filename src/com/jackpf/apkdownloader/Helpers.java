package com.jackpf.apkdownloader;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class Helpers
{
    /**
     * Context menu action mode
     */
    public static ActionMode mActionMode;
    
    /**
     * Add a long click triggered context menu to a listview
     * 
     * @param list
     * @param m
     * @param callback
     */
    public static void addContextMenu(final ListView list, final int m, final ContextMenuCallback callback)
    {
        final ActionMode.Callback mActionModeCallback = new ActionMode.Callback()
        {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(m, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false; // Return false if nothing is done
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (callback.onActionItemClicked(mode, item)) {
                    mode.finish();
                }
                
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mActionMode = null;
                list.setItemChecked(-1, true);
            }
        };
        
        list.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mActionMode != null) {
                    return false;
                }

                mActionMode = callback.startActionMode(mActionModeCallback);
                list.setItemChecked(position, true);
                return true;
            }
        });
    }
    
    /**
     * Context menu callback interface
     */
    public interface ContextMenuCallback
    {
        public ActionMode startActionMode(ActionMode.Callback callback);
        public boolean onActionItemClicked(ActionMode mode, MenuItem item);
    }
}
