package com.example.jo265962.jsonlistview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jo265962.jsonlistview.nework.JsonRow;
import com.example.jo265962.jsonlistview.MainActivity;
import com.example.jo265962.jsonlistview.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JO265962 on 5/21/2016.
 */
public class ListJsonAdapter extends RecyclerView.Adapter<ListJsonAdapter.ViewHolder> {

    private MainActivity pParent;
    List<JsonRow> jsonData;
    private final Picasso mPicasso;

    public ListJsonAdapter(MainActivity context, List<JsonRow> dataSet) {
        pParent = context;
        jsonData = new ArrayList<>(dataSet);
        mPicasso = Picasso.with(context);
    }

    public void setJsonData(List<JsonRow> dataSet) {
        jsonData = new ArrayList<>(dataSet);
    }

    @Override
    public ListJsonAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_layout,viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListJsonAdapter.ViewHolder viewHolder, int position) {

        if (jsonData != null && jsonData.size() > 0) {
            viewHolder.tv_title.setText(jsonData.get(position).getTitle());
            viewHolder.tv_desc.setText(jsonData.get(position).getDescription());

            //load the image with picasso library
            mPicasso.load(jsonData.get(position).getImageURL())
                    .placeholder(R.drawable.no_image)
                    .resizeDimen(R.dimen.image_width, R.dimen.image_height)
                    .into(viewHolder.iv_imgURL);
            }
    }

    @Override
    public int getItemCount() {
        return jsonData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_title,tv_desc;
        ImageView iv_imgURL;
        public ViewHolder(View view) {
            super(view);

            tv_title = (TextView)view.findViewById(R.id.item_title);
            tv_desc = (TextView)view.findViewById(R.id.item_desc);
            iv_imgURL = (ImageView) itemView.findViewById(R.id.item_imgURL);

        }
    }
}
