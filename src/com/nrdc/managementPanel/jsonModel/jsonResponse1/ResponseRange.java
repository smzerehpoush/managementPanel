package com.nrdc.managementPanel.jsonModel.jsonResponse;

public class ResponseRange {

    private long start;
    private long end;


    public ResponseRange() {

    }

    public ResponseRange(long start, long end) {

        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "ResponseRange{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseRange)) return false;

        ResponseRange that = (ResponseRange) o;

        if (getStart() != that.getStart()) return false;
        return getEnd() == that.getEnd();
    }

    @Override
    public int hashCode() {
        int result = (int) (getStart() ^ (getStart() >>> 32));
        result = 31 * result + (int) (getEnd() ^ (getEnd() >>> 32));
        return result;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
