package com.sanhai.live.whiteboard;



import com.sanhai.live.whiteboard.shape.BaseDraw;

import java.util.List;

/**
 * @ClassName inttttt
 * @Description TODO
 * @Author yangjie
 * @Date 2019/3/28 下午4:33
 */


public interface CallBack {
    public void onDrawBack(List<BaseDraw> fabricViewDataList, List<BaseDraw> undoDrawableList, List<BaseDraw> redoDrawableList, BaseDraw baseDraw);
}
