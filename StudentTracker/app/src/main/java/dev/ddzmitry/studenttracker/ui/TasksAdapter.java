package dev.ddzmitry.studenttracker.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ddzmitry.studenttracker.R;
import dev.ddzmitry.studenttracker.ViewTermActivity;
import dev.ddzmitry.studenttracker.models.Term;

import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_TERM_ID;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private final List<Term> allTerms;
    private final Context mContext;

    public TasksAdapter(List<Term> allTerms, Context mContext) {
        this.allTerms = allTerms;
        this.mContext = mContext;
    }

    // every time whe element created
    // Manage view itself
    public class ViewHolder extends RecyclerView.ViewHolder {
        // In Term list item bind element to id on constructor creation

        // On clicker
        @BindView(R.id.fab_term)
        FloatingActionButton termFab;

        @BindView(R.id.term_text)
        TextView term_text_view;



        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    // Every time when element is getting created
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // pass item to the parent
        View view = inflater.inflate(R.layout.term_list_item,parent,false);
        // Return new view
        return new ViewHolder(view);
//        return null;
    }

    // When Updating item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Each time when refresh

        final Term term = allTerms.get(position);
        // Holder -> Viewholder
        holder.term_text_view.setText(term.getTerm_title());

        holder.termFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ViewTermActivity.class);
                intent.putExtra(KEY_TERM_ID,term.getTerm_id());
                mContext.startActivity(intent);

            }
        });



    }

    // Total amount of data
    @Override
    public int getItemCount() {

        return allTerms.size();
    }


}
