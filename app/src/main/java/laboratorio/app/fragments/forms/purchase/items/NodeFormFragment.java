package laboratorio.app.fragments.forms.purchase.items;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import androidx.lifecycle.MutableLiveData;
import laboratorio.app.R;
import laboratorio.app.controllers.API;
import laboratorio.app.fragments.ErrorFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.helpers.NodeMarkerInfoWindow;
import laboratorio.app.models.Address;
import laboratorio.app.models.AvailableNode;
import laboratorio.app.models.General;
import laboratorio.app.models.Node;
import laboratorio.app.models.Pagination;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.stream.Collectors.groupingBy;

public class NodeFormFragment extends Fragment {

    private Executor executor = Executors.newSingleThreadExecutor();

    private static final GeoPoint START_POINT = new GeoPoint(-34.9214, -57.9544);
    private static final int MARKER_ICON = R.drawable.marker_default;
    private static final double INITIAL_MAP_ZOOM = 13.0;

    private GeoPoint currentUbication;
    private List<Overlay> roadOverlays = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_node_form, container, false);

        requestCurrentUbication();

        initMap(view);

        return view;
    }

    private void requestCurrentUbication() {
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!hasPermission()) {
            getActivity().requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            return;
        }

        @SuppressLint("MissingPermission")
        Location lastKnownLoc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (lastKnownLoc != null) {
            double longTemp = lastKnownLoc.getLongitude();
            double latTemp = lastKnownLoc.getLatitude();
            currentUbication = new GeoPoint(latTemp, longTemp);
        }
    }

    private boolean hasPermission() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED || (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void initMap(View view) {
        MapView mapview = view.findViewById(R.id.map);

        initMapConfiguration(mapview);

        initMapEvents(mapview);

        fetchNodes().observe(getViewLifecycleOwner(), nodes -> {
            if (nodes != null) {
                renderNodesOnMap(mapview, nodes);
            } else {
                loadFragment(new ErrorFragment());
            }
        });
    }

    private void initMapConfiguration(MapView mapview) {
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
        mapview.setTileSource(TileSourceFactory.MAPNIK);

        IMapController mapController = mapview.getController();
        mapController.setZoom(INITIAL_MAP_ZOOM);
        mapController.setCenter(START_POINT);
    }

    private void initMapEvents(MapView mapview) {
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                InfoWindow.closeAllInfoWindowsOn(mapview);
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        });
        mapview.getOverlays().add(0, mapEventsOverlay);
    }

    private void renderNodesOnMap(MapView mapview, Map<Node, List<AvailableNode>> nodes) {
        nodes.forEach((node, schedules) -> {
            Marker marker = new Marker(mapview);

            marker.setTitle(getNodeTitle(node));
            marker.setPosition(getNodePosition(node));
            marker.setIcon(getNodeIcon());
            marker.setSnippet(getNodeDescription(node));
            marker.setSubDescription(getNodePhone(node));

            if (node.getImage() != null)
                marker.setImage(getNodeImage(node));

            marker.setInfoWindow(new NodeMarkerInfoWindow(mapview, this, schedules));

            addMarkerOnMap(mapview, marker);
        });

        mapview.invalidate();
    }

    private String getNodePhone(Node node) {
        return node.getPhone() == null ? "" : node.getPhone();
    }

    @NotNull
    private BitmapDrawable getNodeImage(Node node) {
        return new BitmapDrawable(getResources(), node.getImage().bitmap());
    }

    @NotNull
    public void traceRoute(Marker marker, MapView mapview) {
        ArrayList<GeoPoint> waypoints = new ArrayList<>();

        if (currentUbication == null) {
            requestCurrentUbication();
            showRoadError();
            Log.e("UBICATION", "Permission not granted");
            return;
        }

        waypoints.add(currentUbication);
        waypoints.add(marker.getPosition());

        showRoadInfoProgress();

        fetchRoad(waypoints).observe(getViewLifecycleOwner(), road -> {
            if (road != null) {
                deleteAllRoutesOnMap(mapview);
                showRouteOnMap(mapview, road);
            } else {
                showRoadError();
            }
        });
    }

    private void deleteAllRoutesOnMap(MapView mapview) {
        mapview.getOverlays().removeAll(roadOverlays);
        roadOverlays.clear();
    }

    private void showRoadInfoProgress() {
        Toast.makeText(getContext(), "Trazando la ruta", Toast.LENGTH_SHORT).show();
    }

    private void showRouteOnMap(MapView mapView, Road road) {
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        roadOverlays.add(roadOverlay);
        roadOverlays.add(getRouteSteps(mapView, road));

        mapView.getOverlays().addAll(roadOverlays);
        mapView.invalidate();
    }

    private Overlay getRouteSteps(MapView mapView, Road road) {
        FolderOverlay roadMarkers = new FolderOverlay();

        Drawable nodeIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_navigation_24, null);
        for (int i = 0; i < road.mNodes.size(); i++) {
            RoadNode node = road.mNodes.get(i);
            Marker nodeMarker = new Marker(mapView);
            nodeMarker.setPosition(node.mLocation);
            nodeMarker.setIcon(nodeIcon);
            nodeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

            nodeMarker.setTitle("Step " + i);
            nodeMarker.setSnippet(node.mInstructions);
            nodeMarker.setSubDescription(Road.getLengthDurationText(getContext(), node.mLength, node.mDuration));
            Drawable iconContinue = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_continue, null);
            nodeMarker.setImage(iconContinue);

            roadMarkers.add(nodeMarker);
        }

        return roadMarkers;
    }

    private void showRoadError() {
        Toast.makeText(getContext(), getString(R.string.road_error), Toast.LENGTH_SHORT).show();
    }

    private MutableLiveData<Road> fetchRoad(ArrayList<GeoPoint> waypoints) {
        MutableLiveData<Road> response = new MutableLiveData<>();
        RoadManager roadManager = new OSRMRoadManager(getContext());

        executor.execute(() -> {
            final Road road = roadManager.getRoad(waypoints);

            if (road != null && road.mStatus == Road.STATUS_OK) {
                getActivity().runOnUiThread(() -> response.setValue(road));
                Log.d("FETCH MAP", "Fetch road successfully");
            } else {
                getActivity().runOnUiThread(() -> response.setValue(null));
                Log.e("FETCH MAP", "Error on fetch road");
            }
        });

        return response;
    }

    private String getNodeTitle(Node node) {
        return node.getName() == null ? "" : node.getName();
    }

    private String getNodeDescription(Node node) {
        return node.getDescription() == null ? "" : node.getDescription();
    }

    private void addMarkerOnMap(MapView mapview, Marker marker) {
        mapview.getOverlays().add(marker);
    }

    private Drawable getNodeIcon() {
        return ContextCompat.getDrawable(getContext(), MARKER_ICON);
    }

    @NotNull
    private GeoPoint getNodePosition(Node node) {
        Address nodeAddress = node.getAddress();
        return new GeoPoint(nodeAddress.getLatitude(), nodeAddress.getLongitude());
    }

    private void loadFragment(Fragment fragment) {
        FragmentLoader loader = (FragmentLoader) getActivity();
        loader.replaceFragment(fragment, R.id.fragment_node_map_container);
    }


    private MutableLiveData<Map<Node, List<AvailableNode>>> fetchNodes() {
        MutableLiveData<Map<Node, List<AvailableNode>>> nodesResponse = new MutableLiveData<>();

        API.instance.getService().getGeneral().enqueue(new Callback<Pagination<General>>() {
            @Override
            public void onResponse(Call<Pagination<General>> call, Response<Pagination<General>> response) {
                if (response.isSuccessful()) {
                    List<General> general = response.body().getPage();

                    Set<AvailableNode> allAvailableNodes = general.stream()
                            .flatMap(g -> g.getActiveNodes().stream())
                            .collect(Collectors.toSet());

                    Map<Node, List<AvailableNode>> nodeSchedules = allAvailableNodes.stream().collect(
                            groupingBy(AvailableNode::getNode)
                    );

                    nodesResponse.setValue(nodeSchedules);
                    Log.d("GET NODES", String.format("Fetch nodes successfully %s", nodeSchedules));
                } else {
                    Log.e("GET NODES", String.format("Error on fetch nodes %s", response.code()));
                    nodesResponse.setValue(null);
                }

            }

            @Override
            public void onFailure(Call<Pagination<General>> call, Throwable t) {
                Log.e("GET NODES", "Network error", t);
                nodesResponse.setValue(null);
            }
        });

        return nodesResponse;
    }

}