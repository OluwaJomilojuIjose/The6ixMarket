package com.example.the6ixmarket;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {

    private MapView map;
    private EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_main);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        searchInput = findViewById(R.id.search_input);


        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            String query = v.getText().toString();
            if (!query.isEmpty()) {
                performSearch(query);
            }
            return false;
        });
    }

    private void performSearch(String query) {

        GeoPoint userLocation = new GeoPoint(/* user's latitude */, /* user's longitude */);


        GeoPoint resultLocation = new GeoPoint(userLocation.getLatitude() + 0.01, userLocation.getLongitude() + 0.01);


        Marker marker = new Marker(map);
        marker.setPosition(resultLocation);
        marker.setTitle("Search Result: " + query);
        map.getOverlays().add(marker);

        map.getController().setCenter(resultLocation);
        map.getController().setZoom(15.0);
    }
}
