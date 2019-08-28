package com.yemiekai.vedio_voice.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yemiekai.vedio_voice.R;

import java.util.ArrayList;

/**
 * 这个Fragment的布局是一个RecyclerView, 一个垂直滑动的列表, 每一行显示4个医生
 * */
public class DoctorListFragment extends Fragment
{
	public static final String FIRST_CATEGORY_INDEX = "first_category_index";  // 一级菜单索引(名医馆:0 内科:1 外科:1 ...)
	public static final String SECOND_CATEGORY_INDEX = "second_category_index";  // 二级菜单索引(心血管内科:0 内分泌科:1 ...)

	private int first_category_index;  // 传进来的一级菜单索引号
	private int second_category_index;  // 传进来的二级菜单索引号
	private boolean isIndexValid = false;  // 是否传来了科室索引, 或者科室索引是否有效
	RecyclerView recyclerView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(FIRST_CATEGORY_INDEX) && getArguments().containsKey(SECOND_CATEGORY_INDEX))
		{
			// 得到科室索引, 根据这个找到医生并且显示
			isIndexValid = true;
			first_category_index = getArguments().getInt(FIRST_CATEGORY_INDEX);
			second_category_index = getArguments().getInt(SECOND_CATEGORY_INDEX);
			/*
			 *
			 *
			 * todo: 根据科室索引找到该科室所有医生的信息, 以便在adapter里面设置显示各个医生信息, 多少行
			 *       (暂定每行4个医生, 见layout_doctors.xml)
			 *       以及每个医生的信息
			 *
			 *
			 */
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// 加载/res/layout/目录下的fragment_book_detail.xml布局文件
		View rootView = inflater.inflate(R.layout.fragment_doctors_list, container, false);
		recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_doctors_recycler);

		// 设置布局管理器
		LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());  // 线性
		layoutManager.setOrientation(RecyclerView.VERTICAL);  // 纵向
		recyclerView.setLayoutManager(layoutManager);

		// 设置适配器
		recyclerView.setAdapter(new MyDoctorAdapter());

		return rootView;
	}

	public class MyDoctorAdapter extends RecyclerView.Adapter<MyDoctorAdapter.ViewHolder> {
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
				mTextView1 = itemView.findViewById(R.id.layout_doctors_text1);
				mTextView2 = itemView.findViewById(R.id.layout_doctors_text2);
				mTextView3 = itemView.findViewById(R.id.layout_doctors_text3);
				mTextView4 = itemView.findViewById(R.id.layout_doctors_text4);
			}
		}

		public MyDoctorAdapter(){
			// 构造函数, 暂时没有传参
		}

		@Override
		public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doctors,parent,false);
			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

			// todo:
			//      根据position找到对应医生索引的方法：(position就是当前行数)
			//      doctor index1 = position * 4 + 0
			//      doctor index2 = position * 4 + 1
			//      doctor index3 = position * 4 + 2
			//      doctor index4 = position * 4 + 3
			holder.mButton1.setImageResource(R.drawable.sample_doctor);  // 设置按钮图片
			holder.mButton2.setImageResource(R.drawable.sample_doctor);  // 设置按钮图片
			holder.mButton3.setImageResource(R.drawable.sample_doctor);  // 设置按钮图片
			holder.mButton4.setImageResource(R.drawable.sample_doctor);  // 设置按钮图片

			holder.mTextView1.setText("什么医生\n什么职位\n什么职称");  // 设置文字
			holder.mTextView2.setText("什么医生\n什么职位\n什么职称");  // 设置文字
			holder.mTextView3.setText("什么医生\n什么职位\n什么职称");  // 设置文字
			holder.mTextView4.setText("什么医生\n什么职位\n什么职称");  // 设置文字

			// todo:
			//      设置每个按钮的点击响应时间, 切换fragment, 显示医生详细信息
			//      切换时要传入当前fragment的科室索引, 在客户按下"返回"按钮时要切换回现在的fragment
			//      (因为切换了fragment后, 现在的fragment好像被destroy了)
			holder.mButton1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Toast.makeText(getActivity(), "点击了" + (position*4 + 0), Toast.LENGTH_SHORT).show();
				}
			});
			holder.mButton2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Toast.makeText(getActivity(), "点击了"  + (position*4 + 1), Toast.LENGTH_SHORT).show();
				}
			});
			holder.mButton3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Toast.makeText(getActivity(), "点击了" +  (position*4 + 2), Toast.LENGTH_SHORT).show();
				}
			});
			holder.mButton4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Toast.makeText(getActivity(), "点击了" + (position*4 + 3), Toast.LENGTH_SHORT).show();
				}
			});
		}

		@Override
		public int getItemCount() {
			/*
			 *
			 *
			 *
			 * todo 这是设置列表的长度。  列表一行有4个医生
			 *		到时候根据多少医生, 返回值为 (医生总数/4)
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

}
