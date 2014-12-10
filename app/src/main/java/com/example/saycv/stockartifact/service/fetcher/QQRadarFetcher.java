package com.example.saycv.stockartifact.service.fetcher;

import static com.example.saycv.stockartifact.service.fetcher.Utils.rounded;
import com.example.saycv.stockartifact.R;
import com.example.saycv.stockartifact.model.Radar;
import com.example.saycv.stockartifact.service.exception.DownloadException;
import com.example.saycv.stockartifact.service.exception.ParseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

public class QQRadarFetcher extends BaseRadarFetcher {
    public static final String TAG = "QQRadarFetcher";
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm";
    private Context context;
    private boolean initDone = false;

    public QQRadarFetcher(Context context) {
        this.context = context;
    }

    @Override
    public List<Radar> fetch() throws DownloadException, ParseException {
        List<Radar> radar = new ArrayList<Radar>();
        Date currentDate = new Date(System.currentTimeMillis());
        Time currentTime = new Time();
        currentTime.setToNow();

        Calendar cal = Calendar.getInstance();
        int millisecond = cal.get(Calendar.MILLISECOND);
        int second = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
        //12 hour format
        int hour = cal.get(Calendar.HOUR);
        //24 hour format
        int hourofday = cal.get(Calendar.HOUR_OF_DAY);

        int dayofyear = cal.get(Calendar.DAY_OF_YEAR);
        int year = cal.get(Calendar.YEAR);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        int dayofmonth = cal.get(Calendar.DAY_OF_MONTH);

        boolean debug = true;
        if(debug || ( (hourofday == 9 && minute >=24) || hourofday > 9 && hour <15) || (hourofday == 15 && minute <= 1)) {
           if(initDone==false) {
               Log.d(TAG, "get all begin");
               radar.addAll(getChinaRadar());
               Log.d(TAG, "get all end");
               initDone = true;
           }
            Log.d(TAG, "get latest");
            radar.addAll(getChinaRadar());
        } else {
            Log.d(TAG, "get all");
            radar.addAll(getChinaRadar());
        }
        return radar;
    }



    private String getChinaRadarURL() {
        return "http://stock.gtimg.cn/data/index.php?appn=radar&t=all&d=09001515";
    }

    private JSONObject preprocessJson(String content) throws JSONException {
        int pos = content.indexOf('{');
        String result = StringUtils.substring(content, pos);
        JSONObject json = new JSONObject(result);
        return json;
    }

    private List<Radar> getChinaRadar()  throws ParseException, DownloadException {

        try {
            HttpGet req = new HttpGet(getChinaRadarURL());
            //req.setHeader("Referer", "http://stock.gtimg.cn");

            HttpResponse resp = getClient().execute(req);
            String content = EntityUtils.toString(resp.getEntity(), "GB2312");
            return getChinaRadarFromJson(content);
        } catch (org.apache.http.ParseException pe) {
            throw new ParseException("error parsing http data", pe);
        } catch (JSONException je) {
            throw new ParseException("error parsing http data", je);
        } catch (IOException ie) {
            throw new DownloadException("error parsing http data", ie);
        }
    }

    private List<Radar> getChinaRadarFromJson(String content) throws JSONException {
        List<Radar> radars = new ArrayList<Radar>();

        String radarData = null;
        int start = content.indexOf('{');
        while (start > 0) {
            int end = content.indexOf(";", start);
            String result = StringUtils.substring(content, start, end);
            JSONObject json = new JSONObject(result);
            radarData = json.getString("data");
            //Log.d(TAG, radarData);
            start = -1;//content.indexOf('{', end);
        }

        String[] stocks = radarData.split("\\^");
        int index = 0;
        int nbrStocks = stocks.length/6;
        for(int nbr = 0; nbr<nbrStocks; nbr++) {
            String[] stock = stocks[nbr].split("~");
            String time = stocks[0];
            String code = stocks[1];
            String name = stocks[2];
            String price = stocks[3];
            String type = stocks[4];
            String volume = stocks[5];


            Radar radar = new Radar();
            radar.setTime(time);
            radar.setCode(code);
            radar.setName(name);

            radar.setPrice(price);
            radar.setType(type);
            radar.setVolume(volume);

            radars.add(radar);
        }
        Log.i(TAG, "Radar update success, number of results ..." + nbrStocks);
        return radars;
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

}
