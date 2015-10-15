//========================================================================================================
//
//  Title:       Map 
//  Course:      CSC 5991  Mobile Application Development
//  Application: Student Subject Recommendation App
//  Author:      Lakshmi Sompalli, Sunil Srinivas, Mehrina
//  Date:        7/3/2015
//  Description:
//  	This activity displays a screen with Googlemap and point the location of   
//		 	provided GPS co-ordinates
//		No Input is required from user
//      	Program uses Googlemaps 
//===========================================================================================================



package com.app3;

//Importing android related packages
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

//Importing google play related packages
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity implements LocationListener {

 GoogleMap googleMap;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
 	super.onCreate(savedInstanceState);
     
 	// Show error dialog if GoolglePlayServices not available
     if (!isGooglePlayServicesAvailable()) {
         finish();
     }
     setContentView(R.layout.laymain);
     
     // This fragment is the simplest way to place a map in an application
     SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
     googleMap = supportMapFragment.getMap();
     googleMap.setMyLocationEnabled(true);
     
     // This class provides access to the system location services. 
     LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
     Criteria criteria = new Criteria();
     String bestProvider = locationManager.getBestProvider(criteria, true);
     Location location = locationManager.getLastKnownLocation(bestProvider);
     locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);        

     // Setting location for adding markers on the map
     LatLng novi = new LatLng(42.4805556, -83.4755556);
     LatLng detroit = new LatLng(42.3313889, -83.0458333);
     
     // Adding markers
     googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(novi, 8));
     googleMap.addMarker(new MarkerOptions()
	        .title("Novi")
	        .snippet("The place to be :D")
	        .position(novi));
     
     googleMap.addMarker(new MarkerOptions()
     .title("Detroit")
     .snippet("Wayne State University Location")
     .position(detroit));
     
     onLocationChanged(location);
 }

 // Function to set the marker on the current location 
 @Override
 public void onLocationChanged(Location location) {
     TextView locationTv = (TextView) findViewById(R.id.latlongLocation);
     double latitude = location.getLatitude();
     double longitude = location.getLongitude();
     LatLng latLng = new LatLng(latitude, longitude);
     googleMap.addMarker(new MarkerOptions().position(latLng));
     googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
     googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
     locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);        
 }

 @Override
 public void onProviderDisabled(String provider) {
     // TODO Auto-generated method stub
 }

 @Override
 public void onProviderEnabled(String provider) {
     // TODO Auto-generated method stub
 }

 @Override
 public void onStatusChanged(String provider, int status, Bundle extras) {
     // TODO Auto-generated method stub
 }

 // Checking if the Google Play services are Available
 private boolean isGooglePlayServicesAvailable() {
     int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
     if (ConnectionResult.SUCCESS == status) {
         return true;
     } else {
         GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
         return false;
     }
 }
}

