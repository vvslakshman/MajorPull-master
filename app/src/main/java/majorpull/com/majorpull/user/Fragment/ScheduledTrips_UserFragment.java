package majorpull.com.majorpull.user.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.FragmentScheduledtripsUserBinding;
import majorpull.com.majorpull.user.Adapter.YourTripAdapterUser;
import majorpull.com.majorpull.user.DataModel.YourTripepojo;

/**
 * Created by user102 on 3/22/18.
 */

public class ScheduledTrips_UserFragment extends Fragment {


    private FragmentScheduledtripsUserBinding scheduledtripsUserBinding;
    private View view;
    private Context mcontext;
    private YourTripAdapterUser adapterUser;

    private ArrayList<YourTripepojo> dataMethod() {

        ArrayList<YourTripepojo> arrayList = new ArrayList<>();
        arrayList.add(new YourTripepojo("Wednesday, 28 Feb 2018", "SCHEDULED"));
        arrayList.add(new YourTripepojo("Friday, 23 Feb 2018", "SCHEDULED"));
        arrayList.add(new YourTripepojo("Wednesday, 21 Feb 2018", "SCHEDULED"));
        arrayList.add(new YourTripepojo("Friday, 21 Feb 2018", "SCHEDULED"));
        arrayList.add(new YourTripepojo("Wednesday, 28 Feb 2018", "SCHEDULED"));
        arrayList.add(new YourTripepojo("Friday, 23 Feb 2018", "SCHEDULED"));
        arrayList.add(new YourTripepojo("Wednesday, 21 Feb 2018", "SCHEDULED"));
        arrayList.add(new YourTripepojo("Friday, 21 Feb 2018", "SCHEDULED"));

        return arrayList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        scheduledtripsUserBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_scheduledtrips_user, container, false);

        mcontext = container.getContext();
        view = scheduledtripsUserBinding.getRoot();
        scheduledtripsUserBinding.reccyleScheduledTrips.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));
        ArrayList<YourTripepojo> list = dataMethod();

        adapterUser = new YourTripAdapterUser(mcontext, list);
        scheduledtripsUserBinding.reccyleScheduledTrips.setAdapter(adapterUser);

        return view;
    }

}
