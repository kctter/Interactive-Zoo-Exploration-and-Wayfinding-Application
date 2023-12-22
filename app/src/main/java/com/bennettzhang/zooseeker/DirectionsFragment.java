package com.bennettzhang.zooseeker;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import com.google.common.collect.ImmutableList;

public class DirectionsFragment extends Fragment {
    public static final String EXTRA_USE_LOCATION_SERVICE = "use_location_updated";
    public static String detailedDirection;
    public static String briefDirection;
    public static boolean brief;
    public TextView directionTextView;
    public List<Coord> coordList;
    ArrayList<Exhibit> planList;
    private View view;
    private ZooDataAdapter adapter;
    private ZooDataViewModel viewModel;
    private Button next;
    private Button back;
    private Button skip;
    private boolean popupDisplayed = false;
    private int index;
    private String Entrance_Exit;
    private List<String> exhibitOrder = new ArrayList<>();
    private Direction dir = new Direction();
    private boolean reversed = false;
    private boolean useLocationServices;
    private String[] visitArr;
    private LocationModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();


        if (activity != null)
            useLocationServices = activity.getIntent().getBooleanExtra(EXTRA_USE_LOCATION_SERVICE, false);

        model = new LocationModel(getActivity().getApplication());

        SharedPreferences prefs = getContext().getSharedPreferences("index_value", MODE_PRIVATE);
        index = prefs.getInt("index_value", 0);

        // If GPS is enabled, then update the model from the Location service.
        if (useLocationServices) {
            LocationManager locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
            String provider = LocationManager.GPS_PROVIDER;
            model.addLocationProviderSource(locationManager, provider);
        }

        adapter = new ZooDataAdapter();
        adapter.setHasStableIds(true);
        viewModel = new ViewModelProvider(this).get(ZooDataViewModel.class);
    }

    private void setDirectionData(List<Exhibit> vertexInfoList) {
        adapter.setZooData(vertexInfoList);

        planList = new ArrayList<>();
        List<String> idList = new ArrayList<>();
        Entrance_Exit = "";

        for (int i = 0; i < vertexInfoList.size(); i++) {
            if (vertexInfoList.get(i).kind.name().equals("GATE")) {
                Log.e("Entrance_Exit", Entrance_Exit);
                Entrance_Exit = vertexInfoList.get(i).id;
            }
            if (vertexInfoList.get(i).planned) {
                planList.add(vertexInfoList.get(i));
                idList.add(vertexInfoList.get(i).id);
            }
        }

        adapter.setZooData(planList);

        if (planList.size() != 0) {
            dir = new Direction();
            idList.add(Entrance_Exit);
            exhibitOrder = dir.getDirectionOrder(new HashSet<>(idList), Entrance_Exit, Entrance_Exit);
            briefDirection = dir.getBriefDirection(exhibitOrder.get(index), exhibitOrder.get(index + 1),getGroupAnimals());
            detailedDirection = dir.getDetailedDirection(exhibitOrder.get(index), exhibitOrder.get(index + 1),getGroupAnimals());

            setDirections();
        }
        else{
            index = 0;
            SharedPreferences.Editor editor = getContext().getSharedPreferences("index_value", MODE_PRIVATE).edit();
            editor.putInt("index_value", index);
            editor.apply();
        }

        coordList = dir.getExhibitCoord();

        // Observe the model
        if (!coordList.isEmpty()) {
            model.getLastKnownCoords().observe(getViewLifecycleOwner(), (coord) -> {
                Log.i(TAG, String.format("Observing location model update to %s", coord));
                double minDistance = getDistance(coord, coordList.get(index));

                for (int i = index; i < coordList.size() - 1; i++) {
                    double distance = getDistance(coord, coordList.get(i));
                    if (distance < minDistance) {
                        Log.e("location", "Replan popup");
                        minDistance = distance;
                        showReplanPopup(i);
                    }
                }
            });
        }
    }

    private List<String> getGroupAnimals(){
        List<String> groupAnimals = new ArrayList<String>();
        for(Exhibit exhibit : planList){
            if(exhibit.hasGroup()){
                groupAnimals.add(exhibit.id);
            }
        }
        return groupAnimals;
    }

    public void showReplanPopup(int newIndex) {
        if (!popupDisplayed) {
            popupDisplayed = true;
            new AlertDialog.Builder(getContext())
                    .setTitle("Off track. Replan?")
                    .setMessage("Replan to visit " + exhibitOrder.get(newIndex) + " next?")
                    .setPositiveButton("Confirm", (DialogInterface dialog, int which) -> {
                        // continue with delete
                        Log.e("confirm button", "confirm button pressed");
                        exhibitOrder = dir.getReplan(index, exhibitOrder.get(newIndex), Entrance_Exit);
                        briefDirection = dir.getBriefDirection(exhibitOrder.get(index), exhibitOrder.get(index + 1),getGroupAnimals());
                        detailedDirection = dir.getDetailedDirection(exhibitOrder.get(index), exhibitOrder.get(index + 1),getGroupAnimals());

                        setDirections();
                        Log.e("confirm button end", "reached the end of confirm button");
                    })
                    .setNegativeButton("Cancel", (DialogInterface dialog, int which) -> {
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }


    // IMPORTANT!!! CALL THIS HELPER FUNCTION INSTEAD OF setText DIRECTLY!!!
    public void setDirections() {
        String directions = brief ? briefDirection : detailedDirection;

        directions = directions.replace("\n", "\n\0");
        Spannable spannable = new SpannableString(directions);

        for (int i = 0; i < directions.length() - 1; i++) {
            if (directions.charAt(i) == '\n') {
                spannable.setSpan(new RelativeSizeSpan(1.6f), i + 1, i + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        directionTextView.setText(spannable, TextView.BufferType.SPANNABLE);

        SharedPreferences.Editor editor = getContext().getSharedPreferences("index_value", MODE_PRIVATE).edit();
        editor.putInt("index_value", index);
        editor.apply();
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_directions, container, false);

        detailedDirection = getString(R.string.empty_plan);
        briefDirection = getString(R.string.empty_plan);

        viewModel.getZooData().observe(getViewLifecycleOwner(), this::setDirectionData);
        Coord c1 = new Coord(1.0,1.0);
        Coord c2 = new Coord(1.0,1.0);
        List<Coord> route = Coords
                .interpolate(Coords.ENTRANCE_EXIT_GATE, Coords.KOI_FISH, 20)
                .collect(Collectors.toList());

        if (!useLocationServices) {
            model.mockRoute(route, 500, TimeUnit.MILLISECONDS);
            System.err.println("location services are off");
        }

        directionTextView = view.findViewById(R.id.plan_direction);
        directionTextView.setMovementMethod(new ScrollingMovementMethod());
        next = view.findViewById(R.id.next);
        back = view.findViewById(R.id.back);
        skip = view.findViewById(R.id.skip);

        next.setOnClickListener(this::onNextClicked);
        back.setOnClickListener(this::onBackClicked);
        skip.setOnClickListener(this::onSkipClicked);

        return view;
    }

    private void onNextClicked(View view) {
        if (index == exhibitOrder.size() - 2 && !reversed || planList.isEmpty()) {
            return;
        }

        if (!reversed)
            index++;

        briefDirection = dir.getBriefDirection(exhibitOrder.get(index), exhibitOrder.get(index + 1),getGroupAnimals());
        detailedDirection = dir.getDetailedDirection(exhibitOrder.get(index), exhibitOrder.get(index + 1),getGroupAnimals());
        reversed = false;
        popupDisplayed = false;
        setDirections();
    }

    private void onBackClicked(View view) {
        if (index == 0 && reversed || planList.isEmpty()) {
            index = 0;
            return;
        }

        if (reversed)
            index--;


        briefDirection = dir.getBriefDirection(exhibitOrder.get(index + 1), exhibitOrder.get(index),getGroupAnimals());
        detailedDirection = dir.getDetailedDirection(exhibitOrder.get(index + 1), exhibitOrder.get(index),getGroupAnimals());
        reversed = true;
        popupDisplayed = false;
        setDirections();

    }

    //Must update Database (planned boolean)
    private void onSkipClicked(View view) {
        if (index >= exhibitOrder.size() - 2 || planList.isEmpty() || exhibitOrder.size() <= 3)
            return;

        Exhibit skippedExhibit = ZooData.getVertexList().get(exhibitOrder.get(index + 1));

        exhibitOrder = dir.getReplanSkip(index, exhibitOrder.get(index + 2), Entrance_Exit);

        if (index + 3 == exhibitOrder.size())
            exhibitOrder.remove(exhibitOrder.size() - 1);

        briefDirection = dir.getBriefDirection(exhibitOrder.get(index), exhibitOrder.get(index + 1),getGroupAnimals());
        detailedDirection = dir.getDetailedDirection(exhibitOrder.get(index), exhibitOrder.get(index + 1),getGroupAnimals());
        dir.exhibitOrder = exhibitOrder;

        viewModel.removePlanned(skippedExhibit);

        setDirections();


    }

    private double getDistance(Coord c1, Coord c2) {
        double latitude = Math.pow((c1.lat - c2.lat), 2);
        double longitude = Math.pow((c1.lng - c2.lng), 2);
        return Math.sqrt(latitude + longitude);
    }
}