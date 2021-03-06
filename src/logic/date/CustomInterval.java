package logic.date;

import java.util.Objects;

public class CustomInterval {
    
    private final CustomDate begin, end;
    
    public CustomInterval(CustomDate begin, CustomDate end) {
        this.begin = begin;
        this.end   = end;
    }
    
    /**
     * Sets interval begin to given parameter, and end parameter according to
     * minutes given.
     * @param begin
     * @param minutes
     */
    public CustomInterval(CustomDate begin, int minutes) {
        this.begin = begin;
        end = new CustomDate(begin);
        end.addMinutes(minutes);
    }

    public boolean overlapsWith(CustomInterval other) {
        if (this.equals(other)) return true;
        
        final boolean beginEquals = this.begin.equals(other.begin);
        final boolean endEquals   = this.end  .equals(other.end);
        final boolean thisStartedEarlier = this.begin.compareTo(other.begin) < 0;
        final boolean thisEndedEarlier   = this.end  .compareTo(other.end)   < 0;
        
        // if other started earlier
        if (!thisStartedEarlier) {
            // other has already ended when this began
            if ( this.begin.compareTo(other.end) > 0) return false;
            // other began earlier and ended at the same time this began
            // we don't consider this overlaping
            if ( this.begin.equals(other.end) ) return false;
            // if other started earlier but didn't end until this began
            // they surely overlap
            return true;
        // they started at the same time so they definitely overlap
        } else if (beginEquals) {
            return true;
        
        // at this point we know that this started earlier
        
        // this started earlier and ended later -> overlap
        // ( other is a subinterval of this )
        } else if (!thisEndedEarlier) {
            return true;
        // this started earlier and they ended at the same time -> overlap
        } else if (endEquals) {
            return true;
            
        // other began earlier than this ended
        } else if ( other.begin.compareTo(this.end) < 0 ) {
            return true;
        // this ended at the same time as other began
        // we don't consider this overlapping
        } else if (other.begin.equals(this.end)) {
            return false;
        }
        
        // every possibility checked
        // only possibility left: this ended earlier than other began -> not overlapping
        return false;
    }

    public CustomDate getBegin() {
        return begin;
    }

    public CustomDate getEnd() {
        return end;
    }
    
    public String beginToString() {
        return begin.toString();
    }
    
    public String endToString() {
        return end.toString();
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if ( !(other instanceof CustomInterval) ) return false;
        
        final CustomInterval otherInterval = (CustomInterval) other;
        
        return this.begin.equals(otherInterval.begin)
                 && this.end.equals(otherInterval.end);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.begin);
        hash = 47 * hash + Objects.hashCode(this.end);
        return hash;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append("begin: ").append(beginToString())
                                  .append("\nend: ").append(endToString())
                                  .toString();
    }
}
