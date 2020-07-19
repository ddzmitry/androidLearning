package dev.ddzmitry.studenttracker.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import dev.ddzmitry.studenttracker.models.Term;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

public class TermsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Term> allTerms;
    private final Context context;

    public TermsAdapter(List<Term> allTerms, Context context) {
        this.allTerms = allTerms;
        this.context = context;
    }


    // On every call on add it will instantiate a class of  the ViewHolder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
