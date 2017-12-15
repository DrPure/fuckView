package ml.qingsu.fuckview;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import ml.qingsu.fuckview.first_run_library.FirstRun;
import ml.qingsu.fuckview.view_dumper.DumperService;
import ml.qingsu.fuckview.view_dumper.ViewDumper;
import ml.qingsu.fuckview.view_dumper.ViewDumperProxy;

/**
 * Created by w568w on 2017-7-12.
 */

public class DumpViewerPopupView extends GlobalPopupWindow {
    private ArrayList<ViewDumper.ViewItem> list;
    private final String packageName;
    private int gravity = Gravity.TOP;
    private GlobalPopupWindow fullScreenPopupWindow;
    private boolean no_list;
    private Button refresh;

    public DumpViewerPopupView(Activity activity, String pkg) {
        super(activity);
        packageName = pkg;
        no_list=PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("no_list",true);
    }

    @Override
    protected View onCreateView(final Context context) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dump_viewer_view, null);
        refresh = (Button) layout.findViewById(R.id.dump_refresh);
        final Button close = (Button) layout.findViewById(R.id.dump_close);
        final Button top = (Button) layout.findViewById(R.id.dump_top);


        setFocusable(false);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //一个null会被误认为Runnable
                new refreshTask().execute(null, null);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fullScreenPopupWindow != null)
                    fullScreenPopupWindow.hide();
                hide();
            }
        });
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gravity = (gravity == Gravity.TOP ? Gravity.BOTTOM : Gravity.TOP);
                updateLayout();
            }
        });
        return layout;
    }

    @Override
    protected int getGravity() {
        return gravity;
    }

    private class refreshTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (refresh.getText().toString().equals("解析中...")) {
                cancel(true);
                return;
            }
            refresh.setText("解析中...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            boolean force_root = PreferenceManager.getDefaultSharedPreferences(appContext).getBoolean("force_root", false);
            list = force_root ? ViewDumper.parseCurrentView() : ViewDumperProxy.parseCurrentView(getActivity());
            return null;
        }

        @Override
        protected void onPostExecute(Void vo) {
            super.onPostExecute(vo);
            refresh.setText("刷新解析");

            if (list == null || list.isEmpty()) {
                Toast.makeText(appContext, "解析失败!您启用 净眼 的辅助服务了吗?\n\n启用后请强制停止应用重来！\n启用后请强制停止应用重来！\n启用后请强制停止应用重来！\n重要的事说三遍", Toast.LENGTH_LONG).show();
                return;
            }


            if(no_list)
                fullScreenPopupWindow = new FullScreenPopupWindow(getActivity(), list, packageName, DumpViewerPopupView.this);
            else {
                if(FirstRun.isFirstRun(appContext,"list_popup"))
                    Toast.makeText(appContext, "长按显示菜单,短按定位控件", Toast.LENGTH_LONG).show();
                fullScreenPopupWindow = new FullScreenListPopupWindow(getActivity(), list, packageName, DumpViewerPopupView.this);
            }
            fullScreenPopupWindow.show();
            hide();
        }
    }
}
