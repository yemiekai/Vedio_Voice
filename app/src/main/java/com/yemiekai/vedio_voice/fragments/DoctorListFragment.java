package com.yemiekai.vedio_voice.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.yemiekai.vedio_voice.R;

public class DoctorListFragment extends Fragment
{

	public static final String FIRST_CATEGORY_INDEX = "first_category_index";  // 一级菜单索引(名医馆:0 内科:1 外科:1 ...)
	public static final String SECOND_CATEGORY_INDEX = "second_category_index";  // 二级菜单索引(心血管内科:0 内分泌科:1 ...)

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
		RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_doctors_recycler);


		return rootView;
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
