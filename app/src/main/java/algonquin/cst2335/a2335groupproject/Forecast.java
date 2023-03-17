package algonquin.cst2335.a2335groupproject;

/** Represents a single forecast
 * @author Noah King
 * @version 1.0
 */
public class Forecast {

    /** Country of the forecast */
    private String forecastCountry;

    /** City of the forecast */
    private String forecastCity;

    /** Date forecast was saved */
    private String forecastDate;

    /** Temperature of the forecast */
    private int forecastTemp;

    /** Constructor for new forecasts
     * @param forecastCountry Country of the forecast
     * @param forecastCity City of the forecast
     * @param forecastDate Date of the forecast
     * @param forecastTemp Temperature of the forecast
     */
    public Forecast(String forecastCountry, String forecastCity, String forecastDate, int forecastTemp) {
        this.forecastCountry = forecastCountry;
        this.forecastCity = forecastCity;
        this.forecastDate = forecastDate;
        this.forecastTemp = forecastTemp;
    }

    /** Getter for forecastCountry
     * @return The country of the forecast
     */
    public String getForecastCountry() {
        return this.forecastCountry;
    }

    /** Getter for forecastCity
     * @return The city of the forecast
     */
    public String getForecastCity() {
        return this.forecastCity;
    }

    /** Getter for forecastDate
     * @return The date of the forecast
     */
    public String getForecastDate() {
        return this.forecastDate;
    }

    /** Getter for forecastTemp
     * @return The temperature of the forecast
     */
    public int getForecastTemp() {
        return this.forecastTemp;
    }
}
