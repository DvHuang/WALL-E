package time_day;

import android.support.annotation.VisibleForTesting;

/**
 * Created by JTdavy on 2016/4/18.
 */
public class DayInfo {


        private int id;
        private String date;
        private String period;
        public String work;
        public String tips;
        public String Degree_of_completion;


        public DayInfo(int id,
                       String date,
                       String work,
                       String tips,
                       String period,
                       String Degree_of_completion
                      ) {
            this.id = id;
            this.date = date;
            this.work = work;
            this.tips = tips;
            this.period = period;
            this.Degree_of_completion = Degree_of_completion;
        }

        @Override
        public String toString() {
            return "id=" + id + ", data=" + date+ ", work=" + work + ", tips=" + tips + ", Degree=" + Degree_of_completion  ;
        }


        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }


        public String getdate() {
            return date;
        }
        public void setdate(String date) {
            this.date = date;
        }


        public String getWork() {
        return work;
        }
        public void setWork(String work) {
        this.work = work;
        }


        public String getTips() {
        return tips;
        }
        public void setTips(String tips) {
        this.tips = tips;
        }


        public String getPeriod() {
            return period;
        }
        public void setPeriod(String period) {
            this.period = period;
        }


        public String getDegree_of_completion() {
        return Degree_of_completion;
        }
        public void setDegree_of_completion(String Degree_of_completion) {
        this.Degree_of_completion = Degree_of_completion;
        }
}
