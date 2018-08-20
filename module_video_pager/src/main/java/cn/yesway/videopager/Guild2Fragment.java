package cn.yesway.videopager;

import android.net.Uri;
import android.util.Log;


public class Guild2Fragment extends LazyLoadFragment {
    private CustomVideoView customVideoView;
    private int index;

    @Override
    protected int setContentView() {
        return R.layout.video_fm_guild;
    }

    @Override
    protected void lazyLoad() {

        customVideoView = findViewById(R.id.cv);
        /**获取参数，根据不同的参数播放不同的视频**/
        index = getArguments().getInt("index");
        Log.d("Guild2Fragment","开始播放视频="+index);
        Uri uri;
        if (index == 1) {
            uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.test1);
        } else if (index == 2) {
            uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.test2);
        } else {
            uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.test3);
        }
        /**播放视频**/
        customVideoView.playVideo(uri);
    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
        if (customVideoView != null) {
            customVideoView.stopPlayback();
        }
    }
}
