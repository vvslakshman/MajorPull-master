package majorpull.com.majorpull.user.Adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.AdapterYourTripUserBinding;
import majorpull.com.majorpull.user.Activity.YourTripDetailActivity;
import majorpull.com.majorpull.user.DataModel.YourTripepojo;

/**
 * Created by user102 on 3/22/18.
 */

public class YourTripAdapterUser extends RecyclerView.Adapter<YourTripAdapterUser.ViewHolder> {


    private AdapterYourTripUserBinding adapterYourTripUserBinding;

    private ArrayList<YourTripepojo> arrayList;
    private Context context;

    public YourTripAdapterUser(Context mcontext, ArrayList<YourTripepojo> marraylist) {
        this.arrayList = marraylist;
        this.context = mcontext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        adapterYourTripUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_your_trip_user, parent, false);

        return new ViewHolder(adapterYourTripUserBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        adapterYourTripUserBinding.tvDate.setText(arrayList.get(position).date);
        adapterYourTripUserBinding.tvScgeduled.setText(arrayList.get(position).scheduled);
        adapterYourTripUserBinding.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,YourTripDetailActivity.class));

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(AdapterYourTripUserBinding binding) {
            super(binding.getRoot());
        }

    }
}
