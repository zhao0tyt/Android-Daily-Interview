package com.zzq.democollection.ipc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zzq.democollection.R;

public class IpcFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String BUNDLE = "bundle";
    public static final String SHARE_FILE = "share_file";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public IpcFragment() {
        // Required empty public constructor
    }

    public static IpcFragment newInstance() {
        IpcFragment fragment = new IpcFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ipc, container, false);
        Button btBundle = view.findViewById(R.id.ipc_bundle);
        Button btShareFile = view.findViewById(R.id.ipc_share_file);
        btBundle.setOnClickListener(this);
        btShareFile.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), FirstActivity.class);
        String extra = null;
        switch (view.getId()) {
            case R.id.ipc_bundle:
                extra = BUNDLE;
                break;
            case R.id.ipc_share_file:
                extra = SHARE_FILE;
                break;
        }
        intent.putExtra("ipc", extra);
        startActivity(intent);

    }
}
