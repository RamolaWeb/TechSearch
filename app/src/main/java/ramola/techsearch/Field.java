package ramola.techsearch;

public interface Field {
    public static String FEED="feed";
    public static String ID="id";
    public static String CATEGORY="category";
    public static String TITLE="title";
    public static String SHORT_DESCRIPTION="short_description";
    public static String LONG_DESCRIPTION="long_description";
    public static String PHOTO="photo";
    public static String BASE_URL="http://www.ramolawinwar.net23.net";
    public static String URL_FETCH_DATA="/ShowAllJson.php";
    public static String URL_UPLOAD="/mobileupload.php";
    public  interface Field_weather{
        public static  String BASE_URL_WEATHER="http://api.openweathermap.org/data/2.5/forecast/daily?";
        public static String APP_ID="be7e8d2e7aa8f19c7ddd24a35c2bdca9";
        public static String UNIT="&units=metric&cnt=5&appid=";
        public static  String ARRAY_NAME_MAIN="list";
        public static String TEMP="temp";
        public static String MAX="max";
        public static String MIN="min";
        public static String WEATHER="weather";
        public static String STATUS="main";
        public static String LOGO="icon";
        public static String DATE="dt";
        public static String IMAGE_URL="http://openweathermap.org/img/w/";
        public static String WIND="wind";
        public static String WIND_SPEED="speed";
        public static String WIND_DEGREE="deg";
        public static  String BASE_URL_WEATHER_CURRENT="http://api.openweathermap.org/data/2.5/weather?";
        public static String CURRENT_UNIT="&units=metric&appid=";
        public static String HUMIDITY="humidity";
    }
    public interface FIELD_WEATHER_DAY{
        public static final String BASE_URL_DAY="http://api.openweathermap.org/data/2.5/forecast?";
        public static final String UNITS_DAILY="&units=metric&appid=";
        public static final String TIME_CALCULATION="dt";
        public static final String MAIN="main";
        public static final String TEMP_MIN="temp_min";
        public static final String TEMP_MAX="temp_max";
        public static final String DAY_HUMIDITY="humidity";
        public static final String WEATHER="weather";
        public static final String ICON="icon";
        public static final String DESCRIPTION="description";
        public static final String DATE="dt_txt";
        public static final String WIND_HOUR="wind";
        public static String WIND_SPEED_HOUR="speed";
        public static String WIND_DEGREE_HOUR="deg";
    }
}
