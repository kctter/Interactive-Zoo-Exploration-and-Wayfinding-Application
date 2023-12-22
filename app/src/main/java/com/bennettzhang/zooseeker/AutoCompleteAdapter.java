package com.bennettzhang.zooseeker;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;

public class AutoCompleteAdapter extends ArrayAdapter<String> {
    private final List<VertexInfo> exhibits;
    private List<VertexInfo> filteredExhibits;
    private final int itemLayout;
    private final ListFilter listFilter = new ListFilter();
    private final LayoutInflater inflater;
    private String searchLowerCase = "";
    private Consumer<Exhibit> onCheckBoxClicked;

    public AutoCompleteAdapter(Context context, int resource, List<Exhibit> exhibits,
                               List<String> exhibitNames, String searchText) {
        super(context, resource, exhibitNames);

        this.exhibits = new ArrayList<>();
        this.filteredExhibits = new ArrayList<>();

        for (int i = 0; i < exhibits.size(); i++) {
            VertexInfo vertexInfo = new VertexInfo(exhibits.get(i));
            this.exhibits.add(vertexInfo);
            filteredExhibits.add(vertexInfo);
        }

        this.itemLayout = resource;
        this.inflater = LayoutInflater.from(context);

        this.listFilter.filter(searchText);
    }

    @Override
    public int getCount() {
        return filteredExhibits.size();
    }

    @Override
    public String getItem(int position) {
        Exhibit exhibit = filteredExhibits.get(position).vertexInfo;
        if(exhibit.hasGroup()){
            return exhibit.name + " (" + Objects.requireNonNull(ZooData.getVertexList().get(exhibit.groupId)).name + ")";
        }
        return exhibit.name;
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = inflater.inflate(itemLayout, parent, false);

        TextView textView = view.findViewById(R.id.autocomplete_text);
        String name = getItem(position);

        textView.setText(highlight(name, filteredExhibits.get(position).tag));

        CheckBox checkBox = view.findViewById(R.id.autocomplete_checkbox);

        VertexInfo exhibit = filteredExhibits.get(position);
        if (exhibit != null) {
            checkBox.setChecked(exhibit.vertexInfo.planned);
            checkBox.jumpDrawablesToCurrentState();
        }

        checkBox.setOnClickListener(v -> {
            if (onCheckBoxClicked != null && exhibit != null)
                onCheckBoxClicked.accept(exhibit.vertexInfo);
        });

        view.setOnClickListener((View v) -> {
            checkBox.toggle();

            if (onCheckBoxClicked != null && exhibit != null)
                onCheckBoxClicked.accept(exhibit.vertexInfo);
        });

        return view;
    }

    public void setOnCheckBoxClicked(Consumer<Exhibit> onCheckBoxClicked) {
        this.onCheckBoxClicked = onCheckBoxClicked;
    }

    public CharSequence highlight(String name, String tag) {
        String originalText = name + (tag == null ? "" : " (" + tag + ")");

        // Ignore case and accents
        String normalizedText = Normalizer
                .normalize(originalText, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase(Locale.ENGLISH);

        int start = normalizedText.indexOf(searchLowerCase);

        if (start < 0)
            return name;
        else {
            // Highlight each appearance in the original text
            Spannable highlighted = new SpannableString(originalText);

            int spanStart = Math.min(start, originalText.length());
            int spanEnd = Math.min(start + searchLowerCase.length(), originalText.length());

            highlighted.setSpan(new StyleSpan(BOLD),
                    spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            highlighted.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.dark_sky)),
                    spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (tag != null) {
                spanStart = name.length() + 1;
                spanEnd = originalText.length();

                highlighted.setSpan(new StyleSpan(ITALIC),
                        spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            return highlighted;
        }
    }

    public class ListFilter extends Filter {
        private final Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence search) {
            FilterResults results = new FilterResults();

            if (search == null || search.length() == 0) {
                synchronized (lock) {
                    searchLowerCase = "";
                    results.values = exhibits;
                    results.count = exhibits.size();
                }
            } else {
                searchLowerCase = search.toString().toLowerCase(Locale.ENGLISH);
                ArrayList<VertexInfo> matchValuesStart = new ArrayList<>();
                ArrayList<VertexInfo> matchTagsStart = new ArrayList<>();
                ArrayList<VertexInfo> matchValuesContain = new ArrayList<>();
                ArrayList<VertexInfo> matchTagsContain = new ArrayList<>();

                for (int i = 0; i < exhibits.size(); i++) {
                    VertexInfo exhibit = exhibits.get(i);
                    String name = exhibit.vertexInfo.name;
                    List<String> tags = exhibit.vertexInfo.tags;

                    final String nameLowerCase = name.toLowerCase(Locale.ENGLISH);

                    if (nameLowerCase.startsWith(searchLowerCase))
                        matchValuesStart.add(exhibit);
                    else if (nameLowerCase.contains(searchLowerCase))
                        matchValuesContain.add(exhibit);
                    else {
                        String tagStart = null;
                        String tagContain = null;

                        for (String tag : tags) {
                            String tagLowerCase = tag.toLowerCase(Locale.ENGLISH);
                            String tagProperCase = tag.substring(0, 1).toUpperCase() + tag.substring(1);

                            if (tagLowerCase.startsWith(searchLowerCase)) {
                                tagStart = tagProperCase;
                                break;
                            } else if (tagLowerCase.contains(searchLowerCase) && tagContain == null)
                                tagContain = tagProperCase;
                        }

                        if (tagStart != null) {
                            matchTagsStart.add(new VertexInfo(exhibit, tagStart));
                        } else if (tagContain != null) {
                            matchTagsContain.add(new VertexInfo(exhibit, tagContain));
                        }
                    }
                }

                matchValuesStart.addAll(matchTagsStart);
                matchValuesStart.addAll(matchValuesContain);
                matchValuesStart.addAll(matchTagsContain);

                results.values = matchValuesStart;
                results.count = matchValuesStart.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null)
                filteredExhibits = (ArrayList<VertexInfo>) results.values;
            else
                filteredExhibits = null;

            if (results.count > 0)
                notifyDataSetChanged();
            else
                notifyDataSetInvalidated();
        }
    }

    private static class VertexInfo {
        public Exhibit vertexInfo;
        public String tag;

        public VertexInfo(Exhibit vertexInfo) {
            this.vertexInfo = vertexInfo;
            this.tag = null;
        }

        public VertexInfo(VertexInfo vertexInfo, String tag) {
            this.vertexInfo = vertexInfo.vertexInfo;
            this.tag = tag;
        }
    }
}