package com.kck4156.uisample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ChatOtherFragment extends Fragment {
    private static final String ARG_MESSAGE = "message";

    private String message;

    public ChatOtherFragment() {
        // Required empty public constructor
    }

    public static ChatOtherFragment newInstance(String param1) {
        ChatOtherFragment fragment = new ChatOtherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString(ARG_MESSAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_other, container, false);
        TextView tvMessage = view.findViewById(R.id.tvMessageOther);
        tvMessage.setText(message);
        tvMessage.setTextSize(ChatHistoryActivity.fontSize);
        return view;
    }
}
