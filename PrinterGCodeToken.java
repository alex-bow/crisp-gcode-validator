enum PrinterGCodeToken implements TokenBase {
    G_CMD,
    M_CMD,

    // Only tracking param types that influence a toolpath
    COORD_PARAM, // X Y Z
    CIRCLE_PARAM, // I J R
    EXTR_PARAM, // E

    PARAM, // other than any types defined above

    EOF
}