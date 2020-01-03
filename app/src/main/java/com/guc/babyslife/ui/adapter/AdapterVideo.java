package com.guc.babyslife.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.guc.babyslife.R;
import com.guc.babyslife.model.VideoInfo;
import com.guc.babyslife.ui.ActivityDetailInBrowser;
import com.guc.babyslife.utils.ImageUtils;
import com.guc.babyslife.utils.MyImageLoader;
import com.guc.babyslife.widget.CornerImageView;

import org.salient.artplayer.Comparator;
import org.salient.artplayer.MediaPlayerManager;
import org.salient.artplayer.OnWindowDetachedListener;
import org.salient.artplayer.VideoView;
import org.salient.artplayer.ui.ControlPanel;

import java.util.List;

/**
 * Created by guc on 2020/01/03.
 * 描述：专家视点适配器
 */
public class AdapterVideo extends CommonRecycleAdapter<VideoInfo> {

    private Comparator mComparator = new Comparator() {
        @Override
        public boolean compare(VideoView videoView) {
            try {
                Object currentData = MediaPlayerManager.instance().getCurrentData();
                //By comparing the position on the list to distinguish whether the same video
                //根据列表位置识别是否同一个视频
                if (currentData != null && videoView != null) {
                    Object data = videoView.getData();
                    return data != null
                            && currentData instanceof VideoInfo
                            && data instanceof VideoInfo
                            && ((VideoInfo) currentData).listPosition == ((VideoInfo) data).listPosition;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    };
    private OnWindowDetachedListener mOnWindowDetachedListener = null;

    public AdapterVideo(Context context, List<VideoInfo> dataList) {
        super(context, dataList, R.layout.item_video);
        openTiny();
//        MediaPlayerManager.instance().setMediaPlayer(new IjkPlayer());
//        MediaPlayerManager.instance().releasePlayerAndView(context);
//        MediaPlayerManager.instance().setMediaPlayer(new SystemMediaPlayer());
    }

    @Override
    public CommonViewHolder createViewHolder(int layoutId, @NonNull ViewGroup parent, int viewType) {
        View itemView = viewType == 0 ? mLayoutInflater.inflate(mLayoutId, parent, false) : mLayoutInflater.inflate(R.layout.item_knowledge, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void bindData(CommonViewHolder holder, VideoInfo data, int position) {
        data.listPosition = position;
        if (getItemViewType(position) == 0) {
            holder.setText(R.id.tv_title, data.title);
            VideoView videoView = holder.getView(R.id.video_view);
            videoView.setControlPanel(new ControlPanel(mContext));
            videoView.setComparator(mComparator);
            videoView.setOnWindowDetachedListener(mOnWindowDetachedListener);
            videoView.setUp(data.videoUrl, VideoView.WindowType.LIST, data);
            //设置预览图
            ImageView coverView = ((ControlPanel) videoView.getControlPanel()).findViewById(R.id.video_cover);
            MyImageLoader.display(mContext, data.videoUrl, coverView);
        } else {
            holder.setText(R.id.tv_title, data.title);
            holder.setText(R.id.tv_date, data.date);
            CornerImageView coverView = holder.getView(R.id.iv_picture);
            MyImageLoader.display(mContext, data.image, coverView);
            setOnItemClickListener((RecyclerView.Adapter adapter, View view, int p) ->
                    ActivityDetailInBrowser.showDetail(mContext, mDataList.get(p).videoUrl));
        }

    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).type;
    }

    public void setDetachAction(OnWindowDetachedListener onWindowDetachedListener) {
        mOnWindowDetachedListener = onWindowDetachedListener;
    }

    private void openTiny() {
        this.setDetachAction(new OnWindowDetachedListener() {
            @Override
            public void onDetached(VideoView videoView) {
                //开启小窗
                VideoView tinyVideoView = new VideoView(videoView.getContext());
                //set url and data
                tinyVideoView.setUp(videoView.getDataSourceObject(), VideoView.WindowType.TINY, videoView.getData());
                //set control panel
                ControlPanel controlPanel = new ControlPanel(videoView.getContext());
                tinyVideoView.setControlPanel(controlPanel);
                //set cover
                ImageView coverView = controlPanel.findViewById(R.id.video_cover);
                MyImageLoader.display(mContext, ((VideoInfo) videoView.getData()).videoUrl, coverView);
                //set parent
                tinyVideoView.setParentVideoView(videoView);
                //set LayoutParams
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(16 * 45, 9 * 45);
                layoutParams.gravity = Gravity.TOP | Gravity.CENTER;
                layoutParams.setMargins(0, ImageUtils.dp2px(mContext, 110), 0, 0);
                //start tiny window
                tinyVideoView.startTinyWindow(layoutParams);
            }
        });
    }

    private void closePlay() {
        this.setDetachAction(new OnWindowDetachedListener() {
            @Override
            public void onDetached(VideoView videoView) {
                MediaPlayerManager.instance().releasePlayerAndView(mContext);
            }
        });
    }
}
