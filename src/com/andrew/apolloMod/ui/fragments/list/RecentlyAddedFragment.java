
package com.andrew.apolloMod.ui.fragments.list;

import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.MediaColumns;
import com.andrew.apolloMod.R;
import com.andrew.apolloMod.helpers.AddIdCursorLoader;
import com.andrew.apolloMod.helpers.utils.MusicUtils;
import com.andrew.apolloMod.ui.adapters.list.RecentlyAddedAdapter;
import com.andrew.apolloMod.ui.fragments.base.ListViewFragment;
import static com.andrew.apolloMod.Constants.NUMWEEKS;
/**
 * 
 * @author Administrator
 *	最近添加界面
 */
public class RecentlyAddedFragment extends ListViewFragment{
	//接口:准备数据
    public void setupFragmentData(){
        mAdapter = new RecentlyAddedAdapter(getActivity(), R.layout.listview_items,
                null, new String[] {}, new int[] {}, 0);
        //构造查询语句,父类中回调,给cursorloader使用
    	mProjection = new String[] {
                BaseColumns._ID, MediaColumns.TITLE, AudioColumns.ALBUM, AudioColumns.ARTIST
        };
        StringBuilder where = new StringBuilder();
        int X = MusicUtils.getIntPref(getActivity(), NUMWEEKS, 5) * 3600 * 24 * 7;
        where.append(MediaColumns.TITLE + " != ''");
        where.append(" AND " + AudioColumns.IS_MUSIC + "=1");
        where.append(" AND " + MediaColumns.DATE_ADDED + ">"
                + (System.currentTimeMillis() / 1000 - X));
        mWhere = where.toString();
        mSortOrder = MediaColumns.DATE_ADDED + " DESC";
        //查询音乐的uri?
        mUri = Audio.Media.EXTERNAL_CONTENT_URI;
        mTitleColumn = MediaColumns.TITLE;       
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {    
        return new AddIdCursorLoader(getActivity(), mUri, mProjection, mWhere, null, mSortOrder);
    }
}
