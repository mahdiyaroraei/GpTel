package ir.parhoonco.traccar.core;

import java.security.MessageDigest;

public final class Secret {

    private Secret() {

    }

    public static String getUP(String i) throws Exception {

        byte[] bi = i.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] d = md.digest(bi);
        String h = d.toString();
        h = new StringBuilder(h).reverse().toString();
        d = md.digest(h.getBytes("UTF-8"));
        h = d.toString();
        return h.substring(h.length() - 5, h.length() - 1) + " " + h.substring(0, 4);
    }

}
