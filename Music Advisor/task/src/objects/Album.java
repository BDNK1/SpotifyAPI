package objects;

import java.util.ArrayList;
import java.util.List;

public class Album extends MusicObj{
    List<String> performer;
    String link;

    public Album(String name,List<String> performer,String link) {
        super(name);
        this.performer = performer;
        this.link = link;
    }

    @Override
    public String toString() {
        return super.getName() +"\n"
                 + performer +
                "\n" + link + '\n';
    }
}
