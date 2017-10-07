# follow-me-drone

An attempt to make a drone follow a person.
The drone used here is AR Drone 2. It is completely programmable and can be moved with commands.

# Architecture

The three devices- phone, raspberry pi and the drone are in the same wifi.
The phone gets the GPS co-ordinates and sends the co-ordinates to the Pi every second.
The raspbeery pi calculates the displacement of the phone and sends commands to the drone to move accordingly.
