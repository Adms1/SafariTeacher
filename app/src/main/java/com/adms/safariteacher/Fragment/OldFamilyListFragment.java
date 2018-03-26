package com.adms.safariteacher.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adms.safariteacher.Activities.DashBoardActivity;
import com.adms.safariteacher.Adapter.SelectStudentListAdapter;
import com.adms.safariteacher.Interface.onViewClick;
import com.adms.safariteacher.R;
import com.adms.safariteacher.databinding.FragmentOldFamilyListBinding;

import java.util.ArrayList;


public class OldFamilyListFragment extends Fragment {

    private FragmentOldFamilyListBinding oldFamilyListBinding;
    private View rootView;
    private Context mContext;
    private Fragment fragment = null;
    private FragmentManager fragmentManager = null;
    SelectStudentListAdapter selectStudentListAdapter;
    ArrayList<String> arrayList;

    public OldFamilyListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        oldFamilyListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_old_family_list, container, false);

        rootView = oldFamilyListBinding.getRoot();
        mContext = getActivity().getApplicationContext();
        ((DashBoardActivity) getActivity()).setActionBar(2, "true");
        initViews();
        setListners();

        return rootView;
    }

    public void initViews() {


        arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayList.add(String.valueOf(i));
        }
        Log.d("arrayList", "" + arrayList.size());
        selectStudentListAdapter = new SelectStudentListAdapter(mContext, arrayList, new onViewClick() {
            @Override
            public void getViewClick() {
                Fragment fragment = new AddFamilyFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("session","11");
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        oldFamilyListBinding.selectStudentRcView.setLayoutManager(mLayoutManager);
        oldFamilyListBinding.selectStudentRcView.setItemAnimator(new DefaultItemAnimator());
        oldFamilyListBinding.selectStudentRcView.setAdapter(selectStudentListAdapter);
    }

    public void setListners() {

        oldFamilyListBinding.addFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AddFamilyFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("session","10");
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }
}

