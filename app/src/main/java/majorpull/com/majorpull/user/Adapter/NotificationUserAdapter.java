package majorpull.com.majorpull.user.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import majorpull.com.majorpull.R;
import majorpull.com.majorpull.databinding.AdatperNotificationUserBinding;
import majorpull.com.majorpull.user.DataModel.NotificatonUserPojo;

/**
 * Created by user102 on 3/23/18.
 */

public class NotificationUserAdapter extends RecyclerView.Adapter<NotificationUserAdapter.ViewHolder> {


    private Context mcontext;
    private ArrayList<NotificatonUserPojo> marrayList;

    private AdatperNotificationUserBinding notificationUserBinding;


    public NotificationUserAdapter(Context context, ArrayList<NotificatonUserPojo> arrayList) {
        this.mcontext = context;
        this.marrayList = arrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.adatper_notification_user, parent, false);
//
//        return new ViewHolder(itemView);

        notificationUserBinding= DataBindingUtil.inflate(LayoutInflater.from(mcontext),
                R.layout.adatper_notification_user,parent, false);


        return new ViewHolder(notificationUserBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        notificationUserBinding.ivProfile.setImageResource(marrayList.get(position).profileImage);
        notificationUserBinding.tvDescription.setText(marrayList.get(position).description);
        notificationUserBinding.tvTitle.setText(marrayList.get(position).title);

    }

    @Override
    public int getItemCount() {
        return marrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(AdatperNotificationUserBinding binding) {
            super(binding.getRoot());
        }
    }
}
