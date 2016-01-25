package ramola.techsearch;


import android.os.Parcel;
import android.os.Parcelable;

public class item_weather implements Parcelable{
    public String date,status,max,min,icon;

    public item_weather(String date, String status, String max, String min,String icon) {
        this.date = date;
        this.status = status;
        this.max = max;
        this.min = min;
        this.icon=icon;
    }

    public item_weather(Parcel parcel) {
      date=parcel.readString();
      status=parcel.readString();
      max=parcel.readString();
      min=parcel.readString();
      icon=parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(status);
        parcel.writeString(max);
        parcel.writeString(min);
        parcel.writeString(icon);
    }
 public static Creator<item_weather> creator=new Creator<item_weather>() {
     @Override
     public item_weather createFromParcel(Parcel parcel) {
         return new item_weather(parcel);
     }

     @Override
     public item_weather[] newArray(int i) {
         return new item_weather[0];
     }
 }   ;
}
