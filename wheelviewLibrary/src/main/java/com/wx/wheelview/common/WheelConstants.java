/*
 * Copyright (C) 2016 venshine.cn@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wx.wheelview.common;

import android.graphics.Color;

/**
 * 滚轮常量类
 *
 * @author venshine
 */
public class WheelConstants {

    /**
     * 滚轮tag
     */
    public static final String TAG = "com.wx.wheelview";

    
    /**
     * 平滑滚动持续时间
     */
    public static final int WHEEL_SMOOTH_SCROLL_DURATION = 50;



    /**
     * 滚轮默认文本颜色
     */
    public static final int WHEEL_TEXT_COLOR = Color.BLACK;

    /**
     * 滚轮默认文本大小
     */
    public static final int WHEEL_TEXT_SIZE = 16;
    

    /**
     * 滚轮滑动时展示选中项
     */
    public static final int WHEEL_SCROLL_HANDLER_WHAT = 0x0100;

    /**
     * 滚轮滑动时展示选中项延迟时间
     */
    public static final int WHEEL_SCROLL_DELAY_DURATION = 300;


}
