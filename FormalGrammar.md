Define GCODE formal grammar here

command := command param
command := (G_CMD | M_CMD)

param := (X_PM | Y_PM | Z_PM | I_PM | J_PM | R_PM)*

Prusa comment tokens

config := KEY VALUE