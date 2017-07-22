package sarohy.com.musicplayer.Test;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sarohy.com.musicplayer.R;
import sarohy.com.musicplayer.Test.util.AlbumItem;
import sarohy.com.musicplayer.Test.util.BitmapWorkerTask;
import sarohy.com.musicplayer.Test.util.MediaItem;

public class AlbumFragment extends Fragment  implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SongSelection mListener;
    private ArrayList<AlbumItem> albumsList;
    private AlbumAdapter mAdapter;
    private Uri albumsUri;
    private RecyclerView recyclerViewAlbums;
    private RecyclerView recyclerViewSongs;

    public AlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(String param1, String param2) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        albumsUri=MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        getLoaderManager().initLoader(0, null, this);
        albumsList=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_album, container, false);
        recyclerViewAlbums=v.findViewById(R.id.recycler_view_album);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAlbums.setLayoutManager(mLayoutManager);
        recyclerViewAlbums.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSongs=v.findViewById(R.id.recycler_view_songs);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewSongs.setLayoutManager(mLayoutManager);
        recyclerViewSongs.setItemAnimator(new DefaultItemAnimator());
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (recyclerViewSongs.getVisibility()==View.VISIBLE){
                        recyclerViewSongs.setVisibility(View.GONE);
                        recyclerViewAlbums.setVisibility(View.VISIBLE);
                    }
                    else {
                        getActivity().finish();
                    }
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       mListener= (SongSelection) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new android.support.v4.content.CursorLoader(getActivity(), MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{ MediaStore.Audio.AlbumColumns.ALBUM,MediaStore.Audio.Albums._ID,
                        MediaStore.Audio.AlbumColumns.ARTIST},
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {

            do {
                AlbumItem mAlbumItem = new AlbumItem();
                mAlbumItem.setAlbumID(data.getInt(data.getColumnIndex(MediaStore.Audio.Albums._ID)));
                mAlbumItem.setAlbumSingerName(data.getString(data.getColumnIndex( MediaStore.Audio.AlbumColumns.ARTIST)));
                mAlbumItem.setAlbumTitle(data.getString(data.getColumnIndex( MediaStore.Audio.AlbumColumns.ALBUM)));
                //mAlbumItem.setAlbumTotalSongs(data.getInt(data.getColumnIndex(MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS)));
                albumsList.add(mAlbumItem);
            }
            while (data.moveToNext());
        }
        mAdapter = new AlbumAdapter(albumsList);
        recyclerViewAlbums.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {

        private List<AlbumItem> mediaList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvAlbumTitle, tvAlbumArtist, tvNoOfSong;
            public ImageView ivImage;

            public MyViewHolder(View view) {
                super(view);
                ivImage=view.findViewById(R.id.img_albumart);
                ivImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ivImage
                        .setLayoutParams(new LinearLayout.LayoutParams(270, 270));
                tvAlbumTitle = (TextView) view.findViewById(R.id.tv_title);
                tvAlbumArtist = (TextView) view.findViewById(R.id.tv_artist);
                tvNoOfSong = (TextView) view.findViewById(R.id.tv_no_of_songs);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int itemPosition = recyclerViewAlbums.getChildLayoutPosition(view);
                        AlbumItem album = albumsList.get(itemPosition);
                        String selection = "is_music != 0";

                        if (album.getAlbumID() > 0) {
                            selection = selection + " and album_id = " + album.getAlbumID();
                        }
                        Cursor c = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Media.TITLE,
                                MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.DURATION,
                                MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.ALBUM_ID,MediaStore.Audio.Media.COMPOSER},MediaStore.Audio.Albums.ALBUM + " = ?",new String[]{String.valueOf(album.getAlbumTitle())}, null);

                        ArrayList<MediaItem> listOfSongs = new ArrayList<MediaItem>();
                        c.moveToFirst();
                        while(c.moveToNext()){
                            MediaItem songData = new MediaItem();

                            String title = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
                            String artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                            String album1 = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                            long duration = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DURATION));
                            String data = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
                            long albumId = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                            String composer = c.getString(c.getColumnIndex(MediaStore.Audio.Media.COMPOSER));

                            songData.setTitle(title);
                            songData.setAlbum(album1);
                            songData.setArtist(artist);
                            songData.setDuration(duration);
                            songData.setPath(data);
                            songData.setAlbumId(albumId);
                            songData.setComposer(composer);
                            listOfSongs.add(songData);
                        }
                        c.close();
                        Log.d("SIZE", "SIZE: " + listOfSongs.size());
                        recyclerViewAlbums.setVisibility(View.GONE);
                        recyclerViewSongs.setVisibility(View.VISIBLE);
                        SongAdapter mSongAdapter = new SongAdapter(listOfSongs);
                        recyclerViewSongs.setAdapter(mSongAdapter);
                    }
                });
            }
        }
        public AlbumAdapter(List<AlbumItem> moviesList) {
            this.mediaList = moviesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_album_listview, parent, false);

            return new MyViewHolder(itemView);
        }


        public void loadBitmap (String albumId, ImageView mImage)
        {
            BitmapWorkerTask mTask = new BitmapWorkerTask(mImage,getActivity());
            mTask.execute(albumId);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            AlbumItem audio = mediaList.get(position);
            if (audio.getAlbumID()!=0)
            {
                //set the current album's art on the background thread. by an asynctask
                loadBitmap(String.valueOf(audio.getAlbumID()),holder.ivImage);
            }
            else
            {
                holder.ivImage.setImageResource(R.drawable.ic_menu_gallery);
                holder.ivImage.setAlpha(0.5f);

            }
            holder.tvAlbumTitle.setText(audio.getAlbumTitle());
            holder.tvAlbumArtist.setText(audio.getAlbumSingerName());
            holder.tvNoOfSong.setText(String.valueOf(audio.getAlbumTotalSongs()));
        }

        @Override
        public int getItemCount() {
            return mediaList.size();
        }
    }
    public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {

        private ArrayList<MediaItem> audioList;

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
                        MediaItem movie = audioList.get(itemPosition);
                        Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                        mListener.songSelected(audioList,itemPosition);
                    }
                });
            }
        }
        public SongAdapter(ArrayList<MediaItem> moviesList) {
            this.audioList = moviesList;
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
            MediaItem audio = audioList.get(position);
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
            return audioList.size();
        }
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
    public interface SongSelection {
        // TODO: Update argument type and name
        public void songSelected(ArrayList<MediaItem> audioList, int position);
    }
}
