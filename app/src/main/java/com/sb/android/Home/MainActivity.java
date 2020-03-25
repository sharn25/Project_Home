package com.sb.android.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.Calendar;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class MainActivity extends Activity implements ConnectivityReceiver.ConnectivityReceiverListener{
  private TextView digitalclocktv;
  private TextView digitalclocktv2;
  private TextView digitalclocktv3;
  private TextView datetv;
  private TextView datetv2;
  private TextView datetv3;
  private TextView temptv;
  private TextView tvmatchname_0;
  private TextView tvname_teama_0;
  private TextView tvname_teamb_0;
  private TextView tvmatchdate;
  private TextView tvmatchtime;
  private TextView tvnoiinfo;
  private TextView tvmatchname_1, tvname_teama_1, tvteama_score, tvteamabat, tvname_teamb_1, tvteamb_score, tvteambbat, tvresult, tvmatchstatus;
  private ImageView ivteama_1, ivteamb_1;
  private ImageView weatheriv;
  private ImageView ivteama_0;
  private ImageView ivteamb_0;
  private ImageSwitcher newsbgswitcher;
  private TextSwitcher newsswitcher;
  private ImageView newsiv;
  private ImageView bgiv;
  private ProgressBar bgload;
  private ProgressBar crikload;
  private RelativeLayout cricket_info;
  private RelativeLayout cricket_view;

  Animation innewstitle;
  Animation outnewstitle;

  Animation inbg;
  Animation outbg;

  private GestureDetector mGestureDetector;
  private ViewFlipper vf;
  private int oldhour=0;
  private int olddate=0;
  private int oldmin=90;
  private int oldint=5;
  private String[] month=null;
  private String[] day=null;
  private static String API_WEATHER = "94f936a07de01954465169c3d03a95b9";
  private static String API_NEWS="4faad8d1e488492bba7167581c1abdf8";
  private String CITY_WEATHER="6619347";
  private weatherinfo weatherInfo;
  private newsinfo newsInfo;
  private JSONObject weatherdata;
  private JSONObject newsdata;
  private boolean pause=false;
  private boolean running;
  private boolean isNewsLoading=false;
  private boolean isInternetConnected=false;
  private boolean isPlaying;
  private boolean iscricketrunning;
  private boolean isCrikview;
  private boolean isMatchEnded;

  int Position = 0;
  private int i=0;
  private String text;
  private Bitmap imagebg;
  private Bitmap newsbg;
  private Typeface face1;
  private String matchnname, nameteama, nameteamb, matchtime, matchdate, urlmatch, figteama, figteamb, teama_score, teamb_score, batball_1,batball_2, matchresult = "";
    private String oldteamaname="";
    private String matchend="";
  private Bitmap teama, teamb;
  Thread news;

    @Override
    protected void onRestart() {
        super.onRestart();
        pause=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pause=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        pause=false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        pause=true;
    }

    @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
          //      WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_main);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    isInternetConnected=isNetworkAvailable();
        mLogger.mlogger("MainActivity","Internet State : " + isInternetConnected);
    digitalclocktv = (TextView) findViewById(R.id.digitalClocktv);
    datetv = (TextView) findViewById(R.id.datetv);
    temptv = (TextView) findViewById(R.id.temptv);
    weatheriv = (ImageView) findViewById(R.id.weatheriv);
    digitalclocktv2 = (TextView) findViewById(R.id.digitalClocktv2);
    digitalclocktv3 = (TextView) findViewById(R.id.digitalClocktv3);
    datetv2 = (TextView) findViewById((R.id.datetv2));
    datetv3 = (TextView) findViewById(R.id.datetv3);
    tvmatchname_0 = (TextView) findViewById(R.id.tvmatchname_0);
    tvname_teama_0 = (TextView) findViewById(R.id.tvname_teama_0);
    tvname_teamb_0 = (TextView) findViewById(R.id.tvname_teamb_0);
    tvmatchdate = (TextView) findViewById(R.id.tvmatchdate);
    tvmatchtime = (TextView) findViewById(R.id.tvmatchtime);
    tvnoiinfo = (TextView) findViewById(R.id.tvnoiinfo);
        //tvmatchname_1, tvname_teama_1, tvteama_score, tvteamabat, tvname_teamb_1, tvteamb_score, tvteambbat, tvresult;
        //ivteama_1, ivteamb_1;
        tvmatchstatus = (TextView) findViewById(R.id.tvmatchstatus);
    tvmatchname_1 = (TextView) findViewById(R.id.tvmatchname_1);
    tvname_teama_1 = (TextView) findViewById(R.id.tvname_teama_1);
    tvteama_score = (TextView) findViewById(R.id.tvteama_score);
    tvteamabat = (TextView) findViewById(R.id.tvteamabat);
    tvname_teamb_1 = (TextView) findViewById(R.id.tvname_teamb_1);
    tvteamb_score = (TextView) findViewById(R.id.tvteamb_score);
    tvteambbat = (TextView) findViewById(R.id.tvteambbat);
    tvresult = (TextView) findViewById(R.id.tvresult);
    ivteama_1 = (ImageView) findViewById(R.id.ivteama_1);
    ivteamb_1 = (ImageView) findViewById(R.id.ivteamb_1);
    newsbgswitcher  = (ImageSwitcher) findViewById(R.id.newsbgswitcher);
    newsiv = (ImageView) findViewById(R.id.newsiv);
    ivteama_0 = (ImageView) findViewById(R.id.ivteama_0);
    ivteamb_0 = (ImageView) findViewById(R.id.ivteamb_0);
    cricket_info = (RelativeLayout) findViewById(R.id.cirketinfo);
    cricket_view  = (RelativeLayout) findViewById(R.id.cirketview);
    newsswitcher = (TextSwitcher) findViewById(R.id.newsswitcher);
    crikload = (ProgressBar) findViewById(R.id.crikload);
    bgload = (ProgressBar) findViewById(R.id.bgload);
        bgiv = (ImageView) findViewById(R.id.bgiv);
        AssetManager Amr = getAssets();
    face1 = Typeface.createFromAsset(Amr,"fonts/B-reg.ttf");
        newsbgswitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView viewr = new ImageView(MainActivity.this);

                viewr.setLayoutParams(new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT, ImageSwitcher.LayoutParams.MATCH_PARENT));
                viewr.setScaleType(ImageView.ScaleType.FIT_XY);

                return viewr;
            }
        });
        newsswitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView newstitler = new TextView(MainActivity.this);
                newstitler.setTypeface(face1);
                newstitler.setGravity(Gravity.CENTER);
                newstitler.setTextSize(30);
                newstitler.setTextColor(Color.WHITE);
                return newstitler;
            }
        });


        //Animation Creation
        innewstitle = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_in);
        outnewstitle = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_out);

        inbg = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        outbg = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

    newsswitcher.setInAnimation(innewstitle);
    newsswitcher.setOutAnimation(outnewstitle);
    newsbgswitcher.setInAnimation(inbg);
    newsbgswitcher.setOutAnimation(outbg);
    newsswitcher.setCurrentText("No News Updates avaliable");
    vf = (ViewFlipper) findViewById(R.id.vf);
    vf.setDisplayedChild(1);
    Position=1;

    Log.d("mainactivity","Enter Oncreate");
      this.month = new String[] {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
      day = new String[] {"SUN","MON","TUE","WED","THR","FRI","SAT"};

      weatherInfo = new weatherinfo();
      newsInfo = new newsinfo();
       // testapi();
      this.clock();
      mLogger.mlogger("MainActivity","Starting ViewFlipper");
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        mGestureDetector = new GestureDetector(this, customGestureDetector);

  }


  public void testapi(){
      OkHttpClient client = new OkHttpClient();

      Request request = new Request.Builder()
              .url("https://dev132-cricket-live-scores-v1.p.rapidapi.com/matches.php?completedlimit=5&inprogresslimit=5&upcomingLimit=5")
              .get()
              .addHeader("x-rapidapi-host", "dev132-cricket-live-scores-v1.p.rapidapi.com")
              .addHeader("x-rapidapi-key", "70554198a6msh28f4281aca304f5p1b0810jsnece68ad3a5f9")
              .build();
try {
    Response response = client.newCall(request).execute();
    mLogger.mlogger("ManiActivity", "cricket : " + response.body().string());
}catch(Exception e){
    mLogger.mlogger("MainActivity","Error: " + e);
}
  }

  private void clock(){
      Timer timer = new Timer();
      timer.schedule(new TimerTask() {
          @Override
          public void run() {
              if(!pause) {
                  clockup();
              }
          }
      },0,10000);//Update text every second
  }

  private void clockup(){
      pause = true;
      Log.d("mainactivity","EnterClock starting");
    runOnUiThread(new Runnable() {
      @Override
      public void run() {


    Log.d("mainactivity","EnterClock Update");
    Calendar c = Calendar.getInstance();
    int date = c.get(Calendar.DATE);
    int min= c.get(Calendar.MINUTE);
    int hour= c.get(Calendar.HOUR_OF_DAY);

          if (oldhour != hour) {
              mLogger.mlogger("Mainactivity","EnterOldHour");
              oldhour=hour;
              oldint = oldint+1;
              AsyncTaskRunner runner = new AsyncTaskRunner();
              runner.execute();
                i=0;
          }
          if(olddate!=date){
              mLogger.mlogger("Mainactivity","EnterOlddate");
              olddate=date;
              String days = day[c.get(Calendar.DAY_OF_WEEK)-1];
              mLogger.mlogger("MainActivity","Calendar Monthn no : " + c.get(Calendar.MONTH));
              mLogger.mlogger("MainActivity","Calendar Month: " + month[c.get(Calendar.MONTH)]);
              String months = month[c.get(Calendar.MONTH)];
              datetv.setText(days + " , "+ months +" " + date);
              datetv2.setText(months + " "+ date + ", " + days);
              datetv3.setText(months + " "+ date + ", " + days);

          }

   // Log.d("mainactivity","Printing Date : " + date);
    //Log.d("mainactivity","Printing hours and Min : " + hour + ":" + min);
    //datetv.setText(date);
    if(min<10){
        digitalclocktv.setText(hour + ":0" + min);
        digitalclocktv2.setText(hour + ":0" + min);
        if(Position==0){
            digitalclocktv3.setText(hour + ":0" + min);
        }
    }else{
        digitalclocktv.setText(hour + ":" + min);
        digitalclocktv2.setText(hour + ":" + min);
        if(Position==0){
            digitalclocktv3.setText(hour + ":" + min);
        }
    }
        if(Position==0){
            mLogger.mlogger("MainActiviy","Entering 0 position");
         if(oldint>=5) {
             mLogger.mlogger("MainActiviy","Entering oldint<5");
             if (!iscricketrunning && !isMatchEnded) {
                 mLogger.mlogger("MainActiviy","Entering iscrikcet not running");
                 oldint=0;
                 iscricketrunning = true;
                 Asynccricket aynccricket =  new Asynccricket();
                 aynccricket.execute();
             }
         }
        }
          if(Position==1){
              if(oldmin>=90){
                  oldmin=0;
                  Asyncrunnerbg runnerbg = new Asyncrunnerbg();
                  runnerbg.execute();
              }}
          oldmin = oldmin+1;
    if(Position==2){
        if(!isNewsLoading) {

            AsyncTasknewsRunner newsrunner = new AsyncTasknewsRunner();
            newsrunner.execute();
        }
    }
          pause=false;
      }
    });
  }

    private class Asynccricket extends AsyncTask<String, String, String> {

        Toast toast;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(isPlaying){
                mLogger.mlogger("MainActivity","Enter circket_view data write");
                if(isMatchEnded){
                    isPlaying=false;
                    mLogger.mlogger("MainActivity","Enter MatchEnded Nothing Show in CirckView");
                    crikload.setVisibility(View.GONE);

                }else {

                    tvteama_score.setText(teama_score);
                    tvteamb_score.setText(teamb_score);
                    tvresult.setText(matchresult);
                    tvteamabat.setText(batball_1);
                    tvteambbat.setText(batball_2);
                    tvmatchstatus.setText(matchend);
                    if (isCrikview) {
                        tvmatchname_1.setText(matchnname);
                        ivteama_1.setImageBitmap(teama);
                        ivteamb_1.setImageBitmap(teamb);
                        tvname_teama_1.setText(nameteama);
                        tvname_teamb_1.setText(nameteamb);
                        mLogger.mlogger("MainActivity", "Enter Show Crikcet_View...");
                        tvnoiinfo.setVisibility(View.GONE);
                        cricket_info.setVisibility(View.GONE);
                        cricket_view.setVisibility(View.VISIBLE);
                        crikload.setVisibility(View.GONE);
                        isCrikview = false;
                    }

                }
                iscricketrunning=false;
                oldint=5;
            }else{
                mLogger.mlogger("MainActivity","Enter circket_info data write");
                tvmatchname_0.setText(matchnname);
                tvname_teama_0.setText(nameteama);
                tvname_teamb_0.setText(nameteamb);
                tvmatchdate.setText(matchdate);
                tvmatchtime.setText(matchtime);
                ivteama_0.setImageBitmap(teama);
                ivteamb_0.setImageBitmap(teamb);
                tvnoiinfo.setVisibility(View.GONE);
                cricket_info.setVisibility(View.VISIBLE);
                cricket_view.setVisibility(View.GONE);
                iscricketrunning = false;
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            if(isPlaying){
            mLogger.mlogger("MainActivity","Enter circket_view");
                Document doc = getdoc(urlmatch);
                if(doc!=null) {
                    matchend = doc.select("div.mch_hdr-lft").get(0).text();
                    System.out.println("checking match canelled: " + matchresult);
                    if (matchresult.indexOf("cancel") != -1) {

                        mLogger.mlogger("MainActivity", "Enter Match Cancelled Nothing Show in CirckView");
                        isMatchEnded = true;
                    } else if (matchend.indexOf("Match Ended") != -1) {
                        System.out.println("Match Endded: " + matchend);
                        isMatchEnded = true;
                    } else {
                        System.out.println("Match not Endded: " + matchend);
                        Elements teams = doc.select("img.mch_tm-flg");
                        System.out.println("Teams Size: " + teams.size());
                        if (teams.size() != 0) {
                            nameteama = teams.get(0).attr("alt");
                            if (!oldteamaname.equals(nameteama)) {
                                oldteamaname = nameteama;
                                figteama = teams.get(0).attr("src");

                                figteamb = teams.get(1).attr("src");
                                nameteamb = teams.get(1).attr("alt");

                                System.out.println("figteama: " + figteama);
                                System.out.println("figteamb: " + figteamb);
                                System.out.println("nameteama: " + nameteama);
                                System.out.println("nameteamb: " + nameteamb);
                                teama = getBitmapFromURL(figteama);
                                teamb = getBitmapFromURL(figteamb);
                                isCrikview = true;
                            }
                        }

                        teams = doc.select("div.mch_scr-wrp");
                        System.out.println("Team_board Size: " + teams.size());
                        if (teams.size() != 0) {
                            Elements socre = teams.get(0).select("span.mch_scr-pcd");
                            System.out.println("Score team A: " + socre.size());
                            if(socre.size()!=0){
                                teama_score = socre.get(0).text();
                            }
                            socre = teams.get(1).select("span.mch_scr-pcd");
                            System.out.println("Score team B: " + socre.size());
                            if(socre.size()!=0){
                                teamb_score = socre.get(0).text();
                            }

                        }
                        System.out.println("teama_score: " + teama_score);
                        System.out.println("teamb_score: " + teamb_score);
                        teams = doc.select("div.mch_tm-itm");
                        System.out.println("CCR " + teams.size());
                        if (teams.size() != 0) {
                            String ccr = teams.get(0).select("div.mch_scr-rgt").text();
                            System.out.println("CCR value: " + ccr);
                            if (ccr.indexOf("CRR") != -1) {
                                batball_1 = "Batting";
                                batball_2 = "Balling";
                            } else {
                                batball_1 = "Balling";
                                batball_2 = "Batting";
                            }
                        }
                        System.out.println("Team A: " + batball_1 + " vs " + "Team B: " + batball_2);
                        teams = doc.select("div.scr_ftr-txt");
                        if (teams.size() != 0) {
                            matchresult = teams.get(0).text();
                        }
                        System.out.println("matchresult: " + matchresult);

                    }
                }
            }else{
            mLogger.mlogger("MainActivity","Enter circket_info");
                Elements match=null;
                Document doc = getdoc("https://sports.ndtv.com/cricket/live-scores");
                if(doc!=null) {
                    Elements livematch = doc.select("div.live-score-listing");
                    String live = livematch.get(0).getElementsByTag("h2").text();
                    System.out.println("Livematch Size: " + livematch.size());
                    boolean isLive=false;
                    if (live.indexOf("Live") != -1) {
                        System.out.println("Live Match Found: " + live);
                        match = livematch.select("div.live-score-item > a");
                        System.out.println("match size: " + match.size());
                        System.out.println(match.get(0).attr("href"));
                        Elements teamnames = match.select("div.team-name");
                        String teama1 = teamnames.get(0).text();
                        String teamb1 = teamnames.get(1).text();
                        System.out.println("TeamA: " + teama1 + " " + "TeamB: " + teamb1);
                        if ("India".equals(teama1) || "India".equals(teamb1)) {
                            System.out.println("India Match Live");
                            isLive=true;
                        }
                    }
                    if(isLive) {
                        urlmatch = match.get(0).attr("href");
                        matchnname = livematch.select("div.match-venue-livescrore > span").get(0).text();
                        matchend = "Loading Data......";
                        isPlaying = true;
                        oldint = 5;
                        isCrikview = true;

                    }else {
                        System.out.println("Live Match Not Found: " + live);


                        doc = getdoc("https://sports.ndtv.com/cricket/teams/6-india-teamprofile/schedules-fixtures");

                        Elements matches = doc.select("div.team-listing-Fixtures");
                        System.out.println(matches.size());
                        Elements first = matches.get(0).select("div.team_head_date");
                        System.out.println("First Size: " + first.size());
                        matchdate = first.get(0).text();
                        System.out.println(matchdate);
                        Elements url1 = matches.get(0).select("div.team-item > a");
                        urlmatch = url1.attr("href");
                        System.out.println(urlmatch);
                        matchnname = url1.select("div.match-name").text();
                        System.out.println(matchnname);
                        Elements teams = url1.select("div.team_head_vs > span.category");
                        mLogger.mlogger("MainActivity", "Teams Size: " + teams.size());
                        nameteama = teams.get(0).text();
                        nameteamb = teams.get(1).text();
                        System.out.println(nameteama + " vs " + nameteamb);
                        matchtime = url1.select("div.match-schedule").text();
                        System.out.println(matchtime);
                        Elements fig = url1.select("div.team-image-fixture > img");
                        mLogger.mlogger("MainActivity", "fig Size: " + fig.size());
                        figteama = fig.get(0).attr("src");
                        figteamb = fig.get(1).attr("src");
                        mLogger.mlogger("MainActivity", "fig teama: " + figteama);
                        mLogger.mlogger("MainActivity", "fig teamb: " + figteamb);
                        teama = getBitmapFromURL(figteama);
                        teamb = getBitmapFromURL(figteamb);
                    }
                    //System.out.println("new sdsd sd" + doc.html());
                }
            }

            return null;
        }
    }

    private Document getdoc(String s) {
        Document doc=null;
        try {
            doc = Jsoup.connect(s).get();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return doc;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.d("MianActivity","Connectivity : " + isConnected);
        isInternetConnected=isConnected;
    }

    private class AsyncTasknewsRunner extends AsyncTask<String, String, String> {
        Drawable dr;


        @Override
        protected String doInBackground(String... params) {
            mLogger.mlogger("MainActivity","Enter News DoInBackground..");
            try {
                isNewsLoading = true;
                newsbg = getBitmapFromURL(newsInfo.getImageurl(i));
                dr = new BitmapDrawable(getResources(), newsbg);
            }catch(Exception e){
                mLogger.mlogger("MainActivity","Error News DoInBackground..");
            }
           return "";
        }


        @Override
        protected void onPostExecute(String result) {
            if(newsInfo.getTitle(i)!=null){

                mLogger.mlogger("MainActivity", "Title Array string at " + i +" : " + newsInfo.getTitle(i));

                //mLogger.mlogger("MainActivity", "Title Array string at " + i +" : " + newsInfo.getTitle(i).length());
                text=newsInfo.getTitle(i);

                mLogger.mlogger("MainActivity", "Done with image at " + i +" : " + newsInfo.getImageurl(i));
                //mLogger.mlogger("MainActivity", "Setting Image " + i +" : " + newsInfo.getImageurl(i));


                if(text.length()>100){
                    text=text.substring(0,100);
                }
                newsswitcher.setText(text);
                newsbgswitcher.setImageDrawable(dr);

                //mLogger.mlogger("MainActivity", "Starting out");

                i=i+1;
                if(i==20){
                    i=0;
                }
            }else{
                mLogger.mlogger("MainActivity", "Nothing to load from newsinfo : " + newsInfo.getfirstelement());
            }
            isNewsLoading=false;
        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {



        @Override
        protected String doInBackground(String... params) {

            weatherdata=Utils.getJSON("http://api.openweathermap.org/data/2.5/weather?id=" + CITY_WEATHER + "&APPID=" + API_WEATHER + "&units=metric");
            newsdata=Utils.getJSON("http://newsapi.org/v2/top-headlines?country=in&pageSize=20&category=technology&apiKey=4faad8d1e488492bba7167581c1abdf8");
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
        if(weatherdata!=null){
        mLogger.mlogger("MainActivity", "Output(Weatehr) : " +weatherdata.toString());
            try{

                JSONObject weather = weatherdata.getJSONArray("weather").getJSONObject(weatherdata.getJSONArray("weather").length()-1);
                JSONObject main = weatherdata.getJSONObject("main");
                JSONObject wind = weatherdata.getJSONObject("wind");
                weatherInfo.setCity(weatherdata.getString("name"));
                weatherInfo.setDescription(weather.getString("description"));
                weatherInfo.setIcon(weather.getString("icon"));
                weatherInfo.setTemp(main.getInt("temp"));
                weatherInfo.setHumidity(main.getInt("humidity"));
                weatherInfo.setWinddeg(wind.getInt("deg"));
                weatherInfo.setWindspeed(wind.getInt("speed"));
                mLogger.mlogger("MainActivity","City : " + weatherInfo.getCity()+" Des. ; "+weatherInfo.getDescription()+" Icon : "+weatherInfo.getIcon()+" Temp : "+weatherInfo.getTemp()+" Wind Speed : " +weatherInfo.getWindspeed() );

            }catch(Exception e){
                mLogger.mlogger("MainActivity","errorPost : " +e);
            }
        }
            //mLogger.mlogger("MainActivity", "Output(news) : " +newsdata.toString());
            if (newsdata != null) {
                try{
                    JSONArray articles = newsdata.getJSONArray("articles");
                    mLogger.mlogger("MainActivity","arraysize : " + articles.length());
                    String[] title = new String[22];

                    String[] imageurl = new String[22];
                    String[] sourceurl = new String[22];
                    for(int i=0;i<articles.length();i++){
                        JSONObject data = articles.getJSONObject(i);
                        title[i]=data.getString("title");

                        imageurl[i]=data.getString("urlToImage");
                        sourceurl[i]=data.getString("url");
                    }
                    newsInfo.setTitle(title);

                    newsInfo.setImageurl(imageurl);
                    newsInfo.setSourceurl(sourceurl);
                }catch(Exception e){
                    mLogger.mlogger("MainActivity","error(newsJSON) : " +e);
                }
                temptv.setText(weatherInfo.getTemp()+"\u2103");
                seticon(weatherInfo.getIcon());
            }
        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }
    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            mLogger.mlogger("MainActivity","Double tap happed");
            if(Position==1){
                Asyncrunnerbg runnerbg = new Asyncrunnerbg();
            runnerbg.execute();

            }else if(Position==0){
                mLogger.mlogger("MainActivity","Enter Double tap poistion zero");
                if(!isPlaying && tvnoiinfo.getVisibility()!=View.VISIBLE){
                    mLogger.mlogger("MainActivity","Enter Double tap isPlaying False");
                    AsyncTaskcrikchk crikchker = new AsyncTaskcrikchk();
                    crikchker.execute();
            }
            }
            return super.onDoubleTap(e);
        }
        private class AsyncTaskcrikchk extends AsyncTask<String, String, String> {
            Elements matches;
            Toast toast;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                crikload.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                System.out.println(matches.size());
                if(matches.size()!=0) {
                    System.out.println("Match yet to be Started");
                    toast.makeText(MainActivity.this,"Match yet to be Started",Toast.LENGTH_SHORT).show();
                    crikload.setVisibility(View.GONE);
                }else{
                    System.out.println("Match Started");
                    toast.makeText(MainActivity.this,"Match Started",Toast.LENGTH_SHORT).show();

                    isPlaying=true;
                    oldint=5;
                    isCrikview=true;
                }
            }

            @Override
            protected String doInBackground(String... strings) {


                Document doc = getdoc(urlmatch);
                matches = doc.select("div.cnt_dwn-ttl");
                return null;
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            mLogger.mlogger("MainActivity","Entering GentureDector" + "e1.getY : " + e1.getY() + " e2.getY : " + e2.getY());
            // Swipe left (next)e
            mLogger.mlogger("MainActivty","e1.getX : " + e1.getX());
            mLogger.mlogger("MainActivty","e2.getX : " + e2.getX());
            mLogger.mlogger("MainActivty","e1.getY : " + e1.getY());
            mLogger.mlogger("MainActivty","e2.getY : " + e2.getY());
            float Xdiff = e1.getX()-e2.getX();
            if(e1.getX()>770 && e1.getX()>e2.getX()+200){
                mLogger.mlogger("MainActivty","Activity Lunched : " + Xdiff);
                Intent intent = new Intent(MainActivity.this, AppMenuActivity.class);
                startActivity(intent);
            }else {
                mLogger.mlogger("MainActivty","other operation : " + Xdiff);
                if (e1.getX() > e2.getX() + 200) {
                    mLogger.mlogger("MainAcivity", " Enter BOTTOM to TOP");
                    if (Position == 0) {
                        running = false;
                        vf.setInAnimation(MainActivity.this, R.anim.left_in);
                        vf.setOutAnimation(MainActivity.this, R.anim.left_out);
                        Position = 1;
                        mLogger.mlogger("CustomGestureDector", "Postion : " + Position);
                        vf.setDisplayedChild(1);

                    } else if (Position == 1) {
                        vf.setInAnimation(MainActivity.this, R.anim.left_in);
                        vf.setOutAnimation(MainActivity.this, R.anim.left_out);
                        Position = 2;
                        mLogger.mlogger("CustomGestureDector", "Postion : " + Position);
                        vf.setDisplayedChild(2);
                        running = true;

                    } else if (Position == 2) {

                        mLogger.mlogger("CustomGestureDector", "Postion correct : " + Position);

                    }

                }

                // Swipe right (previous)
                if (e1.getX() < e2.getX() + 200) {
                    mLogger.mlogger("MainAcivity", " Enter TOP to BOTTOM");
                    if (Position == 0) {
                        running = false;
                        mLogger.mlogger("CustomGestureDector", "Postion correct : " + Position);

                    } else if (Position == 1) {
                        running = false;
                        vf.setInAnimation(MainActivity.this, R.anim.right_in);
                        vf.setOutAnimation(MainActivity.this, R.anim.right_out);
                        Position = 0;
                        mLogger.mlogger("CustomGestureDector", "Postion : " + Position);
                        vf.setDisplayedChild(0);
                    } else if (Position == 2) {
                        running = false;
                        vf.setInAnimation(MainActivity.this, R.anim.right_in);
                        vf.setOutAnimation(MainActivity.this, R.anim.right_out);
                        Position = 1;
                        mLogger.mlogger("CustomGestureDector", "Postion : " + Position);
                        vf.setDisplayedChild(1);
                    }

                }
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }


    private class Asyncrunnerbg extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bgload.setVisibility(View.VISIBLE);
            //Enable progressbar
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            bgiv.setImageBitmap(imagebg);
            bgload.setVisibility(View.GONE);//Disbale progress bar
            /*
           outbg.setAnimationListener(new Animation.AnimationListener() {
               @Override
               public void onAnimationStart(Animation animation) {

               }

               @Override
               public void onAnimationEnd(Animation animation) {
                   bgiv.setImageBitmap(imagebg);
                   inbg.setAnimationListener(new Animation.AnimationListener() {
                       @Override
                       public void onAnimationStart(Animation animation) {

                       }

                       @Override
                       public void onAnimationEnd(Animation animation) {

                       }

                       @Override
                       public void onAnimationRepeat(Animation animation) {

                       }
                   });bgiv.startAnimation(inbg);
               }

               @Override
               public void onAnimationRepeat(Animation animation) {

               }
           });

            bgiv.startAnimation(outbg);*/
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            imagebg = getBitmapFromURL(getResources().getString(R.string.bg_url));
            return null;
        }
    }

    public Bitmap getBitmapFromURL(String imageUrl) {
        try {


            //-----------

           OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(10,TimeUnit.SECONDS)
                   .writeTimeout(10, TimeUnit.SECONDS)
                   .readTimeout(10,TimeUnit.SECONDS)
                   .build();



            Request.Builder builder = new Request.Builder();
            builder.url(imageUrl);

            Request request = builder.build();

            Response response=null;

            try {
                
                response = client.newCall(request).execute();

            }catch (Exception e){
                e.printStackTrace();
            }
            //---------

           // HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //connection.setDoInput(true);
            //connection.connect();
            //InputStream input = connection.getInputStream();
            Bitmap myBitmap=null;
            try {
              myBitmap = BitmapFactory.decodeStream(response.body().byteStream());
            }catch (OutOfMemoryError e){
                mLogger.mlogger("MainActivity", "OutOfMemoryErro: " + e);
            }
            //Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private  void seticon(String id){
        switch(id){
            case "01d":
                weatheriv.setImageResource(R.drawable.weatherclear);
                break;
            case "01n":
                weatheriv.setImageResource(R.drawable.weathermoon);
                break;
            case "02d":
                weatheriv.setImageResource(R.drawable.weatherfewclouds);
                break;
            case "02n":
                weatheriv.setImageResource(R.drawable.weatherfewclouds);//replace with night few clouds
                break;
            case "03d":
                weatheriv.setImageResource(R.drawable.weatherclouds);
                break;
            case "03n":
                weatheriv.setImageResource(R.drawable.weatherclouds);
                break;
            case "04d":
                weatheriv.setImageResource(R.drawable.weatherclouds);//repalce with double clouds
                break;
            case "04n":
                weatheriv.setImageResource(R.drawable.weatherclouds);//repalce with double clouds
                break;
            case "09d":
                weatheriv.setImageResource(R.drawable.weathershowerrain);
                break;
            case "09n":
                weatheriv.setImageResource(R.drawable.weathershowerrain);
                break;
            case "10d":
                weatheriv.setImageResource(R.drawable.weatherrain);
                break;
            case "10n":
                weatheriv.setImageResource(R.drawable.weatherrain);//raplce  with night rain
                break;
            case "11d":
                weatheriv.setImageResource(R.drawable.weatherthstrom);
                break;
            case "11n":
                weatheriv.setImageResource(R.drawable.weatherthstrom);
                break;
            case "13d":
                weatheriv.setImageResource(R.drawable.weathersnow);
                break;
            case "13n":
                weatheriv.setImageResource(R.drawable.weathersnow);
                break;
            case "50d":
                weatheriv.setImageResource(R.drawable.weatherhazed);
                break;
            case "50n":
                weatheriv.setImageResource(R.drawable.weatherehazen);
                break;
            default:
                weatheriv.setImageResource(R.drawable.weatherrefresh);

                break;

        }
    }
 }