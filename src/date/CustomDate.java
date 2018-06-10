package date;

/* FYI
   YMS = YEAR, MONTH, DAY
   HMS = HOUR, MINUTE, SECOND
*/

public class CustomDate implements Comparable<CustomDate>{
    private int year, month, day, hour, minute, second;

    /* ================= CONSTRUCTORS AND SUBMETHODS ======================= */
    
    /**
     * Creates an empty object to setup values via setters.
     * Sets all fields to 0 to avoid NullPointerException
     */
    private CustomDate() {
        this.year   = 0;
        this.month  = 0;
        this.day    = 0;
        this.hour   = 0;
        this.minute = 0;
        this.second = 0;
    }

    /**
     * Copy constructor. Copies every field to new object.
     * @param other
     */
    public CustomDate (CustomDate other) {
        this.year   = other.year;
        this.month  = other.month;
        this.day    = other.day;
        this.hour   = other.hour;
        this.minute = other.minute;
        this.second = other.second;
    }
    
    // inner constructor for static create method
    // throws InvalidCustomDateException there is any invalid argument
    private CustomDate(String year, String month, String day,
                       String hour, String minute, String second) 
                       throws InvalidCustomDateException {
        
        this.year   = Integer.parseInt(year);
        this.month  = Integer.parseInt(month);
        this.day    = Integer.parseInt(day);
        this.hour   = Integer.parseInt(hour);
        this.minute = Integer.parseInt(minute);
        this.second = Integer.parseInt(second);
        
        // tmp solution
        if (!validate()) throw new InvalidCustomDateException();
    }

    private boolean validate() {

        if ( year   < 1000 || year > 9999 )                return false;
        if ( month  < 1    || month > 12 )                 return false;
        if ( day    < 1    || day > getDayInMonth(month) ) return false;
        if ( hour   < 0    || hour > 23 )                  return false;
        if ( minute < 0    || minute > 59 )                return false;
        if ( second < 0    || second > 59 )                return false;
        
        return true;
    }
    
    // returns number of days in given month
    private int getDayInMonth(int month) {
        int[] dayInMonths = new int[] {31, 29, 31, 30,
                                       31, 30, 31, 31,
                                       30, 31, 30, 31};
        return dayInMonths[month - 1];
    }
    
    /**
     *
     * @param data
     * @return
     * @throws InvalidCustomDateException
     * Creates new object via data String in form:
     * year-month-data hour:minute:second
     * Validates values then returns object or throws
     * InvalidCustomDataException is there is any invalid argument.
     */
    public static CustomDate stringToCustomDate(String data)
            throws InvalidCustomDateException{
        String[] tmp = data.split(" ");
        String[] ymd = tmp[0].split("-");
        String[] hms = tmp[1].split(":");
        
        return new CustomDate(ymd[0], ymd[1], ymd[2],
                              hms[0], hms[1], hms[2]);
    }
    
    /**
     *
     * @param minutes
     * @return created CustomDate object only minutes. Balances minutes to
     * hours if arg >= 60 and sets hour according to it.
     * Most useful to use for addHms() parameter
     * @throws date.InvalidCustomDateException
     */
    // test
    public static CustomDate MinutesToCustomDate(int minutes) 
            throws InvalidCustomDateException{
        
        CustomDate date = new CustomDate();
        date.setMinute(minutes);
        return date;
    }
    
    /* ======================= PRIVATE SETTER ============================= */

    /**
     *
     * @param minute
     * Sets minutes to given arg then corrects it and balances if arg >=60
     */
    private void setMinute(int minute) {
        this.minute = minute;
        balance();
    }
    
    /* ============================= GETTERS ============================= */
    
    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    /**
     *
     * @return date as a string. For example '2012-12-21'
     */
    public String getYmdAsString() {
        return new StringBuilder().append(year).append("-")
                                  .append(month).append("-")
                                  .append(day).toString();
    }

    /**
     *
     * @return date as a string. For example '06:30:15'
     */
    // test
    public String getHmsAsString() {
        return new StringBuilder().append(hour).append(":")
                                  .append(minute).append(":")
                                  .append(second).toString();
    }

    public boolean intervalsWith(CustomDate other) {
        if ( this.equals(other) ) return true;
        
        return false;
        
    }
    
    /* ========================= ADDITION ================================== */
    
    /**
     *
     * @param toAdd
     * Use  HmstoCustomDate() to create a CustomDate object with only
     * hour, minute and seconds to add easily.
     */
    // test
    public CustomDate addHms(CustomDate toAdd) {
        this.hour   += toAdd.hour;
        this.minute += toAdd.minute;
        this.second += toAdd.second;
        
        balance();
        return this;
    }
    
    //test
    public CustomDate addMinutes(int minutes) {
        this.minute += minutes;
        balance();
        return this;
    }
    
    private void balance() {
        if (second >= 60) {
            int howMany = additionalTime(second, 60);
            second -= howMany * 60;
            minute += howMany;
        }
        if (minute >= 60) {
            int howMany = additionalTime(minute, 60);
            minute -= howMany * 60;
            hour   += howMany;
        }
        if (hour  >= 24) {
            int howMany = additionalTime(hour, 24);
            hour -= howMany * 24;
            day  += howMany;
        }
        if ( day >= getDayInMonth(month) ) {
            int howMany = additionalTime(day, getDayInMonth(month));
            day   -= howMany * getDayInMonth(month);
            month += howMany;
        }
        if (month > 12) {
            int howMany = additionalTime(month, 12);
            month -= howMany * 12;
            year  += howMany;
        }
        if (year > 9999) {
            System.out.println("You are wrong on so many levels son.");
        }
        
    }
    
    private int additionalTime(int timePart, int shiftNumber) {
        int needToAdd = 0;
        while (timePart >= shiftNumber) {
            timePart -= shiftNumber;
            ++needToAdd;
        }
        return needToAdd;
    }
    
    /*public CustomDate calculate(CustomDate start, int length) {
        // increase length because of cleanup
        length += 30;
        
        int hours = 0;
        int minutes;
        while(length >= 60) {
            ++hours;
            length -= 60;
        }
        minutes = length;
        
        return addDates(start, new CustomDate(hours,minutes));
        
    }
    
    private CustomDate addDates(CustomDate start, int hour, int minute) {
        int resultHour = start.hour + end.hour;
        int resultMinute = start.minute + end.minute;
        while(resultMinute >= 60) {
            ++resultHour;
            resultMinute = resultMinute - 60;
        }
        
        return new CustomDate(resultHour, resultMinute);
    }*/

    /* ================= OVERRIDING AND SUBMETHOTDS ======================== */
    
    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if ( !(other instanceof CustomDate) ) return false;
        
        CustomDate otherDate = (CustomDate) other;
        return (compareTo(otherDate) == 0);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.year;
        hash = 29 * hash + this.month;
        hash = 29 * hash + this.day;
        hash = 29 * hash + this.hour;
        hash = 29 * hash + this.minute;
        hash = 29 * hash + this.second;
        return hash;
    }
  
    @Override
    public int compareTo(CustomDate other) {
        
        // compares years, months, days
        int yearComparation  = ymdCompare(this.year,  other.year);
        int monthComparation = ymdCompare(this.month, other.month);
        int dayComparation   = ymdCompare(this.day,   other.day);
        
        // if not the same day in same month and same year we're done
        if (yearComparation != 0) {
            return yearComparation;
        } else if (monthComparation != 0) {
            return monthComparation;
        } else if (dayComparation != 0) {
            return dayComparation;
        }
           
        // same date only hours and minutes matter (we don't concern seconds)
        return compareHoursMinutes(other);
    }
    
    private int ymdCompare(int thisYMD, int otherYMD) {
        if (thisYMD < otherYMD) {
           return -1;
       } else if (thisYMD > otherYMD) {
           return 1;
       }
        
        return 0;
    }
    
    private int compareHoursMinutes(CustomDate other) {
        if (hour < other.hour) {
            return -1;
        } else if ( hour > other.hour) {
            return 1;
        } else if (minute < other.minute) {
            return -1;
        } else if (minute > other.minute) {
            return 1;
        }
        // If both hours and minutes are equal we consider them equal.
        return 0;
    }
    
    @Override
    // for example 2015-12-01 15:30:00
    public String toString() {
        
        return new StringBuilder()
                .append(year).append("-")
                .append(convertIfNullDate(month)) .append("-")
                .append(convertIfNullDate(day))   .append(" ")
                .append(convertIfNullDate(hour))  .append(":")
                .append(convertIfNullDate(minute)).append(":")
                .append(convertIfNullDate(second))
                .toString();
    }
    
    // just for correct formatting
    private String convertIfNullDate(int date) {
        if (date == 0) return "00";
        if (date < 10) return "0" + Integer.toString(date);
        return Integer.toString(date);
    }

}
