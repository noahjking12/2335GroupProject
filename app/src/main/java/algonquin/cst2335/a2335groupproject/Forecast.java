package algonquin.cst2335.a2335groupproject;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Represents a single Forecast in the database
 * @author Noah King
 * @version 1.0
 */
@Entity
public class Forecast {

    /** Auto-generated identifier in the database */
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    /** City of the forecast */
    @ColumnInfo(name = "city")
    protected String city;

    /** Date forecast was saved */
    @ColumnInfo(name = "date")
    protected String date;

    /** File name of the weather icon associated with Forecast */
    @ColumnInfo(name = "icon")
    protected String icon;

    /** Description of the weather */
    @ColumnInfo(name = "description")
    protected String description;

    /** Temperature of the forecast */
    @ColumnInfo(name = "temperature")
    protected int temperature;

    /** What the temperature feels like */
    @ColumnInfo(name = "feelsLike")
    protected int feelsLike;

    /** The forecasts humidity */
    @ColumnInfo(name = "humidity")
    protected int humidity;

    /** The forecasts UV index */
    @ColumnInfo(name = "uvIndex")
    protected int uvIndex;

    /** The forecast wind speed */
    @ColumnInfo(name = "windSpeed")
    protected int windSpeed;

    /** The forecasts visibility */
    @ColumnInfo(name = "visibility")
    protected int visibility;

    /** Constructor for new Forecast's
     * @param city The city of the forecast
     * @param date The date of the forecast
     * @param icon The name of the file of the icon associated with this forecast
     * @param description Description of the forecasts weather
     * @param temperature Temperature of the forecast
     * @param feelsLike What the temperature feels like
     * @param humidity Humidity of the forecast
     * @param uvIndex UV index of the forecast
     * @param windSpeed Wind speed of the forecast
     * @param visibility Visibility of the forecast
     */
    public Forecast(String city, String date, String icon, String description, int temperature, int feelsLike, int humidity, int uvIndex, int windSpeed, int visibility) {
        this.city = city;
        this.date = date;
        this.icon = icon;
        this.description = description;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.uvIndex = uvIndex;
        this.windSpeed = windSpeed;
        this.visibility = visibility;
    }

    /** Getter for city
     * @return The city of the forecast
     */
    public String getCity() {
        return this.city;
    }

    /** Getter for date
     * @return The date of the forecast
     */
    public String getDate() {
        return this.date;
    }

    /** Getter for icon
     * @return The file name of the icon for this forecast
     */
    public String getIcon() { return this.icon; }

    /** Getter for description
     * @return Description of the weather
     */
    public String getDescription() {
        return this.description;
    }

    /** Getter for temperature
     * @return The temperature of the forecast
     */
    public int getTemperature() {
        return this.temperature;
    }

    /** Getter for feelsLike
     * @return What the temperature feels like
     */
    public int getFeelsLike() { return this.feelsLike; }

    /** Getter for humidity
     * @return The humidity of the forecast
     */
    public int getHumidity() { return this.humidity; }

    /** Getter for uvIndex
     * @return The uv index of the forecast
     */
    public int getUvIndex() { return this.uvIndex; }

    /** Getter for windSpeed
     * @return The wind speed of the forecast
     */
    public int getWindSpeed() { return this.windSpeed; }

    /** Getter for visibility
     * @return The visibility of the forecast
     */
    public int getVisibility() { return this.visibility; }
}
