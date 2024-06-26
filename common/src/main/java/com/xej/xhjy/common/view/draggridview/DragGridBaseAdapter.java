package com.xej.xhjy.common.view.draggridview;

public interface DragGridBaseAdapter {
	/**
	 * 重新排列数据
	 * @param oldPosition
	 * @param newPosition
	 */
	public void reorderItems(int oldPosition, int newPosition);
	
	
	/**
	 * 设置某个item隐藏
	 * @param hidePosition
	 */
	public void setHideItem(int hidePosition);
	
	/**
	 * 删除某个item
	 * @param removePosition
	 */
	public void removeItem(int removePosition);

	/**
	 * 获取当前item总数
	 * @return
	 */
	public int getItemsCount();
}
