import sys

max_rpm = float(sys.argv[1])
native_units_per_rot = float(sys.argv[2])

f_gain = max_rpm * (1.0 / 60.0) * (1.0 / 10.0) * native_units_per_rot

print(1023.0 / f_gain)