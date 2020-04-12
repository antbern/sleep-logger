# Sleep Logger

A project to create an Android app to log my sleep habits. This includes
* When I go to sleep and wake up
* How tired I am feeling throughout the day and before/after sleep
* Any movements and phone usages during the sleep (for example wakeups)

Features that have been implemented so far
* Alarm time setting, persisting and surviving system reboot

Features that I want to add
* Alarm
    * Activity to show when alarm goes off
    * Alarm sound and vibrations
    * Snoozing
    * UI: animation of progress bar
* Logging
    * Database to store events that occur
    * Dialogs for entering energy level and tiredness before/after sleep
    * Service to run during the night
        * detecting phone pickups
        * listening to sounds to determine movements
    
* Analytics of event data
