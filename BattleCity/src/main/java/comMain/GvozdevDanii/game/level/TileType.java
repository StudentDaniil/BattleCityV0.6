package comMain.GvozdevDanii.game.level;

public enum TileType {
    EMPTY(0),
    BRICK(1),
    METAL(2),
    WATER(3),
    GRASS(4),
    ICE(5),
    FLAG(6);

    private final int n;
    TileType(int n){
        this.n = n;
    }


    public int num(){
        return n;
    }

    public static TileType fromNum(int n){
        switch (n){
            case 1:
                return BRICK;
            case 2:
                return METAL;
            case 3:
                return WATER;
            case 4:
                return GRASS;
            case 5:
                return ICE;
            case 6:
                return FLAG;
            default:
                return EMPTY;
        }
    }
}
