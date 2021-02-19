package laboratorio.app.helpers;

import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.fragments.forms.purchase.items.NodeFormFragment;
import laboratorio.app.models.Address;
import laboratorio.app.models.AvailableNode;
import laboratorio.app.models.Node;
import laboratorio.app.viewmodels.PurchaseViewModel;

public class NodeMarkerInfoWindow extends MarkerInfoWindow {
    private static final int LAYOUT_RES_ID = R.layout.node_map_infowindow;
    private static final int MARKER_ICON = R.drawable.marker_default;

    private NodeFormFragment hostFragment;
    private Node node;
    private List<AvailableNode> schedules;
    private Marker marker;
    private PurchaseViewModel viewmodel;

    public NodeMarkerInfoWindow(MapView mapView,
                                Marker marker,
                                NodeFormFragment hostFragment,
                                Node node, List<AvailableNode> schedules) {
        super(LAYOUT_RES_ID, mapView);

        this.hostFragment = hostFragment;
        this.marker = marker;
        this.node = node;
        this.schedules = schedules;
        this.viewmodel = new ViewModelProvider(hostFragment.getActivity()).get(PurchaseViewModel.class);

        initNode();
        initTraceRouteButton();
        initScheduleRadioButtons();
    }

    private void initNode() {
        marker.setTitle(getNodeTitle());
        marker.setPosition(getNodePosition());
        marker.setIcon(getNodeIcon());
        marker.setSnippet(getNodeDescription());
        marker.setSubDescription(getNodePhone());

        if (node.getImage() != null)
            marker.setImage(getNodeImage());
    }

    private String getNodeTitle() {
        return node.getName() == null ? "" : node.getName();
    }

    private String getNodeDescription() {
        return node.getDescription() == null ? "" : node.getDescription();
    }

    private Drawable getNodeIcon() {
        return ContextCompat.getDrawable(hostFragment.getContext(), MARKER_ICON);
    }

    @NotNull
    private GeoPoint getNodePosition() {
        Address nodeAddress = node.getAddress();
        return new GeoPoint(nodeAddress.getLatitude(), nodeAddress.getLongitude());
    }

    private String getNodePhone() {
        return node.getPhone() == null ? "" : node.getPhone();
    }

    @NotNull
    private BitmapDrawable getNodeImage() {
        return new BitmapDrawable(hostFragment.getResources(), node.getImage().bitmap());
    }


    private void initScheduleRadioButtons() {
        RadioGroup schedulesRadioGroup = getView().findViewById(R.id.schedules_radio_group);
        schedules.forEach(schedule -> {
            RadioButton radioButton = new RadioButton(hostFragment.getContext());

            stylingRadioButton(schedule, radioButton);

            radioButton.setChecked(isScheduleChecked(schedule));

            radioButton.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked)
                    viewmodel.chosenNodeSchedule.setValue(schedule);
            });

            viewmodel.chosenNodeSchedule.observe(hostFragment.getViewLifecycleOwner(), nodeSchedule ->
                radioButton.setChecked(isScheduleChecked(schedule))
            );

            schedulesRadioGroup.addView(radioButton);
        });

    }

    private boolean isScheduleChecked(AvailableNode schedule) {
        return schedule.equals(viewmodel.chosenNodeSchedule.getValue());
    }

    private void stylingRadioButton(AvailableNode schedule, RadioButton radioButton) {
        String scheduleDay = getScheduleDay(schedule);

        radioButton.setText(String.format("%s desde %s a %s", scheduleDay,
                schedule.getDateTimeFrom(), schedule.getDateTimeTo()));
        radioButton.setTextSize(10);
        radioButton.setTypeface(Typeface.create("lato", Typeface.NORMAL));
    }

    @NotNull
    private String getScheduleDay(AvailableNode schedule) {
        Locale locale = new Locale("es", "ES");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        return dateFormat.format(schedule.getDay());
    }

    private void initTraceRouteButton() {
        Button traceRouteButton = getView().findViewById(R.id.trace_route_button);
        traceRouteButton.setOnClickListener(onClickTraceRouteButton());
    }

    @NotNull
    private View.OnClickListener onClickTraceRouteButton() {
        return (buttonView) -> {
            hostFragment.traceRoute(getMarkerReference(), getMapView());
            this.close();
        };
    }

    @Override
    public void onOpen(Object item) {
        super.onOpen(item);
        InfoWindow.closeAllInfoWindowsOn(getMapView());
    }

}
