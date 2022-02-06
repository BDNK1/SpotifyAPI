package objects;

public class Playlist extends MusicObj{
    String link;

    public Playlist(String name,String link) {
        super(name);
        this.link = link;
    }

    @Override
    public String toString() {
        return super.getName()+"\n"+link+"\n";
    }
}
