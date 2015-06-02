import com.google.gson.*;
import com.google.gson.reflect.*;

public class Champion {
    private String region;
    private String lastRegionName;
    private String lastRegionTitle;
    private String name;
    private String title;
    private boolean active;
    private boolean botEnabled;
    private boolean botMmEnabled;
    private boolean freeToPlay;
    private long id;
    private boolean rankedPlayEnabled;

    public String toString() {
        return "What's the champion's name? " + name
            + "\nWhat's the champion's title? " + title
            + "\nIs it inactive? " + active
            + "\nIs it botEnabled? " + botEnabled
            + "\nIs it botMmEnabled? " + botMmEnabled
            + "\nIs it freeToPlay? " + freeToPlay
            + "\nWhat is the ID? " + id
            + "\nIs it rankedPlayEnabled? " + rankedPlayEnabled;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) throws InvalidRegionException {
        InvalidRegionException.validRegionCheck(region);
        updateForRegion(region);
    }

    public String getName() {
        if (region == null) {
            updateForRegion("na");
        }
        return name;
    }

    private void updateForRegion(String region) {
        if (region == null || this.region != region) {
            this.region = region;
            RiotAPICollector collector = new RiotAPICollector(region);
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(
                    collector.collectJsonStringForSpecificChampionStaticData(id)
                    , JsonObject.class);
            this.name = json.get("name").toString();
            this.title = json.get("title").toString();
        }
    }

    public String getTitle() {
        if (region == null) {
            updateForRegion("na");
        }
        return title;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (this == other) {
            return true;
        } else if (other instanceof Champion
                && this.id == ((Champion)(other)).getId()) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (int) (id>>32) + (int) (id);
    }

    public boolean isActive() {
        return active;
    }

    public boolean isBotEnabled() {
        return botEnabled;
    }

    public boolean isBotMmEnabled() {
        return botMmEnabled;
    }

    public boolean isFreeToPlay() {
        return freeToPlay;
    }

    public long getId() {
        return id;
    }

    public boolean isRankedPlayEnabled() {
        return rankedPlayEnabled;
    }
}
