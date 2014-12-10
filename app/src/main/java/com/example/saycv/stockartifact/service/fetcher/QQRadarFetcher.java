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

public class QQRadarFetcher extends BaseRadarFetcher {
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm";
    private Context context;

    public QQRadarFetcher(Context context) {
        this.context = context;
    }

    @Override
    public List<Radar> fetch() throws DownloadException, ParseException {
        List<Radar> radar = new ArrayList<Radar>();
        radar.addAll(getChinaRadar());
        return radar;
    }



    private String getChinaRadarURL() {
        return "http://money18.on.cc/js/daily/worldidx/worldidx_b.js";
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
            req.setHeader("Referer", "http://money18.on.cc/");

            HttpResponse resp = getClient().execute(req);
            String content = EntityUtils.toString(resp.getEntity(), "Big5");
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
        List<Radar> indexes = new ArrayList<Radar>();
        int start = content.indexOf('{');
        while (start > 0) {
            int end = content.indexOf(";", start);
            String result = StringUtils.substring(content, start, end);
            JSONObject json = new JSONObject(result);
            String time = json.getString("Name");
            String code = json.getString("Point");
            String name = json.getString("Name");

            String price = json.getString("Name");
            String type = json.getString("Name");
            String volume = json.getString("Name");


            Radar radar = new Radar();
            radar.setCode(time);
            radar.setCode(code);
            radar.setName(name);

            radar.setPrice(price);
            radar.setType(type);
            radar.setVolume(volume);




            indexes.add(radar);
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

}
