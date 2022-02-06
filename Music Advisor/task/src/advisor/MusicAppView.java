package advisor;

import objects.MusicObj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static advisor.MusicApp.getPages;

public class MusicAppView {
    static int page;
    public static void print(List<? extends MusicObj> arr) {
        int pagesCount = (arr.size() ) / getPages();
        if(page>pagesCount){
            System.out.println("No more pages.");
            page=pagesCount;
            return ;
        }
        if (arr.size() > 0) {
            for(int i = (page - 1) * getPages();i<page  * getPages();i++){
                System.out.println();
                System.out.println(arr.get(i));
            }
            System.out.println("---PAGE " + page + " OF " + pagesCount + "---");
        }
    }
}
