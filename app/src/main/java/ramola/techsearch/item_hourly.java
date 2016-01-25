package ramola.techsearch;


import android.os.Parcel;
import android.os.Parcelable;

public class item_hourly implements Parcelable {
    public String icon_hourly, description, name_hourly, time_hourly, max_hourly, min_hourly, wind_hourly, humidity_hourly;

    public item_hourly(String icon_hourly, String description, String name_hourly, String time_hourly, String max_hourly, String min_hourly, String wind_hourly, String humidity_hourly) {
        this.icon_hourly = icon_hourly;
        this.description = description;
        this.name_hourly = name_hourly;
        this.time_hourly = time_hourly;
        this.max_hourly = max_hourly;
        this.min_hourly = min_hourly;
        this.wind_hourly = wind_hourly;
        this.humidity_hourly = humidity_hourly;
    }

    public item_hourly(Parcel parcel) {
        icon_hourly = parcel.readString();
        description = parcel.readString();
        name_hourly = parcel.readString();
        time_hourly = parcel.readString();
        max_hourly = parcel.readString();
        min_hourly = parcel.readString();
        wind_hourly = parcel.readString();
        humidity_hourly = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(icon_hourly);
        parcel.writeString(description);
        parcel.writeString(name_hourly);
        parcel.writeString(time_hourly);
        parcel.writeString(max_hourly);
        parcel.writeString(min_hourly);
        parcel.writeString(wind_hourly);
        parcel.writeString(humidity_hourly);
    }

    public static Creator<item_hourly> creator = new Creator<item_hourly>() {
        @Override
        public item_hourly createFromParcel(Parcel parcel) {
            return new item_hourly(parcel);
        }

        @Override
        public item_hourly[] newArray(int i) {
            return new item_hourly[0];
        }
    };
}
