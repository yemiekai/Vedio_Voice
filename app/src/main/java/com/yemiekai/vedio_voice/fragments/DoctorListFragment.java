package com.yemiekai.vedio_voice.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yemiekai.vedio_voice.R;

import java.util.ArrayList;

public class DoctorListFragment extends Fragment
{
	public static final String FIRST_CATEGORY_INDEX = "first_category_index";  // 一级菜单索引(名医馆:0 内科:1 外科:1 ...)
	public static final String SECOND_CATEGORY_INDEX = "second_category_index";  // 二级菜单索引(心血管内科:0 内分泌科:1 ...)

	RecyclerView recyclerView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// 如果启动该Fragment时包含了ITEM_ID参数
		if (getArguments().containsKey(FIRST_CATEGORY_INDEX) && getArguments().containsKey(SECOND_CATEGORY_INDEX))
		{
			int first = getArguments().getInt(FIRST_CATEGORY_INDEX);
			int second = getArguments().getInt(SECOND_CATEGORY_INDEX);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// 加载/res/layout/目录下的fragment_book_detail.xml布局文件
		View rootView = inflater.inflate(R.layout.fragment_doctors_list, container, false);
		recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_doctors_recycler);

		init_doctor_view();

		return rootView;
	}

	private void init_doctor_view(){
		LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());  // 线性
		layoutManager.setOrientation(RecyclerView.VERTICAL);  // 纵向
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(new MyDoctorAdapter());
	}

	public class MyDoctorAdapter extends RecyclerView.Adapter<MyDoctorAdapter.ViewHolder> {

		ArrayList<String> buttonList;

		class ViewHolder extends RecyclerView.ViewHolder {
			ImageButton mButton1;
			ImageButton mButton2;
			ImageButton mButton3;
			ImageButton mButton4;
			TextView mTextView1;
			TextView mTextView2;
			TextView mTextView3;
			TextView mTextView4;

			public ViewHolder(View itemView) {
				super(itemView);
				mButton1 = itemView.findViewById(R.id.layout_doctors_button1);
				mButton2 = itemView.findViewById(R.id.layout_doctors_button2);
				mButton3 = itemView.findViewById(R.id.layout_doctors_button3);
				mButton4 = itemView.findViewById(R.id.layout_doctors_button4);
				mButton1 = itemView.findViewById(R.id.layout_doctors_button1);
				mTextView1 = itemView.findViewById(R.id.layout_doctors_text1);
				mTextView2 = itemView.findViewById(R.id.layout_doctors_text2);
				mTextView3 = itemView.findViewById(R.id.layout_doctors_text3);
				mTextView4 = itemView.findViewById(R.id.layout_doctors_text4);
			}
		}

		public MyDoctorAdapter(){
		}

		@Override
		public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doctors,parent,false);
			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

			holder.mButton1.setImageResource(R.drawable.sample_doctor);  // 设置按钮图片
			holder.mButton2.setImageResource(R.drawable.sample_doctor);  // 设置按钮图片
			holder.mButton3.setImageResource(R.drawable.sample_doctor);  // 设置按钮图片
			holder.mButton4.setImageResource(R.drawable.sample_doctor);  // 设置按钮图片
//			holder.mButton.setOnClickListener(buttonInfo.getClickListener());  // 设置按钮监听
//
//			if(position==0){
//				holder.mButton.requestFocus();
//			}
		}

		@Override
		public int getItemCount() {
			/**
			 *
			 *
			 *
			 *
			 *
			 *
			 *
			 *
			 * todo 这是设置列表的长度。  列表一行有4个医生
			 *
			 *
			 *
			 *
			 *
			 * */
			return 3;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

	}
//	public static final String ITEM_ID = "item_id";
//	// 保存该Fragment显示的Book对象
//	BookContent.Book book;
//	@Override
//	public void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		// 如果启动该Fragment时包含了ITEM_ID参数
//		if (getArguments().containsKey(ITEM_ID))
//		{
//			book = BookContent.ITEM_MAP.get(getArguments()
//				.getInt(ITEM_ID)); // ①
//		}
//	}
//	// 重写该方法，该方法返回的View将作为Fragment显示的组件
//	@Override
//	public View onCreateView(LayoutInflater inflater
//		, ViewGroup container, Bundle savedInstanceState)
//	{
//		// 加载/res/layout/目录下的fragment_book_detail.xml布局文件
//		View rootView = inflater.inflate(R.layout.fragment_book_detail,
//				container, false);
//		if (book != null)
//		{
//			// 让book_title文本框显示book对象的title属性
//			((TextView) rootView.findViewById(R.id.book_title))
//				.setText(book.title);
//			// 让book_desc文本框显示book对象的desc属性
//			((TextView) rootView.findViewById(R.id.book_desc))
//				.setText(book.desc);
//		}
//		return rootView;
//	}
}
