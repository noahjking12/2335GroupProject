package algonquin.cst2335.a2335groupproject;
public class Pictures {
    String date;

    boolean isSearchButton;

    Pictures(String date,  boolean isSearchButton)
    {
        this.date=date;
        this.isSearchButton=isSearchButton;
    }

    public String getDate(){
       return date;
    }

    public boolean getIsSearchButton(){
        return isSearchButton;
    }

}
