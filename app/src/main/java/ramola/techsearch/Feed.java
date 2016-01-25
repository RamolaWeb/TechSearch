package ramola.techsearch;


import android.os.Parcel;
import android.os.Parcelable;

public class Feed implements Parcelable {
    public String TITLE;
    public String CATEGORY;
    public String SHORT_DESCRIPTION;
    public String LONG_DESCRIPTION;
    public String URL_PHOTO;

    public Feed(String TITLE, String CATEGORY, String SHORT_DESCRIPTION, String LONG_DESCRIPTION, String URL_PHOTO) {
        this.TITLE = TITLE;
        this.CATEGORY = CATEGORY;
        this.SHORT_DESCRIPTION = SHORT_DESCRIPTION;
        this.LONG_DESCRIPTION = LONG_DESCRIPTION;
        this.URL_PHOTO = URL_PHOTO;
    }

    public Feed(Parcel parcel) {
        TITLE=parcel.readString();
        CATEGORY=parcel.readString();
        SHORT_DESCRIPTION=parcel.readString();
        LONG_DESCRIPTION=parcel.readString();
        URL_PHOTO=parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
       parcel.writeString(TITLE);
       parcel.writeString(CATEGORY);
       parcel.writeString(SHORT_DESCRIPTION);
       parcel.writeString(LONG_DESCRIPTION);
       parcel.writeString(URL_PHOTO);
    }
    public static Creator<Feed> creator=new Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel parcel) {
            return new Feed(parcel);
        }

        @Override
        public Feed[] newArray(int i) {
            return new Feed[0];
        }
    };
}
