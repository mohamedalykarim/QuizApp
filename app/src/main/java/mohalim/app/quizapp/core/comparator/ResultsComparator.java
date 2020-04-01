package mohalim.app.quizapp.core.comparator;


import java.util.Comparator;

import mohalim.app.quizapp.core.models.ResultItem;

public class ResultsComparator implements Comparator<ResultItem> {
    @Override
    public int compare(ResultItem o1, ResultItem o2) {
        if(o1.getResultScore() < o2.getResultScore()){
            return 1;
        }else {
            return -1;
        }
    }
}
