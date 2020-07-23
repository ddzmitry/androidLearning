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
import dev.ddzmitry.studenttracker.CourseActivity;
import dev.ddzmitry.studenttracker.R;
import dev.ddzmitry.studenttracker.models.Course;
import dev.ddzmitry.studenttracker.utilities.Utils;
import  dev.ddzmitry.studenttracker.utilities.Constans;

import static dev.ddzmitry.studenttracker.utilities.Constans.KEY_COURSE_ID;

/**
 * Created by dzmitrydubarau on 7/19/20.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {


    private final List<Course> courses;
    private final Context mContext;

    public CourseAdapter(List<Course> courses, Context mContext) {
        this.courses = courses;
        this.mContext = mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.course_date_text)
        TextView course_date_text;
        // manage view itself
        @BindView(R.id.course_text)
        TextView courseTextView;
        // Bind Edit button
        @BindView(R.id.fab_course)
        FloatingActionButton courseFab;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }


    @NonNull
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.course_list_item_in_term,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.ViewHolder holder, int position) {

        final Course course = courses.get(position);

        holder.courseTextView.setText(course.getCourse_title());
        // Set Date for course
        holder.course_date_text.setText(String.format("%s - %s",
                Utils.formatDate(course.getCourse_start_date()),
                Utils.formatDate(course.getCourse_end_date())));

        holder.courseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, CourseActivity.class);
                intent.putExtra(KEY_COURSE_ID,course.getCourse_id());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return courses.size();
    }




}
