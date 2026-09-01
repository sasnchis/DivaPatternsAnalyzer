package ScriptDecryptor.Targets;

public enum TargetType {
    Triangle(0),
    Circle(1),
    Cross(2),
    Square(3),

    TriangleHold(4),
    CircleHold(5),
    CrossHold(6),
    SquareHold(7),

    Random(8),
    RandomHold(9),
    RandomRepeat(10),

    SlideBoth(11),
    SlideL(12),
    SlideR(13),
//    SlideCrashL(), // UN USED

    SlideChainL(15),
    SlideChainR(16),
//    GreenSquareL(), // UN USED

    TriangleChance(18),
    CircleChance(19),
    CrossChance(20),
    SquareChance(21),

    SlideBothChance(22),
    SlideLChance(23),
    SlideRChance(24),

    // F and F2nd
    TriangleDouble(29),
    CircleDouble(30),
    CrossDouble(31),
    SquareDouble(32),

    Star(37),
    StarDouble(39),

    TriangleHoldF2nd(33),
    SquareHoldF2nd(36),
    CrossHoldF2nd(35),
    CircleHoldF2nd(34),
    ;

    public final int id;

    TargetType(int id) {
        this.id = id;
    }

    public static TargetType get(int id) {
        for (int i = 0; i < values().length; i++) {
            if (TargetType.values()[i].id == id) {
                return TargetType.values()[i];
            }
        }
        System.out.println("Can't find, id " + id);
        return null;
    }
}
