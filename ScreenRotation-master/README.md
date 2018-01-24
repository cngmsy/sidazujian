# Android视频播放或直播时重力感应横竖屏切换
> 开发时遇到播放视频要用重力感应来横竖屏，并且要有按钮点击横竖屏，查了很多资料，参考了一些代码，终于把重力感应横竖屏实现。


### 万恶的效果图，好的开发者都会自己去运行Demo 	:joy:

<table cellpadding="0" cellspacing="0" border='0'>

	<tr>

		<td> <img src='http://oibrygxgr.bkt.clouddn.com/landscape%20.jpg' width='100%' height='50%'>  </td>
		<td> <img src='http://oibrygxgr.bkt.clouddn.com/portrait.jpg'
		width='100%' height='50%'/></td>

	</tr>

	</table>

### 一、实现原理
> 既然是重力感应横竖屏，那就肯定要有重力感应，重力感应属于传感器的一类，所以需要用到重力感应传感器，不清楚的同学可以google一下。
  由于视频播放或者直播页面一直都需要重力感应，所以还需要绑定播放页面的生命周期来注册重力感应和注销重力感应。这样当注册重力感应后，
  就可以获取当前屏幕的朝向角度，根据这些角度来判断哪个范围需要横竖屏。

### 二、实现逻辑

#### 2.1 打造工具类ScreenRotateUtil
* 由于要绑定生命周期，并且可以点击横竖屏，所以可以做一个工具类，提供start()和stop方法来绑定生命周期，
并且暴露一个点击切换横竖屏的方法toggleRotate()就可以。

* start方法注册监听

	    public void start(Activity activity) {
			// 接收activity，用于操作屏幕的旋转
		    mActivity = activity;
			// 注册传感器监听
		    sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
	    }


* stop()方法注销监听

		public void stop() {
			// 注销监听
	        sm.unregisterListener(listener);
			// 防止内存泄漏
	        mActivity = null;
	    }

* toggleRotate()方法，自动切换横竖屏

		public void toggleRotate() {

	        /**
	         * 先判断是否已经开启了重力感应，没开启就直接普通的切换横竖屏
	         */
	        if(isEffetSysSetting){
	            try {
	                int isRotate = Settings.System.getInt(mActivity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);

	                // 如果用户禁用掉了重力感应就直接切换
	                if (isRotate == 0) {
	                    changeOrientation(isLandscape, true);
	                    return;
	                }
	            } catch (Settings.SettingNotFoundException e) {
	                e.printStackTrace();
	            }
	        }

	        /**
	         * 如果开启了重力感应就需要修改状态
	         */
	        isOpenSensor = false;
	        isClickFullScreen = true;
	        if (isChangeOrientation) {
	            changeOrientation(isLandscape, false);
	        } else {
	            isLandscape = !isLandscape;
	            changeOrientation(isLandscape, false);
	        }
	    }


#### 2.2 重力感应传感器
> Android提供了多种传感器，这里只是简单介绍一下重力感应传感器，想了解更多请[传送](http://blog.csdn.net/mad1989/article/details/20848181)。

* 初始化传感器

		// 这里在构造里初始化重力重力感应
		private ScreenRotateUtil(Context context) {
	        // 获取传感器管理器
	        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
			// 获取传感器类型
	        sensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
			// 初始化监听器
	        listener = new OrientationSensorListener(mHandler);
	    }

* start()方法注册监听，stop()方法注销监听

* 看一下监听里非常重要的方法onSensorChanged，当注册了监听后，这个方法会不停的回调用，不停的给Handler发送消息，然后就可以在Handler里修改屏幕的朝向

        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            int orientation = ORIENTATION_UNKNOWN;
            float X = -values[_DATA_X];
            float Y = -values[_DATA_Y];
            float Z = -values[_DATA_Z];
            float magnitude = X * X + Y * Y;
            // Don't trust the angle if the magnitude is small compared to the y
            // value
            if (magnitude * 4 >= Z * Z) {
                // 屏幕旋转时
                float OneEightyOverPi = 57.29577957855f;
                float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
                orientation = 90 - Math.round(angle);
                // normalize to 0 - 359 range
                while (orientation >= 360) {
                    orientation -= 360;
                }
                while (orientation < 0) {
                    orientation += 360;
                }
            }

            /**
             * 获取手机系统的重力感应开关设置，这段代码看需求，不要就删除
             * screenchange = 1 表示开启，screenchange = 0 表示禁用
             * 要是禁用了就直接返回
             */
            if(isEffetSysSetting){
                try {
                    int isRotate = Settings.System.getInt(mActivity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);

                    // 如果用户禁用掉了重力感应就直接return
                    if (isRotate == 0) return;
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
            }

            // 只有点了按钮时才需要根据当前的状态来更新状态
            if (isClickFullScreen) {
                if (isLandscape && screenIsPortrait(orientation)) {           // 之前是横屏，并且当前是竖屏的状态
                    Log.d(TAG, "onSensorChanged: 横屏 ----> 竖屏");
                    updateState(false, false, true, true);
                } else if (!isLandscape && screenIsLandscape(orientation)) {  // 之前是竖屏，并且当前是横屏的状态
                    Log.d(TAG, "onSensorChanged: 竖屏 ----> 横屏");
                    updateState(true, false, true, true);
                } else if (isLandscape && screenIsLandscape(orientation)) {    // 之前是横屏，现在还是横屏的状态
                    Log.d(TAG, "onSensorChanged: 横屏 ----> 横屏");
                    isChangeOrientation = false;
                } else if (!isLandscape && screenIsPortrait(orientation)) {  // 之前是竖屏，现在还是竖屏的状态
                    Log.d(TAG, "onSensorChanged: 竖屏 ----> 竖屏");
                    isChangeOrientation = false;
                }
            }

            // 判断是否要进行中断信息传递
            if (!isOpenSensor) {
                return;
            }

            if (rotateHandler != null) {
                rotateHandler.obtainMessage(888, orientation, 0).sendToTarget();
            }
        }


### 2.3 横竖屏处理

* 这里的横竖屏是去根据当前手机的朝向即orientation的角度进行判断，然后调用mActivity.setRequestedOrientation()方法设置横竖屏，这里进行了360°的判断，实现了四个方向的横竖屏。

		 if (msg.what == 888) {
            int orientation = msg.arg1;

            /**
             * 根据手机屏幕的朝向角度，来设置内容的横竖屏，并且记录状态
             */
            if (orientation > 45 && orientation < 135) {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                isLandscape = true;
            } else if (orientation > 135 && orientation < 225) {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                isLandscape = false;
            } else if (orientation > 225 && orientation < 315) {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                isLandscape = true;
            } else if ((orientation > 315 && orientation < 360) || (orientation > 0 && orientation < 45)) {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                isLandscape = false;
            }
        }



### 三、使用教程
1. Activity的onResume()方法调用start()方法进行注册监听
2. Activity的onPause()方法调用stop()方法注销监听
3. 点击全屏按钮时调用toggleRotate()自动切换横竖屏
4. 如果需要手机系统的横竖屏按钮生效则调用setEffetSysSetting(true)


### 四、总结
> 关于重力感应横竖屏其实并不是很难，根据实际的需求去做，一步一步考虑逻辑。
这个工具类也基本上是没用什么bug，也可以直接拿去用。如果对你有用可以到我的[Github](https://github.com/PingerOne/ScreenRotation)给个star哦，有什么问题欢迎issues哦。



[我的主页](http://www.jianshu.com/u/64f479a1cef7)
[Demo下载](https://github.com/PingerOne/ScreenRotation)