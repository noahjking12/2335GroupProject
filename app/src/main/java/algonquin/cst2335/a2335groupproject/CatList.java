package algonquin.cst2335.a2335groupproject;

public class CatList {
    private String catUrl;

    private String width;

    private String height;

    public CatList(String catUrl, String width, String height){
        this.catUrl = catUrl;
        this.width = width;
        this.height = height;
    }

    public  String getCatUrl(){
        return catUrl;
    }

    public String getWidth(){
        return width;
    }

    public String getHeight(){
        return height;
    }

}
