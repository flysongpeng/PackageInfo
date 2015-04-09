package info.zhegui.packageinfo;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ProgressBar mProgressBar;
    private ListView mListView;
    List<PackageInfo> listPackageInfo = new ArrayList<PackageInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mListView = (ListView) findViewById(R.id.listView);

        new Thread() {
            @Override
            public void run() {


                List<PackageInfo> packages = getPackageManager()
                        .getInstalledPackages(PackageManager.GET_SIGNATURES);
                // System.out.println("---------thisPackageName:" + thisPackageName);
                for (int i = 0; i < packages.size(); i++) {
                    PackageInfo packageInfo = packages.get(i);

//                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0
//                            && (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0) {
//                        // APP WAS INSTALL AND NOT AS AN UPDATE TO A BUILD-IN SYSTEM APP
//                        continue;
//                    }

                    // 跳过百乐汇程序
                    if (packageInfo.packageName.equals(MainActivity.this.getPackageName()))
                        continue;

//                    Applied applied = new Applied();
//                    applied.setApkimage(packageInfo.applicationInfo.loadIcon(context
//                            .getPackageManager()));
//                    applied.setPackageName(packageInfo.packageName);
//                    applied.setTitle(packageInfo.applicationInfo.loadLabel(
//                            context.getPackageManager()).toString());
//                    applied.setStar("0");
//                    applied.setVersionName(packageInfo.versionName);
//                    applied.setVersionCode(packageInfo.versionCode + "");
//                    applied.setApksize(Integer.valueOf((int) new File(
//                            packageInfo.applicationInfo.publicSourceDir).length()) + "");
                    // try {
                    // String publicKey = getPublicKey(packageInfo.signatures[0]
                    // .toByteArray());
                    // // Log.i(TAG, "line 1444------publicKey:" + publicKey);
                    // if (publicKey != null)
                    // applied.setPublicKey(publicKey);
                    // } catch (Exception e) {
                    // e.printStackTrace();
                    // }
                    // applied.set = packageInfo.applicationInfo
                    // .loadIcon(getPackageManager());

                    // Log.i(TAG,
                    // "- line 1673---applied:"+applied.getPackageName());

                    listPackageInfo.add(packageInfo);
                }

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        mListView.setAdapter(new AdapterPackageInfo());
                    }
                });
            }
        }.start();
    }

    class AdapterPackageInfo extends BaseAdapter {

        @Override
        public int getCount() {
            return listPackageInfo.size();
        }

        @Override
        public Object getItem(int position) {
            return listPackageInfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.packageinfo_item, null);
                holder.ivLogo = (ImageView) convertView.findViewById(R.id.iv);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvPacakgeName = (TextView) convertView.findViewById(R.id.tv_package_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final PackageInfo packageInfo = listPackageInfo.get(position);
            holder.ivLogo.setImageDrawable(packageInfo.applicationInfo.loadIcon(MainActivity.this
                    .getPackageManager()));
            holder.tvTitle.setText(packageInfo.applicationInfo.loadLabel(
                    MainActivity.this.getPackageManager()).toString());
            holder.tvPacakgeName.setText(packageInfo.packageName);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", packageInfo.packageName, null);
                        intent.setData(uri);
                        startActivity(intent);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView ivLogo;
        TextView tvTitle;
        TextView tvPacakgeName;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
