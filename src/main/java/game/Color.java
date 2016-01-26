package game;

/**
 * Created by Al on 18/01/2016.
 */
public class Color{

    private Character color;

    public static Color redColor(){
        return new Color('R');
    }

    public static Color greenColor(){
        return new Color('G');
    }

    public static Color emptyColor(){
        return new Color(' ');
    }

    private Color(char color){
        this.color = color;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Color)obj).color == this.color;
    }

    @Override
    public String toString() {
        return color.toString();
    }
}
