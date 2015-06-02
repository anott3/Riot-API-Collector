public class InvalidRegionException extends Exception {
    public InvalidRegionException() {
        super("The region parameter is not valid. Valid parameters are the "
            + "following: \"br\" \"eune\" \"euw\" \"kr\" \"lan\" \"las\" \"na\""
            + " \"oce\" \"pbe\" \"ru\" \"tr\"");
    }

    public static void validRegionCheck(String region)
            throws InvalidRegionException {
        if (!region.equals("br") && !region.equals("eune")
            && !region.equals("euw") && !region.equals("kr")
            && !region.equals("lan") && !region.equals("las")
            && !region.equals("na") && !region.equals("oce")
            && !region.equals("pbe") && !region.equals("ru")
            && !region.equals("tr")) {
            throw new InvalidRegionException();
        }
    }
}

/*
System.out.println("br is: "
                        + "br".hashCode() + "\neune is: " + "eune".hashCode()
                        + "\neuw is: " + "euw".hashCode() + "\nkr is: "
                        + "kr".hashCode() + "\nlan is: " + "lan".hashCode()
                        + "\nlas is: " + "las".hashCode() + "\nna is: "
                        + "na".hashCode()+ "\noce is: " + "oce".hashCode()
                        + "\npbe is: " + "pbe".hashCode() + "\nru is: "
                        + "ru".hashCode() + "\ntr is: " + "tr".hashCode());
 */