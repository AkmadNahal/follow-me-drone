package com.manoj.gps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	
	Double prevLat;
	Double prevLon;
	Double curLat;
	Double curLon;
	int first=0;
	Boolean forward = true;
	Double dist;
	Double d2r = 0.0174532925199433;
	Double Rad = (double) 6371000;
	ListView lv;
	ArrayAdapter<String> listAdapter ; 
	ArrayAdapter<String> adapter;
	ArrayList<String> adist = new ArrayList<String>();
	 DefaultHttpClient client = new DefaultHttpClient();
	 Long lastupdated= System.currentTimeMillis();
	 Timer timer;
	TimerTask timerTask;
	final Handler handler = new Handler();
	TextView tv;
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationListener locationListener = new MyLocationListener();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, (float)1.5, locationListener);
        tv = (TextView) findViewById(R.id.status);
        lv = (ListView) findViewById( R.id.listView1 );  
        startTimer();
        
/*        String[] values = new String[] { "Android List View", 
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android", 
                "Android Example", 
                "List View Source Code", 
                "List View Array Adapter", 
                "Android Example List View" 
               };*/
         
        adist.add("Manoj 1");
        
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, adist);
      
        lv.setAdapter(adapter); 
        writetofile("test");


    }
    
    public void startTimer() {
		timer = new Timer();
		initializeTimerTask();
		
		timer.schedule(timerTask,0,500); //
	}

public void initializeTimerTask() {
		
		timerTask = new TimerTask() {
			public void run() {
				
				handler.post(new Runnable() {
					public void run() {
						long curr = System.currentTimeMillis();
						//writetofile("500 up "+curr);
						if((curr -lastupdated)>2000)
						{
							if(forward==true)
							{
							SendRequest task = new SendRequest();
					        task.execute(new String[] { "stop" });
							writetofile("stop "+curr);
					        addlist("stop");
					        forward=false;
							}
						}
					}
				});
			}
		};
}

	public void takeoff(View v)
	{
		SendRequest task = new SendRequest();
        task.execute(new String[] { "takeoff" });
        addlist("takeoff");
	}
	
	
	public void land(View v)
	{
		SendRequest task = new SendRequest();
        task.execute(new String[] { "land" });
        addlist("land");
	}

    private final class MyLocationListener implements LocationListener {

        @Override
        public void onProviderDisabled(String provider) {
           // called when the GPS provider is turned off (user turning off the GPS on the phone)
        	System.out.println("provider disabled");
        }

        @Override
        public void onProviderEnabled(String provider) {
           // called when the GPS provider is turned on (user turning on the GPS on the phone)
        	System.out.println("provider enabled");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
           // called when the status of the GPS provider changes
        	System.out.println("status changed to " + status);
        }

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			//System.out.println("location changed");
			if(first==0)
			{
				prevLat = location.getLatitude();
				prevLon = location.getLongitude();
				//prevLat = Rad * Math.cos(location.getLatitude()) * Math.cos(location.getLongitude());
				//prevLon = Rad * Math.cos(location.getLatitude()) * Math.sin(location.getLongitude());
				first =1;
				System.out.println("Latitude: "+ prevLat + "Longitude: "+prevLon);
				writetofile(location.getLatitude()+","+location.getLongitude()+","+System.currentTimeMillis());
				Log.d("location", "Latitude: "+ prevLat + "Longitude: "+prevLon);
			}
			else
			{ 
				
				curLat = location.getLatitude();
				curLon = location.getLongitude();
				//System.out.println("Latitude: "+ curLat + "Longitude: "+curLon);
				Log.d("location", "Latitude: "+ curLat + "Longitude: "+curLon);
				writetofile(location.getLatitude()+","+location.getLongitude()+" "+System.currentTimeMillis());
				/*
				//curLat = Rad * Math.cos(location.getLatitude()) * Math.cos(location.getLongitude());
				//curLon = Rad * Math.cos(location.getLatitude()) * Math.sin(location.getLongitude());
				//double dist2 = Math.sqrt(Math.pow((curLat-prevLat), 2) + Math.pow((curLon-prevLon), 2));
				dist= haversine_mt(curLat,curLon,prevLat,prevLon);
				double dx = (curLat - prevLat) * 111000;
		        double dy = (curLon - prevLon) * 111000;
				//double dx = (curLat - prevLat);
				//double dy = (curLon - prevLon);
		        prevLat = location.getLatitude();
				prevLon = location.getLongitude();
		        //prevLat = Rad * Math.cos(location.getLatitude()) * Math.cos(location.getLongitude());
				//prevLon = Rad * Math.cos(location.getLatitude()) * Math.sin(location.getLongitude());
				lastupdated = System.currentTimeMillis();
				//System.out.println("Latitude: " + location.getLatitude() + " Longitude: "+ location.getLongitude());
				//System.out.println("DX: " + dx + " DY: "+ dy);
				System.out.println("Distance :" + dist/2);
				*/
				lastupdated = System.currentTimeMillis();
		        SendRequest task = new SendRequest();
		        task.execute(new String[] { "front" });
		        addlist("front");
		        forward=true;
				
			}
			//System.out.println("Altitude: " + location.getAltitude()+"Accuracy 68%: " + location.getAccuracy()+"bearing: " + location.getBearing());
			//System.out.println("Latitude: " + location.getLatitude()+"Longitude: " + location.getLongitude()+"Speed: " + location.getSpeed());
		}
}
    
    public void writetofile(String s)
    { 
    	//String filename="output.txt";  
        String data=s+"\n";  
          
           try {  
               File myFile = new File("/sdcard/"+"output.txt");   
               // myFile.createNewFile();  
                FileOutputStream fOut = new   FileOutputStream(myFile,true);  
                OutputStreamWriter myOutWriter = new   OutputStreamWriter(fOut);  
                myOutWriter.append(data);  
                myOutWriter.close();  
                fOut.close();  
             
             
           } catch (FileNotFoundException e) {e.printStackTrace();}  
           catch (IOException e) {e.printStackTrace();}  
            
    }
    
    public void addlist(String s){
    	tv.setText(s);
    	adist.add(s);//Double.toString(dlong) + );
		adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, adist);	
        lv.setAdapter(adapter); 
		lv.invalidateViews();
    }
    
    private class SendRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... distance) {
          String response = "";
          for (String dist : distance) {
           
            HttpGet httpGet = new HttpGet("http://192.168.1.2:1396/"+dist+"/0.1");
            try {
              HttpResponse execute = client.execute(httpGet);
              execute.getEntity().getContent().close();

            } catch (Exception e) {
              e.printStackTrace();
            }
          }
          return response;
        }

        @Override
        protected void onPostExecute(String result) {
          
        }
      }

    
    double haversine_mt(double lat1, double long1, double lat2, double long2)
    {
        double dlong = (long2 - long1) * d2r;
        double dlat = (lat2 - lat1) * d2r;
        double a = Math.pow(Math.sin(dlat/2.0), 2) + Math.cos(lat1*d2r) * Math.cos(lat2*d2r) * Math.pow(Math.sin(dlong/2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = 6367 * c;

        return d*1000;
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
