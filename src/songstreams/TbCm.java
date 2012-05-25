//    jDownloader - Downloadmanager
//    Copyright (C) 2009  JD-Team support@jdownloader.org
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package songstreams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class TbCm {
    
    static class Info {
        public String link;
        public long   size;
        public int    fmt;
        public String desc;
    }

    static public final Pattern                 YT_FILENAME_PATTERN = Pattern.compile("<meta name=\"title\" content=\"(.*?)\">", Pattern.CASE_INSENSITIVE);
    private static final Pattern 				VIDEO_ID 			= Pattern.compile("watch\\?v=([\\w_\\-]+)");
    private static final Pattern				VIDEO_FILE			= Pattern.compile("&title=([^&$]+)");
    private static final Pattern				HTML5_FMT_MAP		= Pattern.compile("\"html5_fmt_map\": \\[(.*?)\\]");
    private static final Pattern				NEW_HTML5_FMT_MAP	= Pattern.compile("\"url_encoded_fmt_stream_map\": \"(.*?)\"");
    private static final Pattern				FMT_MAP_COLUMN		= Pattern.compile("\\{(.*?)\\}");
    private static final Pattern				FMT_HIT_URL			= Pattern.compile("url\": \"(http:.*?)\"");
    private static final Pattern				NEW_FMT_HIT_URL		= Pattern.compile("url=(http.*?)\\&");
    private static final Pattern				FMT_HIT_FMT			= Pattern.compile("itag\": (\\d+)");
    private static final Pattern				NEW_FMT_HIT_FMT		= Pattern.compile("itag=(\\d+)");
    private static final Pattern				FMT_HIT_QUALITY		= Pattern.compile("quality\": \"(.*?)\"");
    private static final Pattern				NEW_FMT_HIT_QUALITY	= Pattern.compile("quality=(.*?)&");
    private static final Pattern				FMT_LIST			= Pattern.compile("&fmt_list=(.+?)&");
    private static final Pattern				NEW_FMT_LIST		= Pattern.compile("\"fmt_list\":\\s+\"(.+?)\",");
    private static final Pattern				FMT_LIST_MAP		= Pattern.compile("(\\d+)/(\\d+x\\d+)/\\d+/\\d+/\\d+,");
    private static final Pattern				FALLBACK			= Pattern.compile("url%3D(.*?)($|%2C)");
    private static final Pattern				FALLBACK_URL		= Pattern.compile("(.*?)%26");
    private static final Pattern				FALLBACK_QUALITY	= Pattern.compile("%26quality%3D(.*?)%");
    
    public HashMap<Integer, String[]> decryptIt(final URL param) throws Exception {

        String parameter = param.toString().replace("watch#!v", "watch?v");
        parameter = parameter.replaceFirst("(verify_age\\?next_url=\\/?)", "");
        parameter = parameter.replaceFirst("(%3Fv%3D)", "?v=");
        parameter = parameter.replaceFirst("(watch\\?.*?v)", "watch?v");
        parameter = parameter.replaceFirst("/embed/", "/watch?v=");
        parameter = parameter.replaceFirst("https", "http");

        return getLinks(parameter);
    }

    public HashMap<Integer, String[]> getLinks(final String video) throws Exception {
       
        /* this cookie makes html5 available and skip controversy check */
    	HttpURLConnection conn = (HttpURLConnection) new URL(video).openConnection();
    	conn.setRequestProperty("Cookie", "f2=40100000");
    	conn.setRequestProperty("User-Agent", "Wget/1.12");
    	conn.setRequestMethod("GET");
    	StringBuffer page = new StringBuffer();
    	InputStreamReader isr = new InputStreamReader(conn.getInputStream());
    	BufferedReader bufR = new BufferedReader(isr);
    	String line;
    	while((line = bufR.readLine()) != null)
    		page.append(line).append("\n");
    	bufR.close();
    	String pageString = page.toString();
    	if(pageString.contains("id=\"unavailable-submessage\" class\"watch-unavailable-submessage\""))
    		return null;
    	  
        Matcher matcher = VIDEO_ID.matcher(video);
        String VIDEOID = "";
        if(matcher.find())
        	VIDEOID = matcher.group(1);
        boolean fileNameFound = false;
        String YT_FILENAME = VIDEOID;
        matcher = VIDEO_FILE.matcher(pageString);
        if(pageString.contains("&title=")) {
        	matcher.find();
        	YT_FILENAME = URLDecoder.decode(matcher.group(1).replaceAll("\\+", " ").trim(), conn.getContentEncoding());
        }
        final String url = conn.getURL().toString();
        boolean ythack = false;
        if (url != null && !url.equals(video)) {
        	if(url.toLowerCase(Locale.ENGLISH).indexOf("youtube.com/index?ytsession=") != -1 || url.toLowerCase(Locale.ENGLISH).indexOf("youtube.com/verify_age?next_url=") != -1) {
        		ythack = true;
        		URL info = new URL("http://www.youtube.com/get_video_info?video_id=" + VIDEOID);
        		HttpURLConnection infoConn = (HttpURLConnection) info.openConnection();
        		infoConn.setRequestProperty("Cookie", "f2=40100000");
            	infoConn.setRequestProperty("User-Agent", "Wget/1.12");
            	page = new StringBuffer();
            	isr = new InputStreamReader(infoConn.getInputStream());
            	bufR = new BufferedReader(isr);
            	while((line = bufR.readLine()) != null)
            		page.append(line).append("\n");
            	bufR.close();
            	pageString = page.toString();
            	if(pageString.contains("&title=") && !fileNameFound) {
            		matcher = VIDEO_FILE.matcher(pageString);
            		matcher.find();
            		YT_FILENAME = URLDecoder.decode(matcher.group(1).replaceAll("\\+", " ").trim(), conn.getContentEncoding());
            		fileNameFound = true;
            	}
        	} else if(url.toLowerCase(Locale.ENGLISH).indexOf("google.com/accounts/servicelogin?") != -1) {
        		return null;
        	}
        }
        /* html5_fmt_map */
        matcher = YT_FILENAME_PATTERN.matcher(pageString);
        matcher.find();
        if(matcher.groupCount() != 0 && !fileNameFound) {
        	YT_FILENAME = URLDecoder.decode(matcher.group(1).trim(), "UTF-8");
        	fileNameFound = true;
        }
        final HashMap<Integer, String[]> links = new HashMap<Integer, String[]>();
        matcher = HTML5_FMT_MAP.matcher(pageString);
        String html5_fmt_map;
        Vector<String> fmtHitsVector = new Vector<String>();
        if (matcher.find()) {
        	html5_fmt_map = matcher.group(1);
        	matcher = FMT_MAP_COLUMN.matcher(html5_fmt_map);
        	while(matcher.find())
        		fmtHitsVector.add(matcher.group(1));
        	for(String hit : fmtHitsVector) {
        		matcher = FMT_HIT_URL.matcher(hit);
        		matcher.find();
        		String hitURL = matcher.group(1);
        		matcher = FMT_HIT_FMT.matcher(hit);
        		matcher.find();
        		String hitFMT = matcher.group(1);
        		matcher = FMT_HIT_QUALITY.matcher(hit);
        		String hitQ = matcher.group(1);
        		if(hitURL != null && hitFMT != null && hitQ != null) {
        			hitURL = unescape(hitURL.replaceAll("\\\\/", "/"));
        			links.put(Integer.parseInt(hitFMT), new String[] { URLDecoder.decode(hitURL, "UTF-8"), hitQ});
        		}
        	}
        }
       else {
    		matcher = NEW_HTML5_FMT_MAP.matcher(pageString);
    		if(matcher.find()) {
    			html5_fmt_map = matcher.group(1);
    			matcher = FMT_MAP_COLUMN.matcher(html5_fmt_map);
            	while(matcher.find())
            		fmtHitsVector.add(matcher.group(1));
            	if(fmtHitsVector.isEmpty())
            		fmtHitsVector.addAll(Arrays.asList(html5_fmt_map.split(",")));
	        	for(String hit : fmtHitsVector) {
	        		hit = unescape(hit);
	        		matcher = NEW_FMT_HIT_URL.matcher(hit);
	        		matcher.find();
	        		String hitURL = matcher.group(1);
	        		matcher = NEW_FMT_HIT_FMT.matcher(hit);
	        		matcher.find();
	        		String hitFMT = matcher.group(1);
	        		matcher = NEW_FMT_HIT_QUALITY.matcher(hit);
	        		matcher.find();
	        		String hitQ = matcher.group(1);
	        		if (hitURL != null && hitFMT != null && hitQ != null) {
                        hitURL = unescape(hitURL.replaceAll("\\\\/", "/"));
                        links.put(Integer.parseInt(hitFMT), new String[] { URLDecoder.decode(hitURL, "UTF-8"), hitQ });
                    }
	        	}
    		}
    	}
        	final HashMap<String, String> fmt_list = new HashMap<String, String>();
            String fmt_list_str = "";
            if (ythack) {
            	matcher = FMT_LIST.matcher(pageString);
            	matcher.find();
                fmt_list_str = (matcher.group(1) + ",").replaceAll("%2F", "/").replaceAll("%2C", ",");
            } else {
            	matcher = NEW_FMT_LIST.matcher(pageString);
            	matcher.find();
                fmt_list_str = (matcher.group(1) + ",").replaceAll("\\\\/", "/");
            }
            matcher = FMT_LIST_MAP.matcher(fmt_list_str);
            while(matcher.find()) {
            	fmt_list.put(matcher.group(1), matcher.group(2));
            }
            if (links.size() == 0 && ythack) {
            	/* try to find fallback links */
                matcher = FALLBACK.matcher(pageString);
                while (matcher.find()) {
                	Matcher tempMatcher = FALLBACK_URL.matcher(matcher.group(1));
                    tempMatcher.find();
                	String hitUrl = tempMatcher.group(1);
                    tempMatcher = FALLBACK_QUALITY.matcher(matcher.group(1));
                    tempMatcher.find();
                    String hitQ = tempMatcher.group(1);
                    
                    if (hitUrl != null && hitQ != null) {
                        hitUrl = unescape(hitUrl.replaceAll("\\\\/", "/"));
                        links.put(Integer.parseInt(matcher.group(2)), new String[] { URLDecoder.decode(hitUrl, "UTF-8"), hitQ });
                    }
                }
            }
            for (Integer fmt : links.keySet()) {
                String fmt2 = fmt + "";
                if (fmt_list.containsKey(fmt2)) {
                    String Videoq = links.get(fmt)[1];
                    final Integer q = Integer.parseInt(fmt_list.get(fmt2).split("x")[1]);
                    if (fmt == 40) {
                        Videoq = "240p Light";
                    } else if (q > 1080) {
                        Videoq = "Original";
                    } else if (q > 720) {
                        Videoq = "1080p";
                    } else if (q > 576) {
                        Videoq = "720p";
                    } else if (q > 360) {
                        Videoq = "480p";
                    } else if (q > 240) {
                        Videoq = "360p";
                    } else {
                        Videoq = "240p";
                    }
                    links.get(fmt)[1] = Videoq;
                }
            }
            if (YT_FILENAME != null) {
                links.put(-1, new String[] { YT_FILENAME });
            }
            return links;
    }

    private static synchronized String unescape(final String s) {
    	char ch;
        final StringBuilder sb = new StringBuilder();
        int ii;
        int i;
        for (i = 0; i < s.length(); i++) {
            ch = s.charAt(i);
            switch (ch) {
            case '\\':
                ch = s.charAt(++i);
                StringBuilder sb2 = null;
                switch (ch) {
                case 'u':
                    /* unicode */
                    sb2 = new StringBuilder();
                    i++;
                    ii = i + 4;
                    for (; i < ii; i++) {
                        ch = s.charAt(i);
                        if (sb2.length() > 0 || ch != '0') {
                            sb2.append(ch);
                        }
                    }
                    i--;
                    sb.append((char) Long.parseLong(sb2.toString(), 16));
                    continue;
                case 'x':
                    /* normal hex coding */
                    sb2 = new StringBuilder();
                    i++;
                    ii = i + 2;
                    for (; i < ii; i++) {
                        ch = s.charAt(i);
                        sb2.append(ch);
                    }
                    i--;
                    sb.append((char) Long.parseLong(sb2.toString(), 16));
                    continue;
                default:
                    sb.append(ch);
                    continue;
                }

            }
            sb.append(ch);
        }

        return sb.toString();
    }

}
