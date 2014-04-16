package com.video.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.video.R;

public class MessageItemAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private File imageCache = null;

	public MessageItemAdapter(Context context, File imageCache, ArrayList<HashMap<String, String>> list) {
		this.context = context;
		this.imageCache = imageCache;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private int getReadedState(String state) {
		int result = -1;
		if (state.equals("true")) {
			result = View.INVISIBLE;
		} else {
			result = View.VISIBLE;
		}
		return result;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.msg_alert_item, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_msg_image);
			holder.iv_state = (ImageView) convertView.findViewById(R.id.iv_msg_state);
			holder.msg_event = (TextView) convertView.findViewById(R.id.tv_msg_event);
			holder.msg_mac = (TextView) convertView.findViewById(R.id.tv_msg_mac);
			holder.msg_time = (TextView) convertView.findViewById(R.id.tv_msg_time);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.iv_state.setVisibility(getReadedState(list.get(position).get("isReaded")));
		holder.msg_event.setText(list.get(position).get("msgEvent"));
		holder.msg_mac.setText(list.get(position).get("msgMAC"));
		holder.msg_time.setText(list.get(position).get("msgTime"));
		
		String path = "http://222.174.213.185:8088/plugin/2014-04-09/10021_0_20140403192854_000_23.jpg";
		AsyncImageTask task = new AsyncImageTask(holder.iv_image, path);
		task.execute();

		holder.iv_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "Adapter�е����"+position, Toast.LENGTH_SHORT).show();
			}
		});
		return convertView;
	}

	static class ViewHolder {
		ImageView iv_image;
		ImageView iv_state;
		TextView msg_event;
		TextView msg_mac;
		TextView msg_time;
	}
	
	/**
	 * �첽����ͼƬ��
	 */
	private final class AsyncImageTask extends AsyncTask<String, Integer, Uri> {
		private ImageView imageView;
		private String imagePath;
		
		public AsyncImageTask(ImageView imageView, String path) {
			this.imageView = imageView;
			this.imagePath = path;
		}

		//��̨���е����߳�
		@Override
		protected Uri doInBackground(String... params) {
			try {
				return getCacheImageUri(imagePath, imageCache);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		//���½�����ʾ
		@Override
		protected void onPostExecute(Uri result) {
			super.onPostExecute(result);
			if (imageView != null && result != null) {
				System.out.println("MyDebug: ����ʾͼƬ��");
				imageView.setImageURI(result);
			}
		}
	}
	
	/**
	 * ���ͼƬ�����Uri·��
	 * @param path ������ͼƬ��·��
	 * @param cache ���ػ����ļ���
	 * @return ���ػ����Uri·��
	 * @throws Exception 
	 */
	public Uri getCacheImageUri(String path, File cache) throws Exception {
		String name = path.substring(path.lastIndexOf("/")+1);
		File file = new File(cache, name);
		if (file.exists()) {
			System.out.println("MyDebug: �����ػ���ͼƬ��"+name);
			return Uri.fromFile(file);
		} else {
			System.out.println("MyDebug: ���������ϻ�ȡͼƬ��");
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				is.close();
				fos.close();
				return Uri.fromFile(file);
			}
		}
		return null;
	}
}