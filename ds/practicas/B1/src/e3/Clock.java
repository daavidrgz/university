package e3;

public class Clock {
    private int hours;
    private int minutes;
    private int seconds;
    enum Period {
        AM,
        PM
    }
    private Period period;
    /**
     * Creates a Clock instance parsing a String .
     * @param s The string representing the hour in 24h format (17:25:15) or in
     * 12h format (05:25:15 PM ).
     * @throws IllegalArgumentException if the string is not a valid hour .
     */
    public Clock ( String s) throws IllegalArgumentException {
        boolean exception = false;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        String aux[] = s.split(" ");
        boolean periodExists  = false;
        if (aux.length == 2 ) {
            if (aux[1].equals("PM")) {
                this.period = Period.PM;
                periodExists = true;
            } else if (aux[1].equals("AM")) {
                this.period = Period.AM;
                periodExists = true;
            } else {
                throw new IllegalArgumentException();
            }
        } else if(aux.length != 1) {
            throw new IllegalArgumentException ();
        }

        try {
            hours = Integer.parseInt(s.substring(0,2));
            minutes = Integer.parseInt(s.substring(3,5));
            seconds = Integer.parseInt(s.substring(6,8));
        } catch (Exception e) {
            throw new IllegalArgumentException();

        }
        if (periodExists){
            if (!(hours >= 1 && hours <= 12)) {
                exception = true;
            }

        } else {
            if ( !(hours >= 0 && hours <= 23)) {
                exception = true;
            }
        }

        if ( !(minutes >= 0 &&  minutes <= 59)) {
            exception = true;
        }
        if ( !(seconds >= 0 &&  seconds <= 59)) {
            exception = true;
        }


        if ( s.charAt(2) != ':' || s.charAt(5) != ':') {
            throw new IllegalArgumentException();
        }

        if( !exception ) {
            String segments [] = s.split(":");
            this.minutes = Integer.parseInt(segments[1]);
            this.seconds = Integer.parseInt(segments[2].substring(0,2));

            if ( !periodExists ) {
                if ( hours == 12) {
                    this.period = Period.PM;
                    this.hours = hours;
                }else if(hours == 0 )  {
                    this.period = Period.AM;
                    this.hours = hours + 12;
                }else if(hours > 12 )  {
                    this.period = Period.PM;
                    this.hours = hours - 12;
                } else {
                    this.period = Period.AM;
                    this.hours = hours;
                }
            } else {
                this.hours = hours;
            }
        } else {
            throw  new IllegalArgumentException();
        }
    }
    /**
     * Creates a clock given the values in 24h format .
     * @param hours Hours in 24h format .
     * @param minutes Minutes .
     * @param seconds Seconds .
     * @throws IllegalArgumentException if the values do not represent a valid
     * hour .
     */
    public Clock ( int hours , int minutes , int seconds ) throws IllegalArgumentException{
        boolean exception = false;
        if ( !(hours >= 0 && hours <= 23)) {
            exception = true;
        }
        if ( !(minutes >= 0 &&  minutes <= 59)) {
            exception = true;
        }
        if ( !(seconds >= 0 &&  seconds <= 59)) {
            exception = true;
        }

        if( !exception ) {
            this.minutes = minutes;
            this.seconds = seconds;

            if ( hours == 12) {
                this.period = Period.PM;
                this.hours = hours;
            }else if(hours == 0 )  {
                this.period = Period.AM;
                this.hours = hours + 12;
            }else if(hours > 12 )  {
                this.period = Period.PM;
                this.hours = hours - 12;
            } else {
                this.period = Period.AM;
                this.hours = hours;
            }
        } else {
            throw  new IllegalArgumentException();
        }
    }
    /**
     * Creates a clock given the values in 12h format . Period is a enumeration
     * located inside the Clock class with two values : AM and PM.
     * @param hours Hours in 12h format .
     * @param minutes Minutes .
     * @param seconds Seconds .
     * @param period Period used by the Clock ( represented by an enum ).
     * @throws IllegalArgumentException if the values do not represent a valid
     * hour .
     */
    public Clock ( int hours , int minutes , int seconds , Period period ) throws IllegalArgumentException{
        boolean exception = false;
        if (!(hours >= 1 && hours <= 12)) {
            exception = true;
        }

        if ( !(minutes >= 0 &&  minutes <= 59)) {
            exception = true;
        }
        if ( !(seconds >= 0 &&  seconds <= 59)) {
            exception = true;
        }

        if( !exception ) {
            this.minutes = minutes;
            this.seconds = seconds;
            this.period = period;
            this.hours = hours;
        } else {
            throw  new IllegalArgumentException();
        }
    }
    /**
     * Returns the hours of the clock in 24h format
     * @return the hours in 24h format
     */
    public int getHours24 () {
        if ( this.period == Period.PM) {
            if( this.hours == 12) {
                return this.hours;
            }else {
                return this.hours + 12;
            }
        }else {
            if ( this.hours == 12) {
                return 0;
            } else {
                return this.hours;
            }
        }
    }
    /**
     * Returns the hours of the clock in 12h format
     * @return the hours in 12h format
     */
    public int getHours12 () {
        return this.hours;
    }
    /**
     * Returns the minutes of the clock
     * @return the minutes
     */
    public int getMinutes () {
        return this.minutes;
    }
    /**
     * Returns the seconds of the clock .
     * @return the seconds .
     */
    public int getSeconds () {
        return this.seconds;
    }
    /**
     * Returns the period of the day (AM or PM) that the clock is representing
     * @return An instance of the Clock . Period enum depending if the time is
     * before noon (AM) or after noon (PM ).
     */
    public Period getPeriod () {
        return this.period;
    }
    /**
     * Prints a String representation of the clock in 24h format .
     * @return An string in 24h format
     * @see String . format function to format integers with leading zeroes
     */
    public String printHour24 () {
        StringBuilder str = new StringBuilder();

        str.append(String.format("%02d", getHours24())).append(":")
                .append(String.format("%02d", this.minutes)).append(":")
                .append(String.format("%02d", this.seconds));
        return str.toString();
    }
    /**
     * Prints a String representation of the clock in 12h format .
     * @return An string in 12h format
     * @see String . format function to format integers with leading zeroes
     */
    public String printHour12 () {
        StringBuilder str = new StringBuilder();
        str.append(String.format("%02d", this.hours)).append(":")
                .append(String.format("%02d", this.minutes)).append(":")
                .append(String.format("%02d", this.seconds)).append(" ")
                .append(this.period);
        return str.toString();
    }
    /**
     * Performs the equality tests of the current clock with another clock
     * passed as a parameter . Two clock are equal if they represent the same
     * instant regardless of being in 12h or 24h format .
     * @param o The clock to be compared with the current clock .
     * @return true if the clocks are equals , false otherwise .
     */
    @Override
    public boolean equals ( Object o) {
        if ( this == o ) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if ( this.getClass() != o.getClass()) {
            return false;
        }

        Clock clk = (Clock) o;
        if ( this.hours != clk.hours ||
                this.minutes != clk.minutes ||
                this.seconds != clk.seconds ||
                this.period != clk.period
        ) {
            return false;
        }

        return true;
    }
    /**
     * Returns a integer that is a hash code representation of the clock using
     * the " hash 31" algorithm .
     * Clocks that are equals must have the same hash code .
     * @return the hash code
     */
    @Override
    public int hashCode () {
        int result = 31 * this.hours + this.minutes + this.minutes + this.period.hashCode();
        return result;
    }
}
