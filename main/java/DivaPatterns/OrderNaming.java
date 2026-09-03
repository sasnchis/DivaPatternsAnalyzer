package DivaPatterns;

public enum OrderNaming {
    /**
     *
     * @return 0 if same
     * 1 if clockwise
     * -1 if counterclockwise
     * 2 if opposite
     * 3 if triples changing note
     * -2 if incorrect checking
     */
    SAME,
    CLOCKWISE,
    COUNTER_CLOCKWISE,
    OPPOSITE,
    TRIPLES_CHANGING,
    INCORRECT
    ;
}
