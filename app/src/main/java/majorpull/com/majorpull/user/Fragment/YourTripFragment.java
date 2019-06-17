package majorpull.com.majorpull.user.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.FragmentYourTripBinding;
import majorpull.com.majorpull.user.Adapter.YourTripAdapterUser;
import majorpull.com.majorpull.user.DataModel.YourTripepojo;

/**
 * Created by user102 on 3/21/18.
 */

public class YourTripFragment extends Fragment {


    private View view;


    private YourTripAdapterUser adapterUser;
    private FragmentYourTripBinding yourTripBinding;
    private Context mcontext;

    private ArrayList<YourTripepojo> datamethod() {

        ArrayList<YourTripepojo> arrayList = new ArrayList<>();
        arrayList.add(new YourTripepojo("Wednesday, 28 Feb 2018", "SCHEDULED"));
        arrayList.add(new YourTripepojo("Friday, 23 Feb 2018", "SCHEDULED"));
        arrayList.add(new YourTripepojo("Wednesday, 21 Feb 2018", "$20"));
        arrayList.add(new YourTripepojo("Wednesday, 21 Feb 2018", "$50"));
        arrayList.add(new YourTripepojo("Wednesday, 21 Feb 2018", "$70"));
        arrayList.add(new YourTripepojo("Wednesday, 21 Feb 2018", "$30"));
        arrayList.add(new YourTripepojo("Wednesday, 21 Feb 2018", "$10"));

        return arrayList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        yourTripBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_your_trip, container, false);

        mcontext = container.getContext();
        view = yourTripBinding.getRoot();


        yourTripBinding.recylerviewYourtrip.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));
        ArrayList<YourTripepojo> list = datamethod();

        adapterUser = new YourTripAdapterUser(mcontext, list);

        yourTripBinding.recylerviewYourtrip.setAdapter(adapterUser);


        return view;


    }
}
