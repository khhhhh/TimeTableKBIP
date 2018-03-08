package by.kbp.timetabledesign2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class  Lecture implements Parcelable {
    String subject;
    String teacher;
    String place;
    String group;
    String number;


    public Lecture(String subject, String teacher, String place, String group, String number) {
        this.subject = subject;
        this.teacher = teacher;
        this.place = place;
        this.group = group;
        this.number = number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Lecture(Parcel in) {
        String[] data = new String[4];
        in.readStringArray(data);
        subject = data[0];
        teacher = data[1];
        place = data[2];
        group = data[3];
        number = data[4];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
            ArrayList<String> list = new ArrayList<>();
            list.add(subject);
            list.add(teacher);
            list.add(place);
            list.add(group);
            list.add(number);
            dest.writeStringList(list);
    }

    public static final Parcelable.Creator<Lecture> CREATOR = new Parcelable.Creator<Lecture>() {

        @Override
        public Lecture createFromParcel(Parcel source) {
            return new Lecture(source);
        }

        @Override
        public Lecture[] newArray(int size) {
            return new Lecture[size];
        }
    };

}