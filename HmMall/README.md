# 项目架构模式
```
MVP 架构
```

# 登录的账号

```
内置了一个用户
heima
11111
```
```
注册了一个账号
小明
```

# 引入外部 module

`PhotoPicker`

# 项目需求分析

```

```

# 项目实施

## 1 把常用的全局变量封装到自定义的 Application 类中
```
Android 系统自带Application类，这个类在android.jar中，我们无法修改，但是我们可以写一个类去继承Application，然后我们就可以在我们的类中写自己的成员变量和方法了。最后让系统创建我们的类，而不是创建系统的 Application。
 Application 类是专门用于保存全局变量的类，程序一运行这个类就会被系统创建，它被整个应用程序所共享，所以我们可以在Application类中写一些自己的成员变量或方法。
```
自定义 Application 类，如下：
```java
/** Application是专门用于保存全局变量的类 */
public class MyApp extends Application {
	/** Application类型的上下文 */
	private static Context context;
	// onCreate方法在主线程中运行
	@Override
	public void onCreate() {
		context = this;	
	}
	/** 获取Application类型的上下文 */
	public static Context getContext() {
		return context;
	}
	
}
```
定义完成后一定要在清单文件中配置：
```xml
<application
        android:name=".MyApp"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
```
*关于Context 的使用*
```
不同Context的使用，不用去管它们的区别，用的时候如果没报错就行了。报错了就换另一个Context.
Application Context 和Activity Context的最大的区别，就是他们的生命周期不同，Application Context的生命周期就是一整个应用程序的生命周期。在使用上文的时候尽量用Activity的Context，在需要长期保存上下文的时候则使保存Applicatin Context。
```

## 2 把常见的 UI 操作封装到 UiUtils
*常见的 UI 操作*
1. 在屏幕中央显示一个Toast
2. 把dp单位的值转换为px单位的值
3. 待补充。。。

```java
/** UI 工具类，封装了常用的UI操作 */
public class UiUtils {
		
	/** 在屏幕中央显示一个Toast */
	public static void showToast(CharSequence text) {
		Toast toast = Toast.makeText(MyApp.getContext(), text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}	

	/** 把dp单位的值转换为px单位的值 */
	public static int dp2px(int dp) {
		// 获取手机的屏幕密度，不同手机的屏幕密度可能不一样
		/*
		1） 先获取上下文，再获取资源对象；
		2)  通过资源对象来获取显示矩阵；
		3） 显示矩阵中有 density（密度） 属性， 就可以获得屏幕密度了；
		*/
		float density = MyApp.getContext().getResources().getDisplayMetrics().density;
		//4） 然后 dp * density，再 +0.5f（为了四舍五入），然后结果强转成 int 就是 px 了；
		int px = (int) (density * dp + 0.5); // 加0.5是为了把结果四舍五入
		return px;
	}		
}
```

## 3 封装Logger类, 简化Logger的书写
```java
public class Logger {

	/** 用于控制所有Log的输出 ,true输出，false不输出*/
	public static boolean showLog = true;
	
	/** 输出info级别的log信息，log中的tag和msg可以传任意对象 */
	public static void i(Object objTag, String objMsg) {
		if (showLog) {
			String tag = convertStringTag(objTag);
			String msg = convertStringMsg(objMsg);
			Log.i(tag, msg);
		}
	}

	/** 把Object类型的tag转换为String类型的tag */
	private static String convertStringTag(Object objTag) {
		String tag;
		if (objTag == null) {
			tag = "null";
		} else if (objTag instanceof String) {
			tag = (String) objTag;
		} else if (objTag instanceof Class) {
			tag = ((Class<?>) objTag).getSimpleName();	// 如果objTag不是String，则取它的类名
		} else {
			tag = objTag.getClass().getSimpleName();	// 取类名
		}
		return tag;

	}
	
	/** 把Object类型的消息转换为String类型的消息 */
	private static String convertStringMsg(String objMsg) {
		String msg;
		if (objMsg == null) {
			msg = "null";
		} else {
			msg = objMsg.toString();	// 把对象的toString用于Log信息的显示
		}
		return msg;
	}
}
```








