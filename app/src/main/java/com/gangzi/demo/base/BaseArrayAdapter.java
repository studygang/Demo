package com.gangzi.demo.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */

public abstract class BaseArrayAdapter<D,Holder>extends BaseAdapter{

    private List<D> mInfos = new ArrayList<>();
    private final Object mLock = new Object();
    protected LayoutInflater mInflater;
    protected Context context;
    private boolean mNotifyOnChange = true;
    //传递context引用进来
    public BaseArrayAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }
    //mNotifyOnChange是一个控制的开关
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }
    public Context getContext() {
        return context;
    }
    public int getCount() {
        return isEmpty() ? 0 : mInfos.size();
    }
    public D getItem(int position) {
        return isEmpty() ? null : mInfos.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    //覆写getView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        D d = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(getLayoutId(), parent, false);
            holder = onCreateViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        onBindViewHolder(holder, d, position, convertView);
        return convertView;
    }
    public abstract @LayoutRes int getLayoutId();

    /**
     * 创建Holder
     */
    public abstract Holder onCreateViewHolder(View convertView);

    /**
     * 绑定数据
     */
    public abstract void onBindViewHolder(Holder holder, D d, int position, View convertView);

    /**
      * 局部刷新的方法，如果不指明header个数，这里只考虑listView,其余按0处理
      *  @param parent AdapterView
      * @param position 需要刷新的position,这里的position不包含header
      */
    public void updateItem(AdapterView parent, int position) {
        int headerCount = 0;
        if (parent instanceof ListView) {
            headerCount = ((ListView) parent).getHeaderViewsCount();
        }
        updateItem(parent, position, headerCount);
    }
    /**
     * 局部刷新的方法
     *  @param parent AdapterView
     *  @param position 需要刷新的position,这里的position不包含header
     *  @param headerCount header个数
     *
     */
    public void updateItem(AdapterView parent, int position, int headerCount) {
        int realPosition = position + headerCount;
        int firstVisiblePosition = parent.getFirstVisiblePosition();
        int lastVisiblePosition = parent.getLastVisiblePosition();
        // 判断position是否可见，如果可见调用updateUI方法
        if (realPosition >= firstVisiblePosition && realPosition <= lastVisiblePosition) {
            View view = parent.getChildAt(realPosition - firstVisiblePosition);
            onBindViewHolder((Holder) view.getTag(), getItem(position), position, null);
        }
    }
    /**
     * 这里忽略header
     * @param parent
     * @param position
     * @return
     */
    public Holder getItemHolder(AdapterView parent, int position){
        return (Holder) parent.getChildAt(position).getTag();
    }
    public void setDatas(@Nullable List<D> infos) {
        mInfos = infos; notifyDataSetChanged();
    }
    public List<D> getDatas() {
        return mInfos;
    }
    public boolean isEmpty() {
        return mInfos == null;
    }
    public void add(D t) {
        synchronized (mLock) {
            if (isEmpty()) {
                return;
            }
            mInfos.add(t);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }
    public void addAll(List<D> list) {
        synchronized (mLock) {
            if (isEmpty()) {
                return;
            }
            if (list != null) {
                mInfos.addAll(list);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }
    public void clear() {
        if (isEmpty()) {
            return;
        }
        synchronized (mLock) {
            mInfos.clear();
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }
    public void remove(D t){
        synchronized (mLock){
            if(isEmpty()){
                return;
            }
            if(mInfos != null){
                mInfos.remove(t);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }
    public void insert(D t, int index){
        synchronized (mLock){
            if(isEmpty()){
                return;
            }
            if(mInfos != null){
                mInfos.add(index,t);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }
    public void sort(Comparator<? super D> comparator){
        synchronized (mLock){
            if(isEmpty()){
                return;
            }
            if(mInfos != null){
                Collections.sort(mInfos, comparator);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

}
