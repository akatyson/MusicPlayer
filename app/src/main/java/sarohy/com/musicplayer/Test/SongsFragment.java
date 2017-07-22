package sarohy.com.musicplayer.Test;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sarohy.com.musicplayer.R;
import sarohy.com.musicplayer.Test.util.BitmapWorkerTask;
import sarohy.com.musicplayer.Test.util.MediaItem;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SongsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SongsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongsFragment extends Fragment  implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    RecyclerView recyclerViewSongs;
    private SongAdapter mAdapter;
    private Uri albumsUri;
    private ArrayList<MediaItem> songList;
    private SongSelection selected;

    public SongsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        selected= (SongSelection) context;
    }

    public static SongsFragment newInstance(String param1, String param2) {
        SongsFragment fragment = new SongsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        getLoaderManager().initLoader(0, null, this);
        songList=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_songs, container, false);
        recyclerViewSongs=v.findViewById(R.id.recycler_view_songs);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewSongs.setLayoutManager(mLayoutManager);
        recyclerViewSongs.setItemAnimator(new DefaultItemAnimator());
        return v;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new android.support.v4.content.CursorLoader(getActivity(), albumsUri,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.COMPOSER},
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {

            do {
                MediaItem songData = new MediaItem();
                String title = data.getString(data.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = data.getString(data.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = data.getString(data.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                long duration = data.getLong(data.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String data1 = data.getString(data.getColumnIndex(MediaStore.Audio.Media.DATA));
                long albumId = data.getLong(data.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String composer = data.getString(data.getColumnIndex(MediaStore.Audio.Media.COMPOSER));
                songData.setTitle(title);
                songData.setAlbum(album);
                songData.setArtist(artist);
                songData.setDuration(duration);
                songData.setPath(data1);
                songData.setAlbumId(albumId);
                songData.setComposer(composer);
                //songData.setAlbumArt(data.getString(data.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART)));
                songList.add(songData);
            } while (data.moveToNext());
        }
        mAdapter = new SongAdapter(songList);
        recyclerViewSongs.setAdapter(mAdapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    public interface SongSelection{
        public void songSelected(ArrayList<MediaItem> audioList,int position);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {

        private List<MediaItem> moviesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvTitle, tvArtist, tvDuration;
            public ImageView ivImage;

            public MyViewHolder(View view) {
                super(view);
                ivImage=view.findViewById(R.id.img_albumart);
                ivImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ivImage
                        .setLayoutParams(new LinearLayout.LayoutParams(270, 270));
                tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvArtist = (TextView) view.findViewById(R.id.tv_artist);
                tvDuration = (TextView) view.findViewById(R.id.tv_duration);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int itemPosition = recyclerViewSongs.getChildLayoutPosition(view);
                        MediaItem movie = songList.get(itemPosition);
                        Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                        selected.songSelected(songList,itemPosition);
                    }
                });
            }
        }
        public SongAdapter(List<MediaItem> moviesList) {
            this.moviesList = moviesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_song_listview, parent, false);

            return new MyViewHolder(itemView);
        }

        public void loadBitmap (String albumId, ImageView mImage)
        {
            BitmapWorkerTask mTask = new BitmapWorkerTask(mImage,getActivity());
            mTask.execute(albumId);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            MediaItem audio = moviesList.get(position);
            if (audio.getAlbumId()!=0)
            {
                //set the current album's art on the background thread. by an asynctask
                loadBitmap(String.valueOf(audio.getAlbumId()),holder.ivImage);
            }
            else
            {
                holder.ivImage.setImageResource(R.drawable.ic_menu_gallery);
                holder.ivImage.setAlpha(0.5f);

            }
            holder.tvTitle.setText(audio.getTitle());
            holder.tvArtist.setText(audio.getArtist());
            holder.tvDuration.setText(audio.getDuration());
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }

}
