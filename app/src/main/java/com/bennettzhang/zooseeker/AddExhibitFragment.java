package com.bennettzhang.zooseeker;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddExhibitFragment extends Fragment {
    public static ArrayList<Exhibit> exhibits;
    private AutoCompleteAdapter autoCompleteAdapter;
    private View view;
    private InstantAutoComplete searchBox;
    private ZooDataViewModel viewModel;
    private ZooDataAdapter adapter;
    private RecyclerView recyclerView;
    private TextView textView;
    private Button clearButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ZooDataViewModel.class);
        adapter = new ZooDataAdapter();
        adapter.setHasStableIds(true);
        viewModel = new ViewModelProvider(this).get(ZooDataViewModel.class);
        adapter.setOnDeletedBoxClicked(viewModel::removePlanned);
    }

    private void setZooData(List<Exhibit> exhibits) {
        searchBox = view.findViewById(R.id.search_box);

        exhibits = exhibits.stream()
                .filter(e -> e.kind.toString().equals("EXHIBIT"))
                .collect(Collectors.toList());

        //if (autoCompleteAdapter == null) {
        autoCompleteAdapter = new AutoCompleteAdapter(getContext(),
                R.layout.layout_item_autocomplete, exhibits,
                exhibits.stream().map(e -> e.name).collect(Collectors.toList()),
                searchBox.getText().toString());
        //}

        searchBox.setAdapter(autoCompleteAdapter);

        searchBox.setOnFocusChangeListener((View view, boolean focused) -> {
            if (focused)
                searchBox.showDropDown();
        });
        searchBox.setOnClickListener((View view) -> searchBox.showDropDown());

        autoCompleteAdapter.setOnCheckBoxClicked(viewModel::togglePlanned);
    }

    private void setPlanData(List<Exhibit> vertexInfoList) {
        adapter.setZooData(vertexInfoList);
        exhibits = adapter.getExhibits();

        ArrayList<Exhibit> planList = new ArrayList<>();
        for (int i = 0; i < vertexInfoList.size(); i++) {
            Exhibit exhibit = vertexInfoList.get(i);
            if (exhibit.planned) {
                planList.add(exhibit);
            }
        }
        textView.setText(getString(R.string.num_exhibits_planned, planList.size()));
        adapter.setZooData(planList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_exhibit, container, false);
        viewModel.getZooData().observe(getViewLifecycleOwner(), this::setZooData);
        viewModel.getZooData().observe(getViewLifecycleOwner(), this::setPlanData);

        textView = view.findViewById(R.id.plan_counter);
        textView.setMovementMethod(new ScrollingMovementMethod());

        recyclerView = view.findViewById(R.id.plan_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);

        clearButton = view.findViewById(R.id.clear_button);
        clearButton.setOnClickListener((View view) -> viewModel.clearAll(exhibits));

        return view;
    }
}
