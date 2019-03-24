package com.example.alexandr.maptour;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.sql.Types;
import java.util.Date;
import android.content.Context;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import static android.app.PendingIntent.getActivity;
import static com.here.sdk.hacwrapper.HacAnalytics.initialize;

import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.mapping.SupportMapFragment;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteManager;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteTta;
//import com.example.alexandr.tourist.GPS;
import java.sql.Types;
import java.util.Date;
import android.content.Context;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;


import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
//import com.here.android.mpa.guidance.TrafficUpdater;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.mapping.MapView;
//import com.here.android.mpa.routing.CoreRouter;
//import com.here.android.mpa.routing.DynamicPenalty;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteTta;
//import com.here.android.mpa.routing.RoutingError;
//import com.here.android.mpa.routing.DynamicPenalty;
import java.io.File;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
//import com.here.android.mpa.routing.CoreRouter;
//import com.example.alexandr.tourist.Coordinate;
//import com.here.android.mpa.mapping.ClusterLayer;
//import com.here.android.mpa.navigating.NavigationManager;
//import com.here.android.mpa.mapping.Route;
//import com.here.android.mpa.common.ApplicationContext;
//import com.here.android.mpa.guidance.TrafficUpdater;
//import com.here.android.mpa.routing.CoreRouter;
//import com.here.android.mpa.routing.DynamicPenalty;
//import com.here.android.mpa.routing.RoutingError;

@SuppressLint("Registered")

public class RoutingActivity extends FragmentActivity {
    static final String LOG_TAG = RoutingActivity.class.getSimpleName();
    GeoPosition geoPosition;
    // permissions request code
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    List<RouteResult> result;
    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };
    MapRoute mpR;
    // map embedded in the map fragment
    private Map map = null;
    Route route;
    // map fragment embedded in this activity
    private SupportMapFragment mapFragment = null;

    // TextView for displaying the current map scheme
    private TextView textViewResult = null;

    // MapRoute for this activity
    private static MapRoute mapRoute = null;
    private PositioningManager posManager;
    //  private CoreRouter coreRouter;

    Button button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();

        button = (Button) findViewById(R.id.camera);
        button.setOnClickListener(MyOnClik);
//        initialize();
    }

    View.OnClickListener MyOnClik = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent2 = new Intent(RoutingActivity.this, opencvActivity.class);
            startActivity(intent2);
        }
    };

    private SupportMapFragment getSupportMapFragment() {
        return (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
    }

    private void initialize() {
        setContentView(R.layout.activity_routing);

        // Search for the map fragment to finish setup by calling init().
        mapFragment = getSupportMapFragment();
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // retrieve a reference of the map from the map fragment
                    final GeoCoordinate[] coords = new GeoCoordinate[1];
                    // Set the map zoom level to the average between min and max (no animation)
                    PositioningManager.OnPositionChangedListener positionListener
                            = new PositioningManager.OnPositionChangedListener() {

                        @Override
                        public void onPositionUpdated(PositioningManager.LocationMethod locationMethod, GeoPosition geoPosition, boolean b) {
                            //            textViewResult.setText("");
                            coords[0] = geoPosition.getCoordinate();
                            if (map != null && mapRoute != null) {
                                //map.removeMapObject(mapRoute);
                                mapRoute = null;
                                map = mapFragment.getMap();
                                // Set the map center coordinate to the Vancouver region (no animation)
                                //   map.setCenter(new GeoCoordinate(coords[0].getLatitude(), coords[0].getLongitude()),
                                // Map.Animation.NONE);
                                map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
                                //MapMarker uMarker = new MapMarker();
                                //uMarker.setCoordinate(new GeoCoordinate(59.933900, 30.306474, 0.0));
                                //map.addMapObject(uMarker);
                                // 2. Initialize RouteManager
                                RouteManager routeManager = new RouteManager();
                                Image image=new Image();
                                // 3. Select routing options
                                RoutePlan routePlan = new RoutePlan();

                                RouteOptions routeOptions = new RouteOptions();
                                routeOptions.setTransportMode(RouteOptions.TransportMode.PEDESTRIAN);
                                routeOptions.setRouteType(RouteOptions.Type.FASTEST);
                                routePlan.setRouteOptions(routeOptions);
                                MapMarker mMark1=new MapMarker();
//                            mMark1.setCoordinate(new GeoCoordinate(coords[0]));
                                map.addMapObject(mMark1);
                                System.out.println("coors " + coords[0]);
                                //System.out.print("Coords"+ coords[0].getLatitude());
//                            routePlan.addWaypoint(new GeoCoordinate(coords[0]));
                                // map.setCenter(currentCoordinates,
                                //     Map.Animation.NONE);
                                map.getPositionIndicator().setVisible(true);
                                // 4. Select Waypoints for your routes
                                // START: Nokia, Burnaby
                                //mapMarker.getCoordinate();
                                //  mark1 = image(R.drawable.mark1);

                                // GeoCoordinate currentCoordinates = geoPosition.getCoordinate();
                                MapMarker mMark2=new MapMarker();
                                mMark2.setCoordinate(new GeoCoordinate( 59.934389, 30.324555, 0.0));
                                map.addMapObject(mMark2);
                                routePlan.addWaypoint(new GeoCoordinate(59.934389, 30.324555));
                                MapMarker mMark3=new MapMarker();
                                mMark3.setCoordinate(new GeoCoordinate( 59.940031, 30.312841, 0.0));
                                map.addMapObject(mMark3);
                                routePlan.insertWaypoint(new GeoCoordinate(59.940031, 30.312841), 1);
                                RouteManager.Error eRror = routeManager.calculateRoute(routePlan, routeManagerListener);
//           routeManagerListener.onCalculateRouteFinished(error,result);
                                //RouteManager.Error error = routeManager.calculateRoute(routePlan, routeManagerListener);
                                //          map.addMapObject(mpR);
                                if (eRror != RouteManager.Error.NONE) {
                                    Toast.makeText(getApplicationContext(),
                                            "Route calculation failed with: " + eRror.toString(), Toast.LENGTH_SHORT)
                                            .show();
                                }

                            }

                        }

                        @Override
                        public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {

                        }
                    };

//                              map.addMapObject(mapRoute);


                    posManager = PositioningManager.getInstance();
                    posManager.addListener(
                            new WeakReference<PositioningManager.OnPositionChangedListener>(positionListener));
                    boolean startSuccessful = posManager.start(
                            PositioningManager.LocationMethod.GPS_NETWORK);
                } else {
                    Log.e(LOG_TAG, "Cannot initialize SupportMapFragment (" + error + ")");
                }
            }
        });

    }
    // SQLiteDatabase db = helper.getReadableDatabase();

    String table = "table2";
    String[] columns = {"column1", "column3"};
    String selection = "column3 =?";
    String[] selectionArgs = {"apple"};
    String groupBy = null;
    String having = null;
    String orderBy = "column3 DESC";
    String limit = "10";

    //Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    private PositioningManager.OnPositionChangedListener positionListener
            = new PositioningManager.OnPositionChangedListener() {
        @Override
        public void onPositionUpdated(PositioningManager.LocationMethod locationMethod, GeoPosition geoPosition, boolean b) {
            GeoCoordinate currentCoordinates = geoPosition.getCoordinate();
//            textViewResult.setText("");
            //coords =currentCoordinates;
            if (map != null && mapRoute != null) {
                map.removeMapObject(mapRoute);
                mapRoute = null;
            }

            // 2. Initialize RouteManager
            RouteManager routeManager = new RouteManager();
            Image image=new Image();
            // 3. Select routing options
            RoutePlan routePlan = new RoutePlan();

            RouteOptions routeOptions = new RouteOptions();
            routeOptions.setTransportMode(RouteOptions.TransportMode.PEDESTRIAN);
            routeOptions.setRouteType(RouteOptions.Type.FASTEST);
            routePlan.setRouteOptions(routeOptions);
            MapMarker mMark1=new MapMarker();
            // mMark1.setCoordinate(new GeoCoordinate(coords));
            // map.addMapObject(mMark1);
            //  routePlan.addWaypoint(new GeoCoordinate(coords));
            // map.setCenter(currentCoordinates,
            //     Map.Animation.NONE);
            map.getPositionIndicator().setVisible(true);
            // 4. Select Waypoints for your routes
            // START: Nokia, Burnaby
            //mapMarker.getCoordinate();
            //  mark1 = image(R.drawable.mark1);

            // GeoCoordinate currentCoordinates = geoPosition.getCoordinate();
            MapMarker mMark2=new MapMarker();
            mMark2.setCoordinate(new GeoCoordinate( 59.934389, 30.324555, 0.0));
            map.addMapObject(mMark2);
            routePlan.addWaypoint(new GeoCoordinate(59.934389, 30.324555));
            MapMarker mMark3=new MapMarker();
            mMark3.setCoordinate(new GeoCoordinate( 59.940031, 30.312841, 0.0));
            map.addMapObject(mMark3);
            routePlan.insertWaypoint(new GeoCoordinate(59.940031, 30.312841), 1);
//           map.addMapObject(mapRoute);
            RouteManager.Error error = routeManager.calculateRoute(routePlan, routeManagerListener);
//           routeManagerListener.onCalculateRouteFinished(error,result);
            //RouteManager.Error error = routeManager.calculateRoute(routePlan, routeManagerListener);
            //          map.addMapObject(mpR);
            if (error != RouteManager.Error.NONE) {
                Toast.makeText(getApplicationContext(),
                        "Route calculation failed with: " + error.toString(), Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {

        }
    };


    /**
     * Checks the dynamically controlled permissions and requests missing permissions from end user.
     */

    class HaversineAlgorithm {

        static final double _eQuatorialEarthRadius = 6378.1370D;
        static final double _d2r = (Math.PI / 180D);

        public int HaversineInM(double lat1, double long1, double lat2, double long2) {
            return (int) (1000D * HaversineInKM(lat1, long1, lat2, long2));
        }

        public double HaversineInKM(double lat1, double long1, double lat2, double long2) {
            double dlong = (long2 - long1) * _d2r;
            double dlat = (lat2 - lat1) * _d2r;
            double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r)
                    * Math.pow(Math.sin(dlong / 2D), 2D);
            double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
            double d = _eQuatorialEarthRadius * c;

            return d/0.9;
        }

    }

    // }

    public static String timeConvert(double time){


        //if (!str.matches("\\d+")) throw new IllegalArgumentException("Неверные входные данные");
        // long sec = Long.valueOf(str);
        long s = (int)time % 60;
        long m = (int)(time / 60) % 60;
        long  h = (int)(time / (60 * 60)) % 24;
        return String.format("%d:%02d:%02d", h, m, s);

    }



    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // all permissions were granted
                initialize();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_routing, menu);
        return true;
    }
    // MapMarker mapMarker=new MapMarker();
    //  MapMarker mapMarker1 = mapMarker.setIcon(mark1);
    // Functionality for taps of the "Get Directions" button
    public void getDirections(View view)  {
        // 1. clear previous results
//        textViewResult.setText("");

        if (map != null && mapRoute != null) {
            map.removeMapObject(mapRoute);
            mapRoute = null;
        }
        //private void initialize() {
//            setContentView(R.layout.activity_main);

        // Search for the map fragment to finish setup by calling init().
        mapFragment = getSupportMapFragment();
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // retrieve a reference of the map from the map fragment

                    // Set the map zoom level to the average between min and max (no animation)
                    PositioningManager.OnPositionChangedListener positionListener
                            = new PositioningManager.OnPositionChangedListener() {
                        @Override
                        public void onPositionUpdated(PositioningManager.LocationMethod locationMethod, GeoPosition geoPosition, boolean b) {
                            //
                            //         textViewResult.setText("");
                            GeoCoordinate coords ;
                            coords = geoPosition.getCoordinate();
                            System.out.println("coors " + coords);
                            if (map != null && mapRoute == null) {
                                //map.removeMapObject(mapRoute);
                                //mapRoute = null;


                            }
                            map = mapFragment.getMap();
                            // Set the map center coordinate to the Vancouver region (no animation)
//                    map.setCenter(new GeoCoordinate(coords[0]),
                            //                          Map.Animation.NONE);
                            map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
                            //MapMarker uMarker = new MapMarker();
                            //uMarker.setCoordinate(new GeoCoordinate(59.933900, 30.306474, 0.0));
                            //map.addMapObject(uMarker);
                            // 2. Initialize RouteManager
                            RouteManager routeManager = new RouteManager();
                            Image image=new Image();
                            // 3. Select routing options
                            RoutePlan routePlan = new RoutePlan();
                            //    DynamicPenalty dynamicPenalty = new DynamicPenalty();
                            //  dynamicPenalty.setTrafficPenaltyMode(Route.TrafficPenaltyMode.OPTIMAL);
                            // coreRouter.setDynamicPenalty(dynamicPenalty);
                            RouteOptions routeOptions = new RouteOptions();
                            routeOptions.setTransportMode(RouteOptions.TransportMode.PEDESTRIAN);
                            routeOptions.setRouteType(RouteOptions.Type.FASTEST);
                            routePlan.setRouteOptions(routeOptions);
                            MapMarker mMark1=new MapMarker();
                            //mMark1.setCoordinate(new GeoCoordinate(coords));
                            // map.addMapObject(mMark1);
                            //                  System.out.println("coors " + coords[0]);
                            //              System.out.print("Coords"+ coords[0].getLatitude());
                            routePlan.addWaypoint(new GeoCoordinate(coords));
                            // map.setCenter(currentCoordinates,
                            //     Map.Animation.NONE);
                            map.getPositionIndicator().setVisible(true);
                            // 4. Select Waypoints for your routes
                            // START: Nokia, Burnaby
                            //mapMarker.getCoordinate();
                            //  mark1 = image(R.drawable.mark1);

                            // GeoCoordinate currentCoordinates = geoPosition.getCoordinate();
                            MapMarker mMark2=new MapMarker();
                            mMark2.setCoordinate(new GeoCoordinate( 59.934389, 30.324555, 0.0));
                            map.addMapObject(mMark2);
                            routePlan.addWaypoint(new GeoCoordinate(59.934389, 30.324555));
                            MapMarker mMark3=new MapMarker();
                            mMark3.setCoordinate(new GeoCoordinate( 59.940031, 30.312841, 0.0));
                            map.addMapObject(mMark3);
                            routePlan.insertWaypoint(new GeoCoordinate(59.940031, 30.312841), 1);
//                              map.addMapObject(mapRoute);
                            RouteManager.Error eRror = routeManager.calculateRoute(routePlan, routeManagerListener);
//           routeManagerListener.onCalculateRouteFinished(error,result);
                            //RouteManager.Error error = routeManager.calculateRoute(routePlan, routeManagerListener);
                            //          map.addMapObject(mpR);


                            if (eRror != RouteManager.Error.NONE) {
                                Toast.makeText(getApplicationContext(),
                                        "Route calculation failed with: " + eRror.toString(), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }

                        @Override
                        public void onPositionFixChanged(PositioningManager.LocationMethod locationMethod, PositioningManager.LocationStatus locationStatus) {

                        }

                    };
                    // System.out.println("coors " + coords);



                    posManager = PositioningManager.getInstance();
                    posManager.addListener(
                            new WeakReference<PositioningManager.OnPositionChangedListener>(positionListener));
                    boolean startSuccessful = posManager.start(
                            PositioningManager.LocationMethod.GPS_NETWORK);
                } else {
                    Log.e(LOG_TAG, "Cannot initialize SupportMapFragment (" + error + ")");
                }
            }
        });

        //  }
    }

    private RouteManager.Listener routeManagerListener = new RouteManager.Listener() {
        public void onCalculateRouteFinished(RouteManager.Error errorCode,
                                             List<RouteResult> result) {

            if (errorCode == RouteManager.Error.NONE && result.get(0).getRoute() != null) {
                // create a map route object and place it on the map
                mapRoute = new MapRoute(result.get(0).getRoute());
                map.addMapObject(mapRoute);

                // Get the bounding box containing the route and zoom in (no animation)
                GeoBoundingBox gbb = result.get(0).getRoute().getBoundingBox();
                map.zoomTo(gbb, Map.Animation.NONE, Map.MOVE_PRESERVE_ORIENTATION);
                //calculateTta();
                //calculateTtaUsingDownloadedTraffic();
//                textViewResult.setText(String.format(Locale.ROOT,"Route calculated with %d maneuvers.",
                //  result.get(0).getRoute().getManeuvers().size()));
            } else {
                //textViewResult.setText(
                //      String.format("Route calculation failed: %s", errorCode.toString()));
            }
        }

        public void onProgress(int percentage) {
//            textViewResult.setText(String.format("... %d percent done ...", percentage));
        }
    };

    private void calculateTtaUsingDownloadedTraffic() {
    }
/*
   private void calculateTta() {
            /*
             * Receive arrival time for the whole m_route, if you want to get time only for part of
             * m_route pass parameter in bounds 0 <= m_route.getSublegCount()
            final RouteTta ttaExcluding = mapRoute.getTtaExcludingTraffic(Route.WHOLE_ROUTE);
            final RouteTta ttaIncluding = mapRoute.getTtaIncludingTraffic(Route.WHOLE_ROUTE);
            final TextView tvInclude = findViewById(R.id.tvTtaInclude);
            tvInclude.setText("Tta included: " + String.valueOf(ttaIncluding.getDuration()));
            final TextView tvExclude = findViewById(R.id.tvTtaExclude);
            tvExclude.setText("Tta excluded: " + String.valueOf(ttaExcluding.getDuration()));
    }
*/
}