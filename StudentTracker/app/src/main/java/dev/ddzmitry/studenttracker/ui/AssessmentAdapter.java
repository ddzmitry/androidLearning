package dev.ddzmitry.studenttracker.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.ddzmitry.studenttracker.AssessmentActivity;
import dev.ddzmitry.studenttracker.R;
import dev.ddzmitry.studenttracker.database.AssessmentType;
import dev.ddzmitry.studenttracker.models.Assessment;
import dev.ddzmitry.studenttracker.utilities.Utils;

import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_ASSESSMENT_ID;

/**
 * Created by dzmitrydubarau on 7/30/20.
 */

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.ViewHolder> {

    private final List<Assessment> assessments;
    private final Context mContext;

    public AssessmentAdapter(List<Assessment> assessments, Context mContext) {


        System.out.println("Called Assessment adapter");
        for (Assessment assessment : assessments) {
            System.out.println("IN AssessmentAdapter");
            System.out.println(assessment.toString());
        }

        this.assessments = assessments;
        this.mContext = mContext;
    }
    private Context context;


    public class ViewHolder  extends RecyclerView.ViewHolder{


        @BindView(R.id.assessment_date_text)
        TextView assessment_date_text;

        @BindView(R.id.assessment_text)
        TextView assessment_text;

        @BindView(R.id.fab_assessment)
        FloatingActionButton fab_assessment;

        @BindView(R.id.in_image_assessment)
        ConstraintLayout in_image_assessment;

        public ViewHolder(View itemView) {
            super(itemView);
            System.out.println("RecyclerView.ViewHolder");
            ButterKnife.bind(this,itemView);
        }
    }


    @NonNull
    @Override
    public AssessmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("onCreateViewHolder");
        System.out.println("Parent");
        System.out.println(parent.toString());
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.assessment_list_item_in_course,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.ViewHolder holder, int position) {

        // get position
        final Assessment assessment = assessments.get(position);
        System.out.println("onBindViewHolder");
        System.out.println(assessment.toString());
        holder.assessment_text.setText(assessment.getAssessment_name());
        // check for nulls
        holder.assessment_date_text.setText(assessment.getAssessment_due_date() != null
                ?
                String.format("%s", Utils.formatDate(assessment.getAssessment_due_date())) : "NA" );

        // Set Background
        if(assessment.getAssessmentType() == AssessmentType.OA){
            holder.in_image_assessment.setBackgroundColor(ContextCompat.getColor(context,R.color.oa_assessment));
        } else{
            holder.in_image_assessment.setBackgroundColor(ContextCompat.getColor(context,R.color.pa_assessment));
        }

        // On click listener
        holder.fab_assessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(mContext, AssessmentActivity.class);
                        intent.putExtra(KEY_ASSESSMENT_ID,assessment.getAssessment_id());
                        mContext.startActivity(intent);
                    }
                }).start();
            }
        });


    }

    @Override
    public int getItemCount() {

        System.out.println("getItemCount");
        System.out.println(assessments.size());
        return assessments.size();
    }


}
