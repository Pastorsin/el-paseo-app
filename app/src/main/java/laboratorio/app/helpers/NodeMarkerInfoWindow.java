package laboratorio.app.helpers;

import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import laboratorio.app.R;
import laboratorio.app.fragments.forms.purchase.items.NodeFormFragment;
import laboratorio.app.models.AvailableNode;

public class NodeMarkerInfoWindow extends MarkerInfoWindow {
    private static int layoutResId = R.layout.node_map_infowindow;
    private static NodeFormFragment hostFragment;
    private final List<AvailableNode> schedules;

    public NodeMarkerInfoWindow(MapView mapView, NodeFormFragment hostFragment, List<AvailableNode> schedules) {
        super(layoutResId, mapView);
        this.hostFragment = hostFragment;
        this.schedules = schedules;

        initTraceRouteButton();
        initScheduleRadioButtons();
    }

    private void initScheduleRadioButtons() {
        RadioGroup schedulesRadioGroup = getView().findViewById(R.id.schedules_radio_group);
        schedules.forEach(schedule -> {
            RadioButton radioButton = new RadioButton(hostFragment.getContext());

            String scheduleDay = getScheduleDay(schedule);

            radioButton.setText(String.format("%s desde %s a %s", scheduleDay,
                    schedule.getDateTimeFrom(), schedule.getDateTimeTo()));
            radioButton.setTextSize(10);
            radioButton.setTypeface(Typeface.create("lato", Typeface.NORMAL));

            schedulesRadioGroup.addView(radioButton);
        });

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
