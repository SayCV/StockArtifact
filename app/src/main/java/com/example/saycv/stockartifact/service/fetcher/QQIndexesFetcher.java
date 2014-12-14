package com.example.saycv.stockartifact.service.fetcher;

import android.content.Context;

import com.example.saycv.stockartifact.R;
import com.example.saycv.stockartifact.model.Index;
import com.example.saycv.stockartifact.service.exception.DownloadException;
import com.example.saycv.stockartifact.service.exception.ParseException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.saycv.logger.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.saycv.stockartifact.service.fetcher.Utils.rounded;

public class QQIndexesFetcher extends BaseIndexesFetcher {
    public static final String TAG = "QQIndexesFetcher";
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm";
    private Context context;

    public QQIndexesFetcher(Context context) {
        this.context = context;
    }

    @Override
    public List<Index> fetch() throws DownloadException, ParseException {
        List<Index> indexes = new ArrayList<Index>();
        indexes.add(getChinaIndex("sh", "000001"));
        indexes.add(getChinaIndex("sz", "399001"));
        //indexes.addAll(getWorldIndexes());
        return indexes;
    }

    private Index getChinaIndex(String market, String code) throws ParseException, DownloadException {
        try {
            Index index = new Index();
            HttpGet req = new HttpGet(getChinaIndexURL(market, code));
            //req.setHeader("Referer", "http://QQ.on.cc/");

            HttpResponse resp = getClient().execute(req);
            String content = EntityUtils.toString(resp.getEntity(), "utf8");
            //JSONObject json = preprocessJson(content);
            String[] contentArray = getContent(content);

            index.setName(contentArray[1]);
            index.setCode(code);
            index.setTime(contentArray[30].substring(0, 8));

            index.setLastDayClose(contentArray[4]);
            //index.setLastDayHigh(contentArray[1]);
            //index.setLastDayLow(contentArray[1]);
            index.setDayHigh(contentArray[33]);
            index.setDayLow(contentArray[34]);
            index.setDayOpen(contentArray[5]);
            index.setDayClose(contentArray[3]);
            index.setDayVolume(contentArray[6]);
            index.setDayMoney(contentArray[37]);
            index.setDayRange(contentArray[31]);
            index.setDayRangePercent(contentArray[32]);
            index.setDaySwing(contentArray[43]);



            return index;
        } catch (org.apache.http.ParseException pe) {
            throw new ParseException("error parsing http data", pe);
        } /*catch (JSONException je) {
            throw new ParseException("error parsing http data", je);
        }*/ catch (IOException ie) {
            //throw new DownloadException("error parsing http data", ie);
            Log.e(TAG, "error parsing http data: " + ie.getMessage());
            return null;
        } /*catch (java.text.ParseException e) {
            throw new ParseException("error parsing json data", e);
        }*/
    }

    private String getChinaIndexURL(String market, String code) {
        return String.format("http://qt.gtimg.cn/q=%s%s", market, code);
    }

    private String getWorldIndexURL() {
        return "http://qt.gtimg.cn/q=%s%s";
    }

    private JSONObject preprocessJson(String content) throws JSONException {
        int pos = content.indexOf('{');
        String result = StringUtils.substring(content, pos);
        JSONObject json = new JSONObject(result);
        return json;
    }

    private List<Index> getWorldIndexes() throws ParseException, DownloadException {
        try {
            HttpGet req = new HttpGet(getWorldIndexURL());
            //req.setHeader("Referer", "http://QQ.on.cc/");

            HttpResponse resp = getClient().execute(req);
            String content = EntityUtils.toString(resp.getEntity(), "Big5");
            return getWorldIndexesFromJson(content);
        } catch (org.apache.http.ParseException pe) {
            throw new ParseException("error parsing http data", pe);
        } catch (JSONException je) {
            throw new ParseException("error parsing http data", je);
        } catch (IOException ie) {
            throw new DownloadException("error parsing http data", ie);
        }
    }

    private List<Index> getWorldIndexesFromJson(String content) throws JSONException {
        List<Index> indexes = new ArrayList<Index>();
        int start = content.indexOf('{');
        while (start > 0) {
            int end = content.indexOf(";", start);
            String result = StringUtils.substring(content, start, end);
            JSONObject json = new JSONObject(result);
            String name = json.getString("Name");
            String value = json.getString("Point");
            String diff = json.getString("Difference");

            Index index = new Index();
            index.setName(name);
            index.setValue(new BigDecimal(value));

            if (diff != null && !StringUtils.equalsIgnoreCase(diff, "null")) {
                index.setChange(new BigDecimal(diff));

                double changePercent = index.getValue().doubleValue() / (index.getValue().doubleValue() - index.getChange().doubleValue()) - 1;
                index.setChangePercent(new BigDecimal(changePercent));
            }

            indexes.add(index);
            start = content.indexOf('{', end);
        }
        return indexes;
    }

    /**
     * @return the context
     */
    public Context getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(Context context) {
        this.context = context;
    }


    private String[] getContent(String content) {
        //"处理腾讯股票数据接口信息"
        String results = new String();
        Pattern pattern = Pattern.compile("\"(.*)\"");
        Matcher matcher = pattern.matcher(content);
        while(matcher.find())
        {
            results= matcher.group() ;
            Log.e(TAG, results); // 使用Android的Logcat查看运行结果，直接使用e标志红色的为结果。

        }
        return results.split("~");
    }
}
