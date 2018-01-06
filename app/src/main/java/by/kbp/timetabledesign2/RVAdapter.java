package by.kbp.timetabledesign2;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.LectureViewHolder>{



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(LectureViewHolder lectureViewHolder, int i) {
        lectureViewHolder.subjectName.setText(lectures.get(i).subject);
        lectureViewHolder.teacherName.setText(lectures.get(i).teacher);
        lectureViewHolder.placeName.setText(lectures.get(i).place);
        lectureViewHolder.groupName.setText(lectures.get(i).group);

    }



    @Override
    public int getItemCount() {
        return lectures.size();
    }

    @Override
    public LectureViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardfragment, viewGroup, false);
        LectureViewHolder lvh = new LectureViewHolder(v);
        return lvh;
    }



    private List<Lecture> lectures;
    public RVAdapter(List<Lecture> lectures){
        this.lectures = lectures;
    }

    public static class LectureViewHolder extends RecyclerView.ViewHolder {
        CardView cv;

        TextView subjectName;
        TextView teacherName;
        TextView placeName;
        TextView groupName;


        LectureViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            subjectName = (TextView)itemView.findViewById(R.id.subject_name);
            teacherName = (TextView)itemView.findViewById(R.id.teacher_name);
            placeName = (TextView)itemView.findViewById(R.id.place_name);
            groupName = (TextView)itemView.findViewById(R.id.group_name);
        }

    }
}
