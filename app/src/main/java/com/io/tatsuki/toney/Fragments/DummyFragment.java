package com.io.tatsuki.toney.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.io.tatsuki.toney.R;

/**
 * Created by TatsukiIshijima on 2017/07/06.
 */

public class DummyFragment extends Fragment {

    private static final String TAG = DummyFragment.class.getSimpleName();

    public static DummyFragment newInstance() {
        DummyFragment dummyFragment = new DummyFragment();
        return dummyFragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        View dummyView = inflater.inflate(R.layout.fragment_dummy, viewGroup, false);
        return dummyView;
    }
}
