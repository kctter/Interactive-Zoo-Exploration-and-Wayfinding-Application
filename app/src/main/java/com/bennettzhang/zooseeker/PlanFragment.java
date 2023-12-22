package com.bennettzhang.zooseeker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class PlanFragment extends Fragment {
    private View view;
    private TextView summaryTextView;
    private ZooDataViewModel viewModel;
    private ZooDataAdapter adapter;
    private Button clearButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ZooDataViewModel.class);
        adapter = new ZooDataAdapter();
        adapter.setHasStableIds(true);
        viewModel = new ViewModelProvider(this).get(ZooDataViewModel.class);
    }

    public void setSummary(List<Exhibit> exhibit) {
        Direction direction = new Direction();
        summaryTextView.setText(direction.getSummary(exhibit));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_plan, container, false);
        summaryTextView = view.findViewById(R.id.summary_text_view);

        viewModel.getZooData().observe(getViewLifecycleOwner(), this::setSummary);

        clearButton = view.findViewById(R.id.clear_button);
        clearButton.setOnClickListener((View view) -> viewModel.clearAll(AddExhibitFragment.exhibits));

        return view;
    }
}