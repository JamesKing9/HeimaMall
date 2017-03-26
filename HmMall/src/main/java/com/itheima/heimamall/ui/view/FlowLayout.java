package com.itheima.heimamall.ui.view;

import java.util.ArrayList;
import java.util.Iterator;

import android.R.integer;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.heimamall.util.Logger;

/**
 * 流式布局
 * @author lxj
 *
 */
public class FlowLayout extends ViewGroup{
	
	private int horizontalSpacing = 15;//水平间距
	private int verticalSpacing = 15;//垂直间距
	private boolean isShareRemainSpacing = false;

	
	//用来存放所有的line对象
	public ArrayList<Line> lineList = new ArrayList<Line>();
	
	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		float density = getResources().getDisplayMetrics().density;
		horizontalSpacing = (int) (horizontalSpacing*density);
		verticalSpacing = (int) (verticalSpacing*density);
	}

	public void setHorizontalSpacing(int horizontalSpacing){
		this.horizontalSpacing = horizontalSpacing;
	}
	public void setVerticalSpacing(int verticalSpacing){
		this.verticalSpacing = verticalSpacing;
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public FlowLayout(Context context) {
		this(context,null);
	}

	/**
	 * 分行的计算。要计算哪些子View属于一行，
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		//在某些情况下onMeasure会执行2遍，所以需要先clean一下
		lineList.clear();
		
		//FlowLayout的父View是StateLayout，StateLayout肯定实现onMeasure，并且在onMeasure
		//中会计算好FlowLayout的宽高，然后传递给FlowLayout
		//1.计算总宽度
		int width = MeasureSpec.getSize(widthMeasureSpec);


		//计算出真正的用于比较的宽度，就是去除了paddingLeft和paddingRight的宽度
		int noPaddingWidth = width - getPaddingLeft() - getPaddingRight();
		
		Line line = new Line();//准备Line对象
		//2.遍历所有的子View，进行分行的计算
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);//取出子View
			
			//为了保证能够获取到child的measured宽高
			child.measure(0, 0);

			//3.处理特殊情况，如果当前line中木有子View，那么则直接将child放入line中
			//不用再比较，因为我们要保证每行至少有一个子View
			if(line.viewList.size()==0){
				//直接添加child
				line.addLineView(child);
			}else if (child.getMeasuredWidth()+line.lineWidth+horizontalSpacing
					>noPaddingWidth) {
				//4.如果当前child的宽+line的宽+水平间距大于noPaddingWidth,则child应该放入下一个line中
				//先将之前的line保存起来，再new 新的line
				lineList.add(line);

				//创建新的Line，添加child
				line = new Line();
				line.addLineView(child);
			}else {
				//5.说明child应该放入当前line中
				line.addLineView(child);
			}
			
			//6.如果当前的child是最后的view对象，那么应该手动将line保存起来
			//否则for循环结束后会丢失
			if(i==(getChildCount()-1)){
				lineList.add(line);
			}
		}
		
		//for循环结束后，lineList中存放了所有的line对象，而每个line对象又记录了自己行的所有view对象
		//为了能够摆放所有行的view，需要计算他们的总高度，设置给flowLayout
		//a.先计算paddingTop和paddingBottom
		int height = getPaddingTop()+getPaddingBottom();
		//b.再加上所有行的高度
		for (Line l : lineList) {
			height += l.lineHeight;
		}
		//c.再加上所有行的垂直间距
		height += (lineList.size()-1)*verticalSpacing;
		
		//设置FlowLayout的宽高
		setMeasuredDimension(width, height);
	}

	public void setShareRemainSpacing(boolean isShare){
		isShareRemainSpacing = isShare;
	}

	/**
	 * 摆放所有line的VIew，相当于让每个view坐到自己的位置上去
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(getChildCount()==0)return;

		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		
		
		for (int i = 0; i < lineList.size(); i++) {
			Line line = lineList.get(i);//得到line对象
			
			//从第二行开始，每行都比上一行多一个行高和垂直间距
			if(i>0){
				paddingTop += (lineList.get(i-1).lineHeight+verticalSpacing);
			}
			float perSpacing = 0;
			if(isShareRemainSpacing){
				//1.计算每行的留白区域
				int remainSpacing =  getLineRemainSpacing(line);
				//2.计算每个view平均分到的留白
				perSpacing = remainSpacing/line.viewList.size();
			}

			//遍历line的viewList中的所有view
			for (int j = 0; j < line.viewList.size(); j++) {
				View view = line.viewList.get(j);
				if(isShareRemainSpacing){
					//3.将perSpacing增加到view的padding上
					int halfSpacing = (int) (perSpacing/2);
					view.setPadding(view.getPaddingLeft()+halfSpacing, view.getPaddingTop(),
							view.getPaddingRight()+halfSpacing, view.getPaddingBottom());
					view.measure(0, 0);//强制测量
				}

				if(j==0){
					view.layout(paddingLeft, paddingTop,paddingLeft+view.getMeasuredWidth(),
							paddingTop+view.getMeasuredHeight());
				}else {
					//说明不是当前行的第一个view,那么摆放需要参考前一个View
					View preView = line.viewList.get(j-1);
					//当前的left等于前一个view的right+水平间距
					int left = preView.getRight()+horizontalSpacing;
					view.layout(left, preView.getTop(), left+view.getMeasuredWidth(), preView.getBottom());
				}
			}
		}
	}
	/**
	 * 获取指定行的留白区域
	 * @param line
	 */
	private int getLineRemainSpacing(Line line) {
		return getMeasuredWidth()-getPaddingLeft()-getPaddingRight()-line.lineWidth;
	}

	/**
	 * 用来封装一行数据
	 * @author lxj
	 *
	 */
	class Line{
		public ArrayList<View> viewList ;//用来存放当前行的所有view对象
		public int lineWidth;//当前行的宽，指的是所有view的宽+水平间距
		public int lineHeight;//当前行的高，layout的时候会用到
		public Line(){
			viewList = new ArrayList<View>();
		}
		/**
		 * 将child放入viewList中
		 */
		public void addLineView(View view){
			if(!viewList.contains(view)){
				viewList.add(view);
				
				//更新lineWidth
				if(viewList.size()==1){
					//说明是第一个view，那么直接赋值
					lineWidth = view.getMeasuredWidth();
				}else {
					//说明不是第一个，那么除了加view的宽，还要加上水平的间距
					lineWidth += view.getMeasuredWidth()+horizontalSpacing;
				}
				
				//更新lineHeight
				if(lineHeight==0){
					lineHeight = view.getMeasuredHeight();
				}
			}
		}
	}


	/**
	 * 设置适配器
	 * @param adapter
     */
	public void setAdapter(FlowLayoutAdapter adapter){
		if(adapter!=null){
			for (int i = 0; i < adapter.getCount(); i++) {
				View view = adapter.getView(i);
				addView(view);
			}
		}
	}
	public interface FlowLayoutAdapter{
		int getCount();
		View getView(int position);
	}
}
