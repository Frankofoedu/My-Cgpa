package b4a.My_CGPA;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.My_CGPA", "b4a.My_CGPA.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "b4a.My_CGPA", "b4a.My_CGPA.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.My_CGPA.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        Object[] o;
        if (permissions.length > 0)
            o = new Object[] {permissions[0], grantResults[0] == 0};
        else
            o = new Object[] {"", false};
        processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.Timer _timer1 = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scrollview1 = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinner1 = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinner2 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlhome = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlsplash = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnllogo = null;
public anywheresoftware.b4a.objects.collections.List _culist = null;
public anywheresoftware.b4a.objects.collections.List _grdlist = null;
public static int _paneltop = 0;
public static int _panelheight = 0;
public anywheresoftware.b4a.objects.LabelWrapper[] _arr = null;
public anywheresoftware.b4a.objects.collections.List _j = null;
public static int _count = 0;
public anywheresoftware.b4a.objects.LabelWrapper _lblcgpa = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinner3 = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spinner4 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlupdate = null;
public b4a.My_CGPA.starter _starter = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 45;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 48;BA.debugLine="Activity.LoadLayout(\"cgpa\")";
mostCurrent._activity.LoadLayout("cgpa",mostCurrent.activityBA);
 //BA.debugLineNum = 49;BA.debugLine="centercen(PnlLogo)";
_centercen((anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(mostCurrent._pnllogo.getObject())));
 //BA.debugLineNum = 50;BA.debugLine="PnlHome.SetLayout(PnlHome.Width,0,PnlHome.Width,P";
mostCurrent._pnlhome.SetLayout(mostCurrent._pnlhome.getWidth(),(int) (0),mostCurrent._pnlhome.getWidth(),mostCurrent._pnlhome.getHeight());
 //BA.debugLineNum = 51;BA.debugLine="timer1.Initialize(\"Timer1\",3000)";
_timer1.Initialize(processBA,"Timer1",(long) (3000));
 //BA.debugLineNum = 52;BA.debugLine="timer1.Enabled = True";
_timer1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 53;BA.debugLine="Spinner1.AddAll(Array As String(\"Credit Unit\",\"6\"";
mostCurrent._spinner1.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Credit Unit","6","5","4","3","2","1"}));
 //BA.debugLineNum = 54;BA.debugLine="Spinner2.AddAll(Array As String(\"Grade\",\"A\",\"B\",\"";
mostCurrent._spinner2.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Grade","A","B","C","D","E","F"}));
 //BA.debugLineNum = 57;BA.debugLine="Spinner3.AddAll(Array As String(\"Credit Unit\",\"6\"";
mostCurrent._spinner3.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Credit Unit","6","5","4","3","2","1"}));
 //BA.debugLineNum = 58;BA.debugLine="Spinner4.AddAll(Array As String(\"Grade\",\"A\",\"B\",\"";
mostCurrent._spinner4.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Grade","A","B","C","D","E","F"}));
 //BA.debugLineNum = 59;BA.debugLine="cuList.Initialize";
mostCurrent._culist.Initialize();
 //BA.debugLineNum = 60;BA.debugLine="grdList.Initialize";
mostCurrent._grdlist.Initialize();
 //BA.debugLineNum = 62;BA.debugLine="PanelHeight  = 45dip";
_panelheight = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (45));
 //BA.debugLineNum = 66;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 72;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 74;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 68;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
return "";
}
public static String  _calculatecgpa(anywheresoftware.b4a.objects.collections.List _k,anywheresoftware.b4a.objects.collections.List _f) throws Exception{
int _tcur = 0;
int _tmp = 0;
int _tcue = 0;
double _cgpa = 0;
int _i = 0;
 //BA.debugLineNum = 145;BA.debugLine="Sub CalculateCGPA(k As List, f As List)";
 //BA.debugLineNum = 146;BA.debugLine="Dim tcur As Int";
_tcur = 0;
 //BA.debugLineNum = 147;BA.debugLine="Dim tmp As Int";
_tmp = 0;
 //BA.debugLineNum = 148;BA.debugLine="Dim tcue As Int";
_tcue = 0;
 //BA.debugLineNum = 149;BA.debugLine="Dim cgpa As Double";
_cgpa = 0;
 //BA.debugLineNum = 150;BA.debugLine="For i = 0 To k.Size-1";
{
final int step5 = 1;
final int limit5 = (int) (_k.getSize()-1);
for (_i = (int) (0) ; (step5 > 0 && _i <= limit5) || (step5 < 0 && _i >= limit5); _i = ((int)(0 + _i + step5)) ) {
 //BA.debugLineNum = 151;BA.debugLine="tcur = tcur + k.Get(i)";
_tcur = (int) (_tcur+(double)(BA.ObjectToNumber(_k.Get(_i))));
 //BA.debugLineNum = 152;BA.debugLine="Select f.Get(i)";
switch (BA.switchObjectToInt(_f.Get(_i),(Object)("A"),(Object)("B"),(Object)("C"),(Object)("D"),(Object)("E"),(Object)("F"))) {
case 0: {
 //BA.debugLineNum = 154;BA.debugLine="tmp = 5";
_tmp = (int) (5);
 break; }
case 1: {
 //BA.debugLineNum = 156;BA.debugLine="tmp =4";
_tmp = (int) (4);
 break; }
case 2: {
 //BA.debugLineNum = 158;BA.debugLine="tmp = 3";
_tmp = (int) (3);
 break; }
case 3: {
 //BA.debugLineNum = 160;BA.debugLine="tmp = 2";
_tmp = (int) (2);
 break; }
case 4: {
 //BA.debugLineNum = 162;BA.debugLine="tmp =1";
_tmp = (int) (1);
 break; }
case 5: {
 //BA.debugLineNum = 164;BA.debugLine="tmp = 0";
_tmp = (int) (0);
 break; }
}
;
 //BA.debugLineNum = 166;BA.debugLine="tcue = tcue + k.Get(i) * tmp";
_tcue = (int) (_tcue+(double)(BA.ObjectToNumber(_k.Get(_i)))*_tmp);
 }
};
 //BA.debugLineNum = 169;BA.debugLine="cgpa = Round2((tcue/tcur),2)";
_cgpa = anywheresoftware.b4a.keywords.Common.Round2((_tcue/(double)_tcur),(int) (2));
 //BA.debugLineNum = 170;BA.debugLine="LblCGPA.Text = \"CGPA = \" & cgpa";
mostCurrent._lblcgpa.setText((Object)("CGPA = "+BA.NumberToString(_cgpa)));
 //BA.debugLineNum = 171;BA.debugLine="LblCGPA.TextSize = 25";
mostCurrent._lblcgpa.setTextSize((float) (25));
 //BA.debugLineNum = 172;BA.debugLine="LblCGPA.TextColor = Colors.Black";
mostCurrent._lblcgpa.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 173;BA.debugLine="End Sub";
return "";
}
public static String  _centercen(anywheresoftware.b4a.objects.ConcreteViewWrapper _ctrl) throws Exception{
 //BA.debugLineNum = 76;BA.debugLine="Sub centercen (ctrl As View)";
 //BA.debugLineNum = 78;BA.debugLine="ctrl.Left = (Activity.Width - ctrl.Width) / 2";
_ctrl.setLeft((int) ((mostCurrent._activity.getWidth()-_ctrl.getWidth())/(double)2));
 //BA.debugLineNum = 79;BA.debugLine="ctrl.top = (Activity.Height - ctrl.Height) / 2";
_ctrl.setTop((int) ((mostCurrent._activity.getHeight()-_ctrl.getHeight())/(double)2));
 //BA.debugLineNum = 80;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 25;BA.debugLine="Private ScrollView1 As ScrollView";
mostCurrent._scrollview1 = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private Spinner1 As Spinner";
mostCurrent._spinner1 = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private Spinner2 As Spinner";
mostCurrent._spinner2 = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private PnlHome As Panel";
mostCurrent._pnlhome = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private PnlSplash As Panel";
mostCurrent._pnlsplash = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private PnlLogo As Panel";
mostCurrent._pnllogo = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Dim cuList As List";
mostCurrent._culist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 32;BA.debugLine="Dim grdList As List";
mostCurrent._grdlist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 33;BA.debugLine="Dim PanelTop, PanelHeight As Int";
_paneltop = 0;
_panelheight = 0;
 //BA.debugLineNum = 34;BA.debugLine="Dim arr(3) As Label";
mostCurrent._arr = new anywheresoftware.b4a.objects.LabelWrapper[(int) (3)];
{
int d0 = mostCurrent._arr.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._arr[i0] = new anywheresoftware.b4a.objects.LabelWrapper();
}
}
;
 //BA.debugLineNum = 35;BA.debugLine="Dim j As List";
mostCurrent._j = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 37;BA.debugLine="Dim count As Int =0";
_count = (int) (0);
 //BA.debugLineNum = 39;BA.debugLine="Private LblCGPA As Label";
mostCurrent._lblcgpa = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private Spinner3 As Spinner";
mostCurrent._spinner3 = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private Spinner4 As Spinner";
mostCurrent._spinner4 = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private PnlUpdate As Panel";
mostCurrent._pnlupdate = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 43;BA.debugLine="End Sub";
return "";
}
public static String  _imgaddcourse_click() throws Exception{
 //BA.debugLineNum = 245;BA.debugLine="Sub imgAddCourse_Click";
 //BA.debugLineNum = 246;BA.debugLine="If Spinner1.SelectedIndex = 0 Or Spinner2.Selecte";
if (mostCurrent._spinner1.getSelectedIndex()==0 || mostCurrent._spinner2.getSelectedIndex()==0) { 
 //BA.debugLineNum = 247;BA.debugLine="ToastMessageShow(\"please fill your grade selecti";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("please fill your grade selections",anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 249;BA.debugLine="cuList.Add(Spinner1.SelectedItem)";
mostCurrent._culist.Add((Object)(mostCurrent._spinner1.getSelectedItem()));
 //BA.debugLineNum = 250;BA.debugLine="grdList.Add(Spinner2.SelectedItem)";
mostCurrent._grdlist.Add((Object)(mostCurrent._spinner2.getSelectedItem()));
 //BA.debugLineNum = 251;BA.debugLine="UpdateCgpa(count, Spinner1.SelectedItem, Spinner";
_updatecgpa(_count,mostCurrent._spinner1.getSelectedItem(),mostCurrent._spinner2.getSelectedItem());
 //BA.debugLineNum = 253;BA.debugLine="Spinner1.SelectedIndex = 0";
mostCurrent._spinner1.setSelectedIndex((int) (0));
 //BA.debugLineNum = 254;BA.debugLine="Spinner2.SelectedIndex = 0";
mostCurrent._spinner2.setSelectedIndex((int) (0));
 //BA.debugLineNum = 255;BA.debugLine="count = count + 1";
_count = (int) (_count+1);
 };
 //BA.debugLineNum = 257;BA.debugLine="End Sub";
return "";
}
public static String  _panel1_click() throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _p = null;
anywheresoftware.b4a.objects.LabelWrapper _v = null;
String _a = "";
String _b = "";
 //BA.debugLineNum = 175;BA.debugLine="Sub Panel1_Click";
 //BA.debugLineNum = 176;BA.debugLine="Dim p As Panel";
_p = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 177;BA.debugLine="p = Sender";
_p.setObject((android.view.ViewGroup)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 //BA.debugLineNum = 178;BA.debugLine="j.Initialize";
mostCurrent._j.Initialize();
 //BA.debugLineNum = 179;BA.debugLine="For Each v As Label In p.GetAllViewsRecursive";
_v = new anywheresoftware.b4a.objects.LabelWrapper();
final anywheresoftware.b4a.BA.IterableList group4 = _p.GetAllViewsRecursive();
final int groupLen4 = group4.getSize();
for (int index4 = 0;index4 < groupLen4 ;index4++){
_v.setObject((android.widget.TextView)(group4.Get(index4)));
 //BA.debugLineNum = 180;BA.debugLine="j.Add(v)";
mostCurrent._j.Add((Object)(_v.getObject()));
 }
;
 //BA.debugLineNum = 183;BA.debugLine="arr(0) = j.Get(0)";
mostCurrent._arr[(int) (0)].setObject((android.widget.TextView)(mostCurrent._j.Get((int) (0))));
 //BA.debugLineNum = 184;BA.debugLine="arr(1) = j.Get(1)";
mostCurrent._arr[(int) (1)].setObject((android.widget.TextView)(mostCurrent._j.Get((int) (1))));
 //BA.debugLineNum = 185;BA.debugLine="arr(2) = j.Get(2)";
mostCurrent._arr[(int) (2)].setObject((android.widget.TextView)(mostCurrent._j.Get((int) (2))));
 //BA.debugLineNum = 186;BA.debugLine="Log(arr(1).Text)";
anywheresoftware.b4a.keywords.Common.Log(mostCurrent._arr[(int) (1)].getText());
 //BA.debugLineNum = 187;BA.debugLine="PnlUpdate.BringToFront";
mostCurrent._pnlupdate.BringToFront();
 //BA.debugLineNum = 188;BA.debugLine="PnlUpdate.Visible = True";
mostCurrent._pnlupdate.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 189;BA.debugLine="PnlUpdate.Enabled = True";
mostCurrent._pnlupdate.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 191;BA.debugLine="Dim a As String = arr(1).Text";
_a = mostCurrent._arr[(int) (1)].getText();
 //BA.debugLineNum = 192;BA.debugLine="Select a";
switch (BA.switchObjectToInt(_a,"6","5","4","3","2","1")) {
case 0: {
 //BA.debugLineNum = 194;BA.debugLine="Spinner3.SelectedIndex = 1";
mostCurrent._spinner3.setSelectedIndex((int) (1));
 break; }
case 1: {
 //BA.debugLineNum = 196;BA.debugLine="Spinner3.SelectedIndex = 2";
mostCurrent._spinner3.setSelectedIndex((int) (2));
 break; }
case 2: {
 //BA.debugLineNum = 198;BA.debugLine="Spinner3.SelectedIndex = 3";
mostCurrent._spinner3.setSelectedIndex((int) (3));
 break; }
case 3: {
 //BA.debugLineNum = 200;BA.debugLine="Spinner3.SelectedIndex = 4";
mostCurrent._spinner3.setSelectedIndex((int) (4));
 break; }
case 4: {
 //BA.debugLineNum = 202;BA.debugLine="Spinner3.SelectedIndex = 5";
mostCurrent._spinner3.setSelectedIndex((int) (5));
 break; }
case 5: {
 //BA.debugLineNum = 204;BA.debugLine="Spinner3.SelectedIndex = 6";
mostCurrent._spinner3.setSelectedIndex((int) (6));
 break; }
}
;
 //BA.debugLineNum = 207;BA.debugLine="Dim b As String = arr(2).Text";
_b = mostCurrent._arr[(int) (2)].getText();
 //BA.debugLineNum = 209;BA.debugLine="Select b";
switch (BA.switchObjectToInt(_b,"A","B","C","D","E","F")) {
case 0: {
 //BA.debugLineNum = 211;BA.debugLine="Spinner4.SelectedIndex = 1";
mostCurrent._spinner4.setSelectedIndex((int) (1));
 break; }
case 1: {
 //BA.debugLineNum = 213;BA.debugLine="Spinner4.SelectedIndex = 2";
mostCurrent._spinner4.setSelectedIndex((int) (2));
 break; }
case 2: {
 //BA.debugLineNum = 215;BA.debugLine="Spinner4.SelectedIndex = 3";
mostCurrent._spinner4.setSelectedIndex((int) (3));
 break; }
case 3: {
 //BA.debugLineNum = 217;BA.debugLine="Spinner4.SelectedIndex = 4";
mostCurrent._spinner4.setSelectedIndex((int) (4));
 break; }
case 4: {
 //BA.debugLineNum = 219;BA.debugLine="Spinner4.SelectedIndex = 5";
mostCurrent._spinner4.setSelectedIndex((int) (5));
 break; }
case 5: {
 //BA.debugLineNum = 221;BA.debugLine="Spinner4.SelectedIndex = 6";
mostCurrent._spinner4.setSelectedIndex((int) (6));
 break; }
}
;
 //BA.debugLineNum = 223;BA.debugLine="End Sub";
return "";
}
public static String  _panel8_click() throws Exception{
int _index = 0;
 //BA.debugLineNum = 226;BA.debugLine="Sub Panel8_Click";
 //BA.debugLineNum = 228;BA.debugLine="arr(1).Text = Spinner3.SelectedItem";
mostCurrent._arr[(int) (1)].setText((Object)(mostCurrent._spinner3.getSelectedItem()));
 //BA.debugLineNum = 229;BA.debugLine="arr(2).Text = Spinner4.SelectedItem";
mostCurrent._arr[(int) (2)].setText((Object)(mostCurrent._spinner4.getSelectedItem()));
 //BA.debugLineNum = 230;BA.debugLine="Dim index As Int = arr(0).Text - 1";
_index = (int) ((double)(Double.parseDouble(mostCurrent._arr[(int) (0)].getText()))-1);
 //BA.debugLineNum = 232;BA.debugLine="cuList.Set(index,Spinner3.SelectedItem)";
mostCurrent._culist.Set(_index,(Object)(mostCurrent._spinner3.getSelectedItem()));
 //BA.debugLineNum = 233;BA.debugLine="grdList.Set(index,Spinner4.SelectedItem)";
mostCurrent._grdlist.Set(_index,(Object)(mostCurrent._spinner4.getSelectedItem()));
 //BA.debugLineNum = 235;BA.debugLine="CalculateCGPA(cuList,grdList)";
_calculatecgpa(mostCurrent._culist,mostCurrent._grdlist);
 //BA.debugLineNum = 236;BA.debugLine="PnlUpdate.Visible = False";
mostCurrent._pnlupdate.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 237;BA.debugLine="End Sub";
return "";
}
public static String  _pnlcalculate_click() throws Exception{
 //BA.debugLineNum = 140;BA.debugLine="Sub pnlCalculate_Click";
 //BA.debugLineNum = 141;BA.debugLine="Log(\"yes\")";
anywheresoftware.b4a.keywords.Common.Log("yes");
 //BA.debugLineNum = 142;BA.debugLine="CalculateCGPA(cuList,grdList)";
_calculatecgpa(mostCurrent._culist,mostCurrent._grdlist);
 //BA.debugLineNum = 143;BA.debugLine="End Sub";
return "";
}
public static String  _pnlupdate_click() throws Exception{
 //BA.debugLineNum = 239;BA.debugLine="Sub PnlUpdate_Click";
 //BA.debugLineNum = 240;BA.debugLine="PnlUpdate.Enabled = False";
mostCurrent._pnlupdate.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 241;BA.debugLine="PnlUpdate.SendToBack";
mostCurrent._pnlupdate.SendToBack();
 //BA.debugLineNum = 242;BA.debugLine="PnlUpdate.Visible=False";
mostCurrent._pnlupdate.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 243;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
starter._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 18;BA.debugLine="Dim timer1 As Timer";
_timer1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public static String  _timer1_tick() throws Exception{
 //BA.debugLineNum = 82;BA.debugLine="Sub Timer1_Tick";
 //BA.debugLineNum = 83;BA.debugLine="timer1.Enabled = False";
_timer1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 84;BA.debugLine="PnlSplash.SetLayoutAnimated(700,-PnlSplash.Width,";
mostCurrent._pnlsplash.SetLayoutAnimated((int) (700),(int) (-mostCurrent._pnlsplash.getWidth()),(int) (0),mostCurrent._pnlsplash.getWidth(),mostCurrent._pnlsplash.getHeight());
 //BA.debugLineNum = 85;BA.debugLine="PnlHome.SetLayoutAnimated(800,0,0,PnlHome.Width,P";
mostCurrent._pnlhome.SetLayoutAnimated((int) (800),(int) (0),(int) (0),mostCurrent._pnlhome.getWidth(),mostCurrent._pnlhome.getHeight());
 //BA.debugLineNum = 87;BA.debugLine="End Sub";
return "";
}
public static String  _updatecgpa(int _x,String _cu,String _gd) throws Exception{
anywheresoftware.b4a.objects.PanelWrapper _panel0 = null;
anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
anywheresoftware.b4a.objects.LabelWrapper _label2 = null;
anywheresoftware.b4a.objects.LabelWrapper _label3 = null;
 //BA.debugLineNum = 88;BA.debugLine="Sub UpdateCgpa(x As Int, cu As String, gd As Strin";
 //BA.debugLineNum = 89;BA.debugLine="Dim panel0 As Panel";
_panel0 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 90;BA.debugLine="panel0 = ScrollView1.Panel";
_panel0 = mostCurrent._scrollview1.getPanel();
 //BA.debugLineNum = 91;BA.debugLine="Dim panel1 As Panel";
_panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 92;BA.debugLine="Dim label1 As Label";
_label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 93;BA.debugLine="Dim label2 As Label";
_label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 94;BA.debugLine="Dim label3 As Label";
_label3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 99;BA.debugLine="label1.Initialize(\"\")";
_label1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 100;BA.debugLine="label2.Initialize(\"\")";
_label2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 101;BA.debugLine="label3.Initialize(\"\")";
_label3.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 102;BA.debugLine="panel1.Initialize(\"Panel1\")";
_panel1.Initialize(mostCurrent.activityBA,"Panel1");
 //BA.debugLineNum = 105;BA.debugLine="panel1.Color = Colors.ARGB(0,0,0,0)";
_panel1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 106;BA.debugLine="panel1.BringToFront";
_panel1.BringToFront();
 //BA.debugLineNum = 108;BA.debugLine="label1.Text = (x +1) & \".\"";
_label1.setText((Object)(BA.NumberToString((_x+1))+"."));
 //BA.debugLineNum = 109;BA.debugLine="label1.TextSize = 25";
_label1.setTextSize((float) (25));
 //BA.debugLineNum = 110;BA.debugLine="label1.TextColor = Colors.Black";
_label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 111;BA.debugLine="label1.Tag = 0";
_label1.setTag((Object)(0));
 //BA.debugLineNum = 113;BA.debugLine="label2.Text = cu";
_label2.setText((Object)(_cu));
 //BA.debugLineNum = 114;BA.debugLine="label2.Textsize = 25";
_label2.setTextSize((float) (25));
 //BA.debugLineNum = 115;BA.debugLine="label2.TextColor = Colors.RGB(107,142,35)";
_label2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (107),(int) (142),(int) (35)));
 //BA.debugLineNum = 116;BA.debugLine="label2.Gravity = Gravity.CENTER";
_label2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 117;BA.debugLine="label2.Tag = 1";
_label2.setTag((Object)(1));
 //BA.debugLineNum = 119;BA.debugLine="label3.Text = gd";
_label3.setText((Object)(_gd));
 //BA.debugLineNum = 120;BA.debugLine="label3.TextColor = Colors.RGB(107,142,35)";
_label3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (107),(int) (142),(int) (35)));
 //BA.debugLineNum = 121;BA.debugLine="label3.Gravity = Gravity.CENTER";
_label3.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 122;BA.debugLine="label3.TextSize = 25";
_label3.setTextSize((float) (25));
 //BA.debugLineNum = 123;BA.debugLine="label3.Tag = 2";
_label3.setTag((Object)(2));
 //BA.debugLineNum = 128;BA.debugLine="panel0.AddView(panel1,0,PanelTop,ScrollView1.Widt";
_panel0.AddView((android.view.View)(_panel1.getObject()),(int) (0),_paneltop,mostCurrent._scrollview1.getWidth(),_panelheight);
 //BA.debugLineNum = 130;BA.debugLine="panel1.AddView(label1,5dip,5dip,20%x,65dip)";
_panel1.AddView((android.view.View)(_label1.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (65)));
 //BA.debugLineNum = 131;BA.debugLine="panel1.AddView(label2,label1.Width + 10dip,5dip,4";
_panel1.AddView((android.view.View)(_label2.getObject()),(int) (_label1.getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 132;BA.debugLine="panel1.AddView(label3,label2.Left + label2.Width";
_panel1.AddView((android.view.View)(_label3.getObject()),(int) (_label2.getLeft()+_label2.getWidth()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 135;BA.debugLine="PanelTop=PanelTop+PanelHeight+5dip";
_paneltop = (int) (_paneltop+_panelheight+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)));
 //BA.debugLineNum = 137;BA.debugLine="End Sub";
return "";
}
}
