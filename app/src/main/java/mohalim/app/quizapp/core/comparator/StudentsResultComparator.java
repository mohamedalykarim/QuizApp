package mohalim.app.quizapp.core.comparator;

import java.util.Comparator;

import mohalim.app.quizapp.core.models.StatisticsStudentsArrangeItem;

public class StudentsResultComparator implements Comparator<StatisticsStudentsArrangeItem> {
    @Override
    public int compare(StatisticsStudentsArrangeItem o1, StatisticsStudentsArrangeItem o2) {

        if (o1.getGrade() < o2.getGrade()){
            return 1;
        }else {
            return -1;
        }
    }
}
