package com.coderbunker.android.sunshine.legacy;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.view.FragmentGridPagerAdapter;

import com.coderbunker.android.sunshine.WeatherPageFragment;
import com.coderbunker.android.sunshine.library.model.WeatherData;

import java.util.ArrayList;
import java.util.List;

public class WeatherGridAdapter extends FragmentGridPagerAdapter {

    private List<Row> data = new ArrayList<>();

    public WeatherGridAdapter(FragmentManager fm) {
        super(fm);
    }

    public void update(List<WeatherData> data) {
        Fragment[] dataAsSet = new Fragment[data.size()];
        for (int idx = 0; idx < data.size(); idx++) {
            dataAsSet[idx] = WeatherPageFragment.newInstance(data.get(idx));
        }

        this.data.clear();
        this.data.set(0, new Row(dataAsSet));
        notifyDataSetChanged();
    }

    @Override
    public Fragment getFragment(int row, int col) {
        return data.get(row).getColumn(col);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount(int rowPosition) {
        return data.get(rowPosition).getColumnCount();
    }

    private class Row {
        final List<Fragment> columns = new ArrayList<Fragment>();

        public Row(Fragment... fragments) {
            for (Fragment f : fragments) {
                add(f);
            }
        }

        public void add(Fragment f) {
            columns.add(f);
        }

        Fragment getColumn(int i) {
            return columns.get(i);
        }

        public int getColumnCount() {
            return columns.size();
        }
    }
}
