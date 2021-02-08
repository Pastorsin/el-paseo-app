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
import laboratorio.app.viewmodels.Ubication;


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

            if (getViewModel().isUbicationDefined()) {
                Ubication ubication = getViewModel().ubication.getValue();
                point = new GeoPoint(ubication.getLatitude(), ubication.getLongitude());
                showMarkerOnMap(view, point);
            } else {
                point = DEFAULT_START_POINT;
            }

            mapController.setCenter(point);
        });
    }

    @NotNull
    private Runnable onClickSearchButton(View view) {
        return () -> {
            String addressName = getViewModel().getFullAddressName();
            try {
                List<Address> addresses = fetchAddresses(addressName);

                if (addresses.isEmpty()) {
                    getActivity().runOnUiThread(() -> showEmptyResultsMessage());

                } else {
                    Address address = addresses.get(0);
                    GeoPoint point = new GeoPoint(address.getLatitude(), address.getLongitude());

                    getActivity().runOnUiThread(() -> {
                        getViewModel().lastAddressSearch.setValue(addressName);
                        Ubication ubication = new Ubication(point.getLatitude(), point.getLongitude());
                        getViewModel().ubication.setValue(ubication);

                        showMarkerOnMap(view, point);
                    });
                }

            } catch (IOException e) {
                getActivity().runOnUiThread(() -> showNetworkError());
            }
        };
    }

    private void showEmptyResultsMessage() {
        Toast.makeText(getContext(), R.string.empty_address_results, Toast.LENGTH_SHORT).show();
    }

    private void showNetworkError() {
        Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
    }

    private List<Address> fetchAddresses(String addressName) throws IOException {
        GeocoderNominatim geocoder = new GeocoderNominatim("OSMBonusPackTutoUserAgent");

        try {
            List<Address> addresses = geocoder.getFromLocationName(addressName, 1);
            return addresses;
        } catch (IOException e) {
            Log.e("Error", "Network error to fetch addresses", e);
            throw e;
        }
    }

    protected void showMarkerOnMap(View view, GeoPoint point) {
        MapView mapview = view.findViewById(R.id.map);
        Drawable markerIcon = getActivity().getDrawable(R.drawable.marker_default);

        Marker marker = new Marker(mapview);
        marker.setPosition(point);
        marker.setIcon(markerIcon);

        deleteMarkersOnMap(mapview);

        animateToPoint(mapview, point);

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
        View addressLayout = view.findViewById(R.id.street_address_layout);
        EditText streetAddressInput = view.findViewById(R.id.signup_address_street);
        EditText neighborhoodAddressInput = view.findViewById(R.id.signup_address_type);
        EditText numberAddressInput = view.findViewById(R.id.signup_address_number);

        validator.addValidation(addressLayout,
                validationHolder -> getViewModel().isValidForm()
                , validationHolder -> {
                    streetAddressInput.setError(getString(R.string.address_error));
                    numberAddressInput.setError(getString(R.string.address_error));
                    neighborhoodAddressInput.setError(getString(R.string.address_error));
                }, validationHolder -> {
                    streetAddressInput.setError(null);
                    numberAddressInput.setError(null);
                    neighborhoodAddressInput.setError(null);
                }, getString(R.string.address_error));
    }

    abstract protected AddressViewModel getViewModel();
}