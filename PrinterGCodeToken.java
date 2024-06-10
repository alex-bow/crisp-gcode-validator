enum PrinterGCodeToken implements TokenBase {
    G_CMD,
    M_CMD,

    // Only tracking param types that influence a toolpath
    X_PM,
    Y_PM,
    Z_PM,
    I_PM,
    J_PM,
    R_PM,
    E_PM,

    PARAM, // other than any types defined above

    EOF
}