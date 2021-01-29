package laboratorio.app.fragments.forms;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import laboratorio.app.R;
import laboratorio.app.viewmodels.AddressViewModel;


abstract public class AddressFormFragment extends FormFragment {

    private Executor executor = Executors.newSingleThreadExecutor();
    private static final GeoPoint DEFAULT_START_POINT = new GeoPoint(-34.9214, -57.9544);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        initMap(view);

        view.findViewById(R.id.signup_address_search_button).setOnClickListener(
                v -> executor.execute(onClickSearchButton(view)));

        getViewModel().addressResponse.observe(getViewLifecycleOwner(), onAddressChanges(view));

        return view;
    }

    private void initMap(View view) {
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
        MapView mapview = view.findViewById(R.id.map);
        mapview.setTileSource(TileSourceFactory.MAPNIK);

        IMapController mapController = mapview.getController();

        executor.execute(() -> {
            GeoPoint point;
            mapController.setZoom(15.0);

            if (getViewModel().addressResponse.getValue() == null) {
                point = DEFAULT_START_POINT;
            } else {
                Address address = getViewModel().addressResponse.getValue();
                point = new GeoPoint(address.getLatitude(), address.getLongitude());
                showMarkerOnMap(mapview, point);
            }

            mapController.setCenter(point);
        });
    }

    @NotNull
    private Runnable onClickSearchButton(View view) {
        EditText streetInput = view.findViewById(R.id.signup_address_street);
        EditText neighborhoodInput = view.findViewById(R.id.signup_address_type);

        String street = streetInput.getText().toString();
        String neighborhood = neighborhoodInput.getText().toString();


        return () -> {
            try {
                List<Address> addresses = fetchAddresses(street, neighborhood);

                if (addresses.isEmpty()) {
                    getActivity().runOnUiThread(() -> showEmptyResultsMessage());

                } else {
                    Address address = addresses.get(0);

                    getActivity().runOnUiThread(() -> {
                        getViewModel().addressRequest.setValue(getAddressToSearch(street, neighborhood));
                        getViewModel().addressResponse.setValue(address);
                    });
                }

            } catch (IOException e) {
                getActivity().runOnUiThread(() -> showNetworkError());
            }
        };
    }

    @NotNull
    private Observer<Address> onAddressChanges(View view) {
        return address -> {
            MapView mapview = view.findViewById(R.id.map);
            GeoPoint point = new GeoPoint(address.getLatitude(), address.getLongitude());

            deleteMarkersOnMap(mapview);
            animateToPoint(mapview, point);
            showMarkerOnMap(mapview, point);
        };
    }

    @NotNull
    private String getAddressToSearch(String street, String neighborhood) {
        return street + " " + neighborhood;
    }

    private void showEmptyResultsMessage() {
        Toast.makeText(getContext(), R.string.empty_address_results, Toast.LENGTH_SHORT).show();
    }

    private void showNetworkError() {
        Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
    }

    private List<Address> fetchAddresses(String street, String neighborhood) throws IOException {
        GeocoderNominatim geocoder = new GeocoderNominatim("OSMBonusPackTutoUserAgent");

        try {
            String addressName = getAddressToSearch(street, neighborhood);
            List<Address> addresses = geocoder.getFromLocationName(addressName, 1);
            return addresses;
        } catch (IOException e) {
            Log.e("Error", "Network error to fetch addresses", e);
            throw e;
        }
    }

    protected void showMarkerOnMap(MapView mapview, GeoPoint point) {
        Drawable markerIcon = getActivity().getDrawable(R.drawable.marker_default);

        Marker marker = new Marker(mapview);
        marker.setPosition(point);
        marker.setIcon(markerIcon);

        mapview.getOverlays().add(marker);
    }

    private void animateToPoint(MapView mapview, GeoPoint point) {
        mapview.getController().animateTo(point);
        mapview.getController().setZoom(18.0);
        mapview.getController().setCenter(point);
    }

    private void deleteMarkersOnMap(MapView mapview) {
        mapview.getOverlays().clear();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_address_form;
    }

    @Override
    protected void initValidators(View view) {
        View addressLayout = view.findViewById(R.id.signup_address_layout);

        EditText streetAddressInput = view.findViewById(R.id.signup_address_street);
        EditText neighborhoodAddressInput = view.findViewById(R.id.signup_address_type);

        validator.addValidation(addressLayout,
                validationHolder -> {
                    String street = streetAddressInput.getText().toString();
                    String neighborhood = neighborhoodAddressInput.getText().toString();

                    String addressInput = getAddressToSearch(street, neighborhood);
                    String addressLastSearch = getViewModel().addressRequest.getValue();

                    return addressInput.equals(addressLastSearch) &&
                            getViewModel().addressResponse.getValue() != null;
                }, validationHolder -> {
                    streetAddressInput.setError(getString(R.string.address_error));
                    neighborhoodAddressInput.setError(getString(R.string.address_error));
                }, validationHolder -> {
                    streetAddressInput.setError(null);
                    neighborhoodAddressInput.setError(null);
                }, getString(R.string.address_error));
    }

    abstract protected AddressViewModel getViewModel();
}