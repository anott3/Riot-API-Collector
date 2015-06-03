import java.io.IOException;
import java.lang.InterruptedException;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.lang.IllegalArgumentException;
import java.util.concurrent.TimeUnit;
import com.google.gson.*;
import com.google.gson.reflect.*;
//NO SUPPORT FOR CURRENTGAME API YET


/**
  * Java class that uses Riot API to collect match data
  * and store it in a separate file.
  *
  * @author Adithya Nott
  * @version 1.0
  */
public class RiotAPICollector {
    private static String apiKey;

    private String region;
    private static boolean validAPIKey = false;
    private static Gson gson = new Gson();
    private static final String championVersion = "v1.2";
    private static final String gameVersion = "v1.3";
    private static final String staticDataVersion = "v1.2";
    private static final String summonerVersion = "v1.4";


    private static final String invalidURLMessage = "The URL isn't right, which"
            + " caused an IOException";
    private static final String noFileMessage = "You need to create an "
        + "apikey.txt file in the same directory that contains a Riot API key";
    private static final String invalidKeyMessage = "The API key is invalid.";
    private static final String interalServerErrorMessage = "Riot API server "
        + "experienced an interal server error that prevented it from "
        + "fulfilling the request.";
    private static final String serverUnavailableMessage = "Riot API server is"
        + "currently unavailable to handle the request. Try again later.";

    /**
     * No-args constructor that uses the API Key stored in apikey.txt
     * The default region is North America
     */
    public RiotAPICollector() throws IOException {
        this("na");
    }

    /**
     * Takes in a String that represents the region to collect data from
     * for this instance of RiotAPICollector
     */
    public RiotAPICollector(String region) throws IOException {
        this.region = region;
        if (apiKey == null) {
            try {
                java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(
                        new java.io.FileInputStream(
                            new java.io.File("apikey.txt")), "US-ASCII"));
                apiKey = reader.readLine();
                reader.close();
            } catch (IOException e) {
                throw new NoAPIFileException();
            }
        }
        validAPICheck();
    }

    public Champion[] getFreeRotation() throws IOException {
        JsonObject freeweek = gson.fromJson(
            collectJsonStringForFreeRotation(), JsonObject.class);
        Champion[] champions =
            gson.fromJson(freeweek.get("champions"), Champion[].class);
        try {
            for (Champion champ : champions) {
                champ.setRegion(region);
            }
        } catch (InvalidRegionException e) {
            return null;
        }
        return champions;
    }

    public Summoner[] getSummoners(String ... args) throws IOException {
        return getSummonerNameHelper(0, args);
    }

    private Summoner[] getSummonerNameHelper(int offset, String ... args) throws IOException {
        Summoner[] summoners = new Summoner[args.length - offset];
        if (args.length - offset > 10) {
            Summoner[] moreSummoners = getSummonerNameHelper(0,
                    args[offset++], args[offset++],
                    args[offset++], args[offset++],
                    args[offset++], args[offset++],
                    args[offset++], args[offset++],
                    args[offset++], args[offset++]);

            Summoner[] otherSummoners = getSummonerNameHelper(offset, args);
            for (int i = 0; i < moreSummoners.length; i++) {
                summoners[i] = moreSummoners[i];
            }
            for (int i = 0; i < otherSummoners.length; i++) {
                summoners[i + moreSummoners.length] = otherSummoners[i];
            }
        } else {
            JsonObject summonerJson = gson.fromJson(
                    collectJsonStringForSummonersByName(args),
                    JsonObject.class);
            for (int i = 0; i < args.length - offset; i++) {
                summoners[i] =
                        gson.fromJson(summonerJson.get(
                                args[i + offset].toLowerCase()
                                        .replaceAll("\\s","")), Summoner.class);
            }
        }
        return summoners;
    }

    public Summoner[] getSummoners(long ... summonerIds) throws IOException {
       return getSummonerIdHelper(0, summonerIds);
    }

    private Summoner[] getSummonerIdHelper(int offset, long ... args) throws IOException {
        Summoner[] summoners = new Summoner[args.length - offset];
        if (args.length - offset > 10) {
            Summoner[] moreSummoners = getSummonerIdHelper(0,
                    args[offset++], args[offset++],
                    args[offset++], args[offset++],
                    args[offset++], args[offset++],
                    args[offset++], args[offset++],
                    args[offset++], args[offset++]);
            Summoner[] otherSummoners = getSummonerIdHelper(offset, args);
            for (int i = 0; i < moreSummoners.length; i++) {
                summoners[i] = moreSummoners[i];
            }
            for (int i = 0; i < otherSummoners.length; i++) {
                summoners[i + moreSummoners.length] = otherSummoners[i];
            }
        } else {
            JsonObject summonerJson = gson.fromJson(
                    collectJsonStringForSummonersById(args),
                    JsonObject.class);
            for (int i = 0; i < args.length - offset; i++) {
                summoners[i] =
                        gson.fromJson(
                            summonerJson.get("" + args[i + offset]),
                                Summoner.class);
            }
        }
        return summoners;
    }

    public String collectJsonStringForChampionList() throws IOException {
        return getJsonStringFromURL("https://" + region + ".api.pvp.net"
                + "/api/lol/" + region + "/" + championVersion
                + "/champion?api_key=" + apiKey);
    }

    public String collectJsonStringForFreeRotation() throws IOException {
        return getJsonStringFromURL("https://" + region + ".api.pvp.net/api/"
                + "lol/" + region + "/" + championVersion + "/champion?"
                + "freeToPlay=true&api_key=" + apiKey);
    }

    public String collectJsonStringForSpecificChampion(long championID)
        throws IOException {
        return getJsonStringFromURL("https://" + region + ".api.pvp.net/api/lol"
                + "/" + region + "/" + championVersion + "/champion/"
                + championID + "?api_key=" + apiKey);
    }

    public String collectJsonStringForFeaturedGames() throws IOException {
        return getJsonStringFromURL("https://" + region + ".api.pvp.net/"
            +"observer-mode/rest/featured?api_key=" + apiKey);
    }

    public String collectJsonStringForChampionsStaticData() throws IOException {
        return getJsonStringFromURL("https://global.api.pvp.net/api/lol/"
            + "static-data/" + region + "/" + staticDataVersion
            + "/champion?api_key=" + apiKey);
    }

    public String collectJsonStringForSpecificChampionStaticData
        (long championID) throws IOException {
        return getJsonStringFromURL("https://global.api.pvp.net/api/lol/"
                + "static-data/" + region + "/" + staticDataVersion
                + "/champion/" + championID + "?api_key=" + apiKey);
    }

    public String collectJsonStringForSummonersByName(String... args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("The necessary input is a "
                + "non-zero group of summoner names");
        }
        String s = "https://" + region + ".api.pvp.net/api/lol/" + region + "/"
            + summonerVersion + "/summoner/by-name/";
        for (String summoner : args) {
            s += summoner.replaceAll("\\s","") + ",";
        }
        s += "?api_key=" + apiKey;
        return getJsonStringFromURL(s);
    }

    public String collectJsonStringForSummonersById(long ... args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("The necessary input is a "
                + "non-zero group of summoner IDs.");
        }
        String s = "https://" + region + ".api.pvp.net/api/lol/" + region + "/"
            + summonerVersion + "/summoner/";
        for (long summoner : args) {
            s += summoner + ",";
        }
        s += "?api_key=" + apiKey;
        return getJsonStringFromURL(s);
    }

    /**
     * Used to collect a String representation of the JSON containing all the
     * games for a given summoner
     *
     * @param summonerID    unique identifier for a given summoner
     * @return  JSON String for games from the given Summoner ID
     */
    public String collectJsonStringForGamesBySummonerID(long summonerID) throws IOException {
        return getJsonStringFromURL("https://" + region + ".api.pvp.net/api/lol"
                + "/na/" + gameVersion + "/game/by-summoner/" + summonerID
                +"/recent?api_key=" + apiKey);
    }

    /**
     * Private helper method that checks whether or not the API key is valid
     * by calling static data from the API. When already determined to be valid
     * in a previous call, runs in O(1) time.
     */
    private static void validAPICheck() throws IOException {
        if (validAPIKey) { //we initially assume it is invalid when first call
            if (apiKey != null) { //reexamines the assumption
                //check url
                JsonObject azir = gson.fromJson(getJsonStringFromURL(
                    "https://global.api.pvp.net/api/lol/static-data/na/v1.2/"
                    + "champion/268?api_key=" + apiKey), JsonObject.class);
                if (azir.get("key") != null) {
                    validAPIKey = true;
                } else {
                    throw new InvalidAPIKeyException();
                }
            } else {
                throw new NoAPIFileException();
            }
        }
    }

    /**
     * Private helper method that returns the JSON String from the Riot API URL
     * parameter that is given.
     * @param url   Riot API URL to get JSON String from
     * @return JSON String from the URL visited
     */
    private static String getJsonStringFromURL(String url)
            throws IOException, InvalidURLException, InvalidAPIKeyException {
        try {
            java.io.InputStream is = new java.net.URL(url).openStream();
            try {
                java.io.BufferedReader rd = new java.io.BufferedReader(
                        new java.io.InputStreamReader(
                                is, java.nio.charset.Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }
                return sb.toString();
            } catch(IOException e) {
                throw new InvalidURLException();
            } finally {
                is.close();
            }
        } catch(IOException e) {
            java.net.HttpURLConnection http =  ((java.net.HttpURLConnection)
                    (new java.net.URL(url).openConnection()));
            int responseCode = http.getResponseCode();
            if (responseCode == 400) { //Bad Request
                throw new InvalidURLException();
            } else if (responseCode == 401) { //Unauthorized
                throw new InvalidAPIKeyException();
            } else if (responseCode == 404) { //Not Found
                throw new InvalidURLException();
            } else if (responseCode == 429) { //Rate Limit Exceeded
                try {
                    Thread.sleep(1000 * Integer.parseInt(
                            http.getHeaderField(2)) + 500);
                } catch (InterruptedException i) {

                }
            } else if (responseCode == 500) { //Internal Server Error
                throw new InteralServerErrorException();
            } else if (responseCode == 503) { //Service Unavailable
                throw new ServerUnavailableException();
            }
            throw new UnexpectedResponseCodeException("The response code "
                + "received was: " + responseCode);

        }
    }

    private static class InvalidURLException extends RuntimeException {
        public InvalidURLException() {
            super(invalidURLMessage);
        }
    }

    private static class NoAPIFileException extends RuntimeException {
        public NoAPIFileException() {
            super(noFileMessage);
        }
    }

    private static class InvalidAPIKeyException extends RuntimeException {
        public InvalidAPIKeyException() {
            super(invalidKeyMessage);
        }
    }

    private static class InteralServerErrorException extends RuntimeException {
        public InteralServerErrorException() { super(interalServerErrorMessage); }
    }

    private static class ServerUnavailableException extends RuntimeException {
        public ServerUnavailableException() {
            super(serverUnavailableMessage);
        }
    }

    private static class UnexpectedResponseCodeException extends RuntimeException {
        public UnexpectedResponseCodeException(String message) {
            super(message);
        }
    }
}
