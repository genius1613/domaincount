package vn.mvcorp.common;

/**
 * Created by tienbm on 03/10/2014.
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;


import org.apache.commons.lang.StringUtils;

public class TableUtil {

    public static final ByteBuffer YES_VAL = ByteBuffer.wrap(new byte[]{'y'});

    public static String reverseUrl(String urlString)
            throws MalformedURLException {
        return reverseUrl(new URL(urlString));
    }

    public static String reverseUrl(URL url) {
        String host = url.getHost();
        String file = url.getFile();
        String protocol = url.getProtocol();
        int port = url.getPort();

        StringBuilder buf = new StringBuilder();

    /* reverse host */
        reverseAppendSplits(host, buf);

    /* add protocol */
        buf.append(':');
        buf.append(protocol);

    /* add port if necessary */
        if (port != -1) {
            buf.append(':');
            buf.append(port);
        }

    /* add path */
        if (file.length() > 0 && '/' != file.charAt(0)) {
            buf.append('/');
        }
        buf.append(file);

        return buf.toString();
    }

    public static String unreverseUrl(String reversedUrl) {
        StringBuilder buf = new StringBuilder(reversedUrl.length() + 2);

        int pathBegin = reversedUrl.indexOf('/');
        if (pathBegin == -1)
            pathBegin = reversedUrl.length();
        String sub = reversedUrl.substring(0, pathBegin);

        String[] splits = StringUtils.splitPreserveAllTokens(sub, ':'); // {<reversed host>, <port>, <protocol>}

        buf.append(splits[1]); // add protocol
        buf.append("://");
        reverseAppendSplits(splits[0], buf); // splits[0] is reversed
        // host
        if (splits.length == 3) { // has a port
            buf.append(':');
            buf.append(splits[2]);
        }
        buf.append(reversedUrl.substring(pathBegin));
        return buf.toString();
    }

    /**
     * Given a reversed url, returns the reversed host E.g
     * "com.foo.bar:http:8983/to/index.html?a=b" -> "com.foo.bar"
     *
     * @param reversedUrl Reversed url
     * @return Reversed host
     */
    public static String getReversedHost(String reversedUrl) {
        return reversedUrl.substring(0, reversedUrl.indexOf(':'));
    }

    private static void reverseAppendSplits(String string, StringBuilder buf) {
        String[] splits = StringUtils.split(string, '.');
        if (splits.length > 0) {
            for (int i = splits.length - 1; i > 0; i--) {
                buf.append(splits[i]);
                buf.append('.');
            }
            buf.append(splits[0]);
        } else {
            buf.append(string);
        }
    }

    public static String reverseHost(String hostName) {
        StringBuilder buf = new StringBuilder();
        reverseAppendSplits(hostName, buf);
        return buf.toString();

    }

    public static String unreverseHost(String reversedHostName) {
        return reverseHost(reversedHostName); // Reversible
    }


}
