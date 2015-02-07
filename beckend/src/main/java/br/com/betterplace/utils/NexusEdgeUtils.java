package br.com.betterplace.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.util.ByteSource;

public class NexusEdgeUtils {
    
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yyyyMMdd";
    
    public static <T> List<T> createList(T... a) {
        List<T> list = new ArrayList<T>();
        for (T b : a)
            list.add(b);
        return list;
    }

    public static <T> Set<T> createSet(T... a) {
        Set<T> set = new LinkedHashSet<T>();
        for (T b : a)
            set.add(b);
        return set;
    }

    public static Set<Integer> splitToIntegerSet(String str, String separator) {
        Set<Integer> set = null;
        if (str != null) {
            set = new LinkedHashSet<Integer>();
            for (String a : str.split(",")) {
                try {
                    set.add(Integer.valueOf(a));
                } catch (Exception ex) {
                }
            }
        }
        return set;
    }

    public static List<Integer> splitToIntegerList(String str, String separator) {
        List<Integer> list = null;
        if (str != null) {
            list = new ArrayList<Integer>();
            for (String a : str.split(",")) {
                try {
                    list.add(Integer.valueOf(a));
                } catch (Exception ex) {
                }
            }
        }
        return list;
    }

    public static Set<String> splitToStringSet(String str, String separator) {
        Set<String> set = null;
        if (str != null && !str.equals("")) {
            set = new LinkedHashSet<String>();
            for (String a : str.split(",")) {
                try {
                    set.add(a);
                } catch (Exception ex) {
                }
            }
        }
        return set;
    }

    public static List<String> splitToStringList(String str, String separator) {
        List<String> list = null;
        if (str != null && !str.equals("")) {
            list = new ArrayList<String>();
            for (String a : str.split(",")) {
                try {
                    list.add(a);
                } catch (Exception ex) {
                }
            }
        }
        return list;
    }

    public static Set<Integer> getSetOfIds(String params) {
        if (params != null && params.trim().equals(""))
            return null;
        return splitToIntegerSet(params, ",");
    }

    public static List<Integer> getIds(String params) {
        if (params != null && params.trim().equals(""))
            return null;
        return splitToIntegerList(params, ",");
    }

    public static Set<String> getSetOfStrings(String params) {
        return splitToStringSet(params, ",");
    }

    public static List<String> getListOfStrings(String params) {
        return splitToStringList(params, ",");
    }

    public static String join(@SuppressWarnings("rawtypes") Collection c, String separator) {
        if (c != null) {
            StringBuilder s = new StringBuilder();
            int i = 0;
            for (Object o : c) {
                if (i++ > 0) {
                    s.append(separator);
                }
                s.append(o);
            }
            return s.toString();
        } else {
            return null;
        }
    }

    public static class Encryption {
        public final String encrypted;
        public final String salt;
        public Encryption(String encrypted, String salt) {
            this.salt = salt;
            this.encrypted = encrypted;
        }
    }

    public static Encryption encrypt(String text) {
        
        DefaultHashService dhs = new DefaultHashService();
        ByteSource salt = dhs.getRandomNumberGenerator().nextBytes();
        
        AesCipherService acs = new AesCipherService();
        ByteSource abs = acs.encrypt(text.getBytes(), salt.getBytes());
        
        return new Encryption(abs.toBase64(), salt.toBase64());
    }

    public static String decrypt(String encrypted, String salt) {
        AesCipherService acs = new AesCipherService();
        ByteSource decrypt = acs.decrypt(Base64.decode(encrypted.getBytes()),
                Base64.decode(salt.getBytes()));
        return new String(decrypt.getBytes());
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat(YYYY_MM_DD).format(date);
    }

    public static String specificDayInPast(Integer specificDayInPast) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE) - specificDayInPast);
        return new SimpleDateFormat(YYYY_MM_DD).format(c.getTime());
    }

    public static Date parseDate(String str) {
        try {
            return new SimpleDateFormat(YYYY_MM_DD).parse(str);
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
    }

    public static String formatDatetime(Date date) {
        return new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(date);
    }

    public static Date parseDateTime(String str) {
        try {
            return new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).parse(str);
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
    }

    public static String getRange(int id, int step) {
        int x = ((id - 1) / step);
        return (x * step + 1) + "-" + ((x + 1) * step);
    }

    public static String regecaUrl(String url, String keyword, String value) {
        if(url.contains(keyword)) {
            if(value != null) {
                value = value.toLowerCase()
                            .replaceAll("^\\s+|\\s+$", "").trim()
                            .replaceAll("[áàãâ]", "a")
                            .replaceAll("[éèẽê]", "e")
                            .replaceAll("[íìĩî]", "i")
                            .replaceAll("[óòõô]", "o")
                            .replaceAll("[úùũû]", "u")
                            .replaceAll("[ç]", "c")
                            .replaceAll("\\s", "_")
                            .replaceAll("[^a-z0-9_|:]", "");
            } else
                value = "";
            return url.replaceAll(keyword, value);
        } else
            return url;
    }
    
    public static String regecaUrl(String url, String keyword, Integer value) {
        return regecaUrl(url, keyword,
                value != null ? value.toString() : "");
    }
    
    public static String getStackTrace(Throwable ex) {
        StringBuilder result = new StringBuilder();
        result.append( ex.toString() );
        String NEW_LINE = System.getProperty( "line.separator" );
        result.append( NEW_LINE );
        for (StackTraceElement element : ex.getStackTrace() ){
          result.append( "    " );
          result.append( element );
          result.append( NEW_LINE );
        }
        return result.toString();
    }
    
    public static List<String[]> getAnchorUrls(String codeHTML) {
        
        List<String[]> findedTags=new ArrayList<String[]>();
        Matcher regexMatcher;
        String regExp = "<a.*?href=(\"|')(.*?)(\"|')[^>]*?>";
        
        Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        regexMatcher = pattern.matcher( codeHTML );
        
        while (regexMatcher.find()){
            String[] hrefTag = new String[2]; 
            hrefTag[0]=regexMatcher.group();
            hrefTag[1]=regexMatcher.group(2);
            findedTags.add(hrefTag);
        }
        
        return findedTags;
    }

    public static String[] splitFragment(String url) {
        if (url == null) {
            return null;
        }
        String[] out = new String[2];
        if (url.indexOf("#") > -1) {
            out[0] = url.substring(0, url.indexOf("#"));
            out[1] = url.substring(url.indexOf("#"));
        } else {
            out[0] = url;
        }
        return out;
    }
}