public class Summoner {
    private long id;
    private String name;
    private int profileIconId;
    private long revisionDate;
    private long summonerLevel;

    public String toString() {
        return "What is the summoner ID? " + id
            + "\nWhat is the profileIconId? " + profileIconId
            + "\nWhat is the revisionDate? " + revisionDate
            + "\nWhat is the summonerLevel? " + summonerLevel;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getProfileIconId() {
        return profileIconId;
    }

    public long getRevisionDate() {
        return revisionDate;
    }

    public long getSummonerLevel() {
        return summonerLevel;
    }
}
