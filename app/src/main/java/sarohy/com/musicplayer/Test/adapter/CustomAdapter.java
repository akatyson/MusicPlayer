package sarohy.com.musicplayer.Test.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import sarohy.com.musicplayer.R;
import sarohy.com.musicplayer.Test.util.MediaItem;
import sarohy.com.musicplayer.Test.util.UtilFunctions;

public class CustomAdapter extends ArrayAdapter<MediaItem>{

	ArrayList<MediaItem> listOfSongs;
	Context context;
	LayoutInflater inflator;
	
	public CustomAdapter(Context context, int resource,	ArrayList<MediaItem> listOfSongs) {
		super(context, resource, listOfSongs);
		this.listOfSongs = listOfSongs;
		this.context = context;
		inflator = LayoutInflater.from(context);
	}

	private class ViewHolder{
		TextView textViewSongName, textViewArtist, textViewDuration;
	}
	
	ViewHolder holder;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View myView = convertView;
		if(convertView == null){
			myView = inflator.inflate(R.layout.custom_list, parent, false);
			holder = new ViewHolder();
			holder.textViewSongName = (TextView) myView.findViewById(R.id.textViewSongName);
			holder.textViewArtist = (TextView) myView.findViewById(R.id.textViewArtist);
			holder.textViewDuration = (TextView) myView.findViewById(R.id.textViewDuration);
			myView.setTag(holder);
		}else{
			holder = (ViewHolder)myView.getTag();
		}
		MediaItem detail = listOfSongs.get(position);
		holder.textViewSongName.setText(detail.toString());
		holder.textViewArtist.setText(detail.getAlbum() + " - " + detail.getArtist());
		holder.textViewDuration.setText((detail.getDuration()));
		return myView;
	}
}
