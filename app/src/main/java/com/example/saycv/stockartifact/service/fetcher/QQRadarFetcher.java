package com.example.saycv.stockartifact.service.fetcher;

import android.content.Context;
import android.text.format.Time;

import com.example.saycv.stockartifact.Engine;
import com.example.saycv.stockartifact.events.RadarsEventArgs;
import com.example.saycv.stockartifact.model.RadarsHistoryEvent;
import com.example.saycv.stockartifact.model.Radar;
import com.example.saycv.stockartifact.service.exception.DownloadException;
import com.example.saycv.stockartifact.service.exception.ParseException;
import com.example.saycv.stockartifact.service.impl.RadarsService;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.saycv.logger.Log;
import org.saycv.sgs.model.SgsHistoryEvent;
import org.saycv.sgs.utils.SgsDateTimeUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QQRadarFetcher extends BaseRadarFetcher {
    public static final String TAG = "QQRadarFetcher";
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm";
    private Context context;
    private boolean initDone = false;

    private boolean debug = false;

    /*private static final int sTimeoutConnection = 3000;
    private static final int sTimeoutSocket = 5000;*/

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


        if (debug || ((hourofday == 9 && minute >= 24) || hourofday > 9 && hourofday < 15) || (hourofday == 15 && minute <= 1)) {
            if (initDone == false) {
                Log.d(TAG, "get all begin");
                radar.addAll(getChinaRadar("all"));
                Log.d(TAG, "get all end");
                initDone = true;
            } else {
                Log.d(TAG, "get latest");
                radar.addAll(getChinaRadar("latest"));
            }
        } else {
            Log.d(TAG, "get all");
            radar.addAll(getChinaRadar("all"));
        }
        return radar;
    }


    private String getChinaRadarURL() {
        return "http://stock.gtimg.cn/data/index.php?appn=radar&t=all&d=09001515";
    }

    private String getChinaRadarURL(String flag) {
        return String.format("http://stock.gtimg.cn/data/index.php?appn=radar&t=%s&d=09001515", flag);
    }

    private JSONObject preprocessJson(String content) throws JSONException {
        int pos = content.indexOf('{');
        String result = StringUtils.substring(content, pos);
        JSONObject json = new JSONObject(result);
        return json;
    }

    private List<Radar> getChinaRadar(String flag) throws ParseException, DownloadException {

        try {
            HttpGet req = new HttpGet(getChinaRadarURL(flag));
            //req.setHeader("Referer", "http://stock.gtimg.cn");
            /*final HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, sTimeoutConnection);
            HttpConnectionParams.setSoTimeout(params, sTimeoutSocket);
            ((HttpGet)req).setParams(params);*/

            HttpResponse resp = getClient().execute(req);

            String content = EntityUtils.toString(resp.getEntity(), "GB2312");
            return getChinaRadarFromJson(content);
        } catch (org.apache.http.ParseException pe) {
            throw new ParseException("error parsing http data", pe);
        } catch (JSONException je) {
            throw new ParseException("error parsing http data", je);
        } catch (IOException ie) {
            //throw new DownloadException("error parsing http data", ie);
            Log.e(TAG, "error parsing http data: " + ie.getMessage());
            return new ArrayList<Radar>();
        }
    }

    private List<Radar> getChinaRadarFromJson(String content) throws JSONException {
        List<Radar> radars = new ArrayList<Radar>();

        ((RadarsService) ((Engine) Engine.getInstance()).getRadarsService()).getHistoryService().clear();

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
        int nbrStocks = stocks.length / 6;
        for (int nbr = 0; nbr < nbrStocks; nbr++) {
            String[] stock = stocks[nbr].split("~");
            String time = stock[0];
            String code = stock[1];
            String name = stock[2];
            String price = stock[3];
            String type = stock[4];
            String volume = stock[5];
            String numbers = String.valueOf(nbrStocks - nbr);


            Radar radar = new Radar();
            radar.setTime(time);
            radar.setCode(code);
            radar.setName(name);

            radar.setPrice(price);
            radar.setType(type);
            radar.setVolume(volume);
            radar.setNumbers(numbers);

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
