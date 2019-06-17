package majorpull.com.majorpull.user.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ResultResponse.Date;
import majorpull.com.majorpull.R;

/**
 * Created by user102 on 4/10/18.
 */

public class CountryAdapter extends
        RecyclerView.Adapter<CountryAdapter.CountryViewHolder> implements Filterable {

    private List<Date> mList;
    private Context mcontext;
    private OnclickListener mOnclickListener;
    private List<Date> filteredist;
    private CountryFilter mCountryFilter;

    public CountryAdapter(Context mcontext, List<Date> mList) {
        this.mcontext = mcontext;
        this.mList = mList;
        this.filteredist = mList;
    }

    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_country, parent, false);
        return new CountryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CountryViewHolder holder, int position) {
        holder.tv_countryname.setText(mList.get(position).getName().toString());
        holder.tv_countrycode.setText(mList.get(position).getPhonecode().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclickListener != null) {
                    mOnclickListener.Onposclick(holder.getPosition());
                }
            }
        });

        if (mList.get(position).is_checked()) {
            holder.checked.setVisibility(View.VISIBLE);
        } else {
            holder.checked.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setOnClickListener(OnclickListener mOnclickListener) {
        this.mOnclickListener = mOnclickListener;
    }

    @Override
    public Filter getFilter() {
        if (mCountryFilter == null)
            mCountryFilter = new CountryFilter(this, mList);
        return mCountryFilter;
    }


    public interface OnclickListener {
        public void Onposclick(int pos);
    }

    /*Meetup View Holder*/
    public class CountryViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_countryname, tv_countrycode;
        public ImageView checked;
        public LinearLayout llparent;

        public CountryViewHolder(View view) {
            super(view);
            llparent = (LinearLayout) view.findViewById(R.id.llparent);
            tv_countryname = (TextView) view.findViewById(R.id.tv_username);
            tv_countrycode = (TextView) view.findViewById(R.id.tv_countrycode);
            checked = (ImageView) view.findViewById(R.id.checked);
        }
    }

    private static class CountryFilter extends Filter {

        private final CountryAdapter adapter;
        private List<Date> originalList = new ArrayList<>();
        private List<Date> filteredList;

        private CountryFilter(CountryAdapter adapter, List<Date> originalList) {
            super();
            this.adapter = adapter;
            this.originalList.addAll(originalList);
            this.filteredList = new ArrayList<>();
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //Here you have to implement filtering way
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Date user : originalList) {
                    if (user.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(user);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredist.clear();
            adapter.filteredist.addAll((ArrayList<Date>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
