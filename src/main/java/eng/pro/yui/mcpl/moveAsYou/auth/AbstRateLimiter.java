package eng.pro.yui.mcpl.moveAsYou.auth;

public abstract class AbstRateLimiter implements IRateLimiter {

    static class Limits {
        int minuteRateLimit = 60; //TODO configで宣言
        int hourRateLimit = 60;
        int dayRateLimit = 48;
    }
    static class RateRecord extends Limits{
        int minuteRequestCount = 0;
        int hourRequestCount = 0;
        int dayRequestCount = 0;
        long lastMinuteRateReset = System.currentTimeMillis();
        long lastHourRateReset = System.currentTimeMillis();
        long lastDayRateReset = System.currentTimeMillis();

        public int getMinuteRateVolumeLeft(){
            return minuteRateLimit - minuteRequestCount;
        }
        public int getHourRateVolumeLeft(){
            return hourRateLimit - hourRequestCount;
        }
        public int getDayRateVolumeLeft(){
            return dayRateLimit - dayRequestCount;
        }

        void refresh(){
            if(System.currentTimeMillis() - lastMinuteRateReset > 60*1_000){
                resetMinute();
            }
            if(System.currentTimeMillis() - lastHourRateReset > 60*60*1_000){
                resetHour();
            }
            if(System.currentTimeMillis() - lastDayRateReset > 24*60*60*1_000){
                resetDay();
            }
        }
        void incrementCounts(){
            minuteRequestCount++;
            hourRequestCount++;
            dayRequestCount++;
        }
        void resetMinute(){
            minuteRequestCount = 0;
            lastMinuteRateReset = System.currentTimeMillis();
        }
        void resetHour(){
            hourRequestCount = 0;
            lastHourRateReset = System.currentTimeMillis();
        }
        void resetDay(){
            dayRequestCount = 0;
            lastDayRateReset = System.currentTimeMillis();
        }
    }
}
