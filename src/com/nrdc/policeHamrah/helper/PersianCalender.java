package com.nrdc.policeHamrah.helper;

import org.apache.log4j.Logger;

import java.util.Calendar;

public class PersianCalender {
    private static Logger logger = Logger.getLogger(PersianCalender.class.getName());


    private int day;
    private int month;
    private int year;
    private String time;

    public PersianCalender() {
        this(Calendar.getInstance().getTimeInMillis());
    }

    public PersianCalender(long timeMillis) {
        calcSolarCalendar(timeMillis);
    }

    public static String getTime() {
        return getTime(System.currentTimeMillis());
    }

    public static String getTime(long timeMilli) {
        try {
            String result;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeMilli);
            int hourValue = calendar.get(Calendar.HOUR);
            int minuteValue = calendar.get(Calendar.MINUTE);
            String hour;
            String minute;
            if (hourValue < 10)
                hour = "0" + hourValue;
            else
                hour = "" + hourValue;
            if (minuteValue < 10)
                minute = "0" + minuteValue;
            else
                minute = "" + minuteValue;
            result = hour + ":" + minute;
            return result;
        } catch (Exception exception) {
            throw exception;
        }
    }

    public static String getCompleteTime() {
        return getCompleteTime(System.currentTimeMillis());
    }

    public static String getCompleteTime(long timeMilli) {
        try {
            String result;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeMilli);
            int hourValue = calendar.get(Calendar.HOUR);
            int minuteValue = calendar.get(Calendar.MINUTE);
            int secondValue = calendar.get(Calendar.SECOND);
            String hour;
            String minute;
            String second;
            if (hourValue < 10)
                hour = "0" + hourValue;
            else
                hour = "" + hourValue;
            if (minuteValue < 10)
                minute = "0" + minuteValue;
            else
                minute = "" + minuteValue;
            if (secondValue < 10)
                second = "0" + secondValue;
            else
                second = "" + secondValue;
            result = hour + ":" + minute + ":" + second;
            return result;
        } catch (Exception exception) {
            throw exception;
        }
    }

    public static String getDate() {
        return getDate(System.currentTimeMillis());
    }

    public static String getDate(long timeMilli) {
        try {
            String result;
            result = new PersianCalender(timeMilli).getCustomDate();
            return result;
        } catch (Exception exception) {
            throw exception;
        }
    }

    private void calcSolarCalendar(long timeMillis) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        int ld;
        time = getTime(timeMillis);
        timeMillis = calendar.get(Calendar.HOUR) + calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND);
        int year = calendar.get(Calendar.YEAR);
//        int year = calendar.get(Calendar.YEAR) + 1900;
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        int[] buf1 = new int[12];
        int[] buf2 = new int[12];

        buf1[0] = 0;
        buf1[1] = 31;
        buf1[2] = 59;
        buf1[3] = 90;
        buf1[4] = 120;
        buf1[5] = 151;
        buf1[6] = 181;
        buf1[7] = 212;
        buf1[8] = 243;
        buf1[9] = 273;
        buf1[10] = 304;
        buf1[11] = 334;

        buf2[0] = 0;
        buf2[1] = 31;
        buf2[2] = 60;
        buf2[3] = 91;
        buf2[4] = 121;
        buf2[5] = 152;
        buf2[6] = 182;
        buf2[7] = 213;
        buf2[8] = 244;
        buf2[9] = 274;
        buf2[10] = 305;
        buf2[11] = 335;

        if ((year % 4) != 0) {
            this.day = buf1[month - 1] + day;

            if (this.day > 79) {
                this.day = this.day - 79;
                if (this.day <= 186) {
                    switch (this.day % 31) {
                        case 0:
                            this.month = this.day / 31;
                            this.day = 31;
                            break;
                        default:
                            this.month = (this.day / 31) + 1;
                            this.day = (this.day % 31);
                            break;
                    }
                    this.year = year - 621;
                } else {
                    this.day = this.day - 186;

                    switch (this.day % 30) {
                        case 0:
                            this.month = (this.day / 30) + 6;
                            this.day = 30;
                            break;
                        default:
                            this.month = (this.day / 30) + 7;
                            this.day = (this.day % 30);
                            break;
                    }
                    this.year = year - 621;
                }
            } else {
                if ((year > 1996) && (year % 4) == 1) {
                    ld = 11;
                } else {
                    ld = 10;
                }
                this.day = this.day + ld;

                switch (this.day % 30) {
                    case 0:
                        this.month = (this.day / 30) + 9;
                        this.day = 30;
                        break;
                    default:
                        this.month = (this.day / 30) + 10;
                        this.day = (this.day % 30);
                        break;
                }
                this.year = year - 622;
            }
        } else {
            this.day = buf2[month - 1] + day;

            if (year >= 1996) {
                ld = 79;
            } else {
                ld = 80;
            }
            if (this.day > ld) {
                this.day = this.day - ld;

                if (this.day <= 186) {
                    switch (this.day % 31) {
                        case 0:
                            this.month = (this.day / 31);
                            this.day = 31;
                            break;
                        default:
                            this.month = (this.day / 31) + 1;
                            this.day = (this.day % 31);
                            break;
                    }
                    this.year = year - 621;
                } else {
                    this.day = this.day - 186;

                    switch (this.day % 30) {
                        case 0:
                            this.month = (this.day / 30) + 6;
                            this.day = 30;
                            break;
                        default:
                            this.month = (this.day / 30) + 7;
                            this.day = (this.day % 30);
                            break;
                    }
                    this.year = year - 621;
                }
            } else {
                this.day = this.day + 10;

                switch (this.day % 30) {
                    case 0:
                        this.month = (this.day / 30) + 9;
                        this.day = 30;
                        break;
                    default:
                        this.month = (this.day / 30) + 10;
                        this.day = (this.day % 30);
                        break;
                }
                this.year = year - 622;
            }

        }


    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getCustomDate() {
//        output like 1393/02/05
        final String SEPARATOR = "/";
        String yearValue = year + "";
        String monthValue;
        if (month < 10)
            monthValue = "0" + month;
        else
            monthValue = "" + month;
        String dayValue;
        if (day < 10)
            dayValue = "0" + day;
        else
            dayValue = "" + day;
        return year + SEPARATOR + month + SEPARATOR + day;
    }

    public String getCustomDate2() {
        return getCustomDate() + " " + time;
    }


}
