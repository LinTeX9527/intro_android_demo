package codepath.apps.demointroandroid;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class DemoSelector extends Activity {
	
	ExpandableListView elvChapters;
	ChaptersListAdapter elaAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo_selector);
	    setupChaptersListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_demo_selector, menu);
		return true;
	}


    /**
     * 初始化界面，给列表控件加载数据，并且添加监听器
     */
	private void setupChaptersListView() {
		elvChapters = (ExpandableListView)findViewById(R.id.elvChapters);
		elaAdapter = new ChaptersListAdapter();
		elvChapters.setAdapter(elaAdapter);
		elvChapters.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
				String exerciseTitle =  (String)elaAdapter.getChild(groupPosition, childPosition);
                Class<? extends Activity> exerciseClass = elaAdapter.getExerciseClass(groupPosition, childPosition, id);
                if (exerciseClass != null) {
                	Toast.makeText(DemoSelector.this, exerciseTitle, Toast.LENGTH_LONG).show();
                	startActivity(new Intent(DemoSelector.this, exerciseClass));	
                } else {
                	Toast.makeText(DemoSelector.this, "Exercise Not Available", Toast.LENGTH_SHORT).show();
                }
				return false;
			}
		});

	}


    /**
     * 私有的类 ChapterListAdapter 在构造方法中就自行加载了需要显示的数据
     */
	private class ChaptersListAdapter extends BaseExpandableListAdapter {
        private String[] chapters = getResources().getStringArray(R.array.chapters);
        private String[][] exercises;
        
        public ChaptersListAdapter() {
        	super();
        	exercises = new String[chapters.length][];
        	for (int i=0; i < exercises.length; i++) {
        		int resId = getResources().getIdentifier("chap" + (i+1), "array", getPackageName());
        		exercises[i] = getResources().getStringArray(resId);
        	}
        }


        /**
         * 得到子元素对应的数据
         * @param groupPosition 子元素所在的组的位置 （组的索引号）
         * @param childPosition 子元素所在组中的位置 （子元素在这个组中的索引号）
         * @return 子元素中包含的数据
         */
        public Object getChild(int groupPosition, int childPosition) {
            return exercises[groupPosition][childPosition];
        }


        /**
         * 返回指定组中的子元素的ID，这个ID必须在该组中是唯一的。
         * @param groupPosition 子元素所在的组的序号
         * @param childPosition 子元素在组中的序号
         * @return 子元素的序号
         */
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }


        /**
         * 返回指定组中的子元素的个数
         * @param groupPosition 组序号
         * @return 这个组中的子元素的个数
         */
        public int getChildrenCount(int groupPosition) {
            return exercises[groupPosition].length;
        }


        /**
         * 返回一个TextView对象，并设立好基本属性。
         * @return
         */
        public TextView getGenericView() {
            // Layout parameters for the ExpandableListView
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            TextView textView = new TextView(DemoSelector.this);
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setTextSize(20);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            textView.setPadding(60, 20, 20, 20);
            return textView;
        }


        /**
         * 返回一个视图，用来显示指定的组中的指定子元素。
         * @param groupPosition 组序号
         * @param childPosition 子元素在组中的序号
         * @param isLastChild 是否是这个组中的最后一个元素
         * @param convertView 可重复利用的old view。
         * @param parent 这个视图最终要依附到的父视图。
         * @return 用来显示子元素的视图。
         */
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
            TextView textView = getGenericView();
            textView.setPadding(80, 20, 20, 20);
            textView.setText(getChild(groupPosition, childPosition).toString());
            return textView;
        }


        /**
         * 返回指定的组包含的数据（这里一个组返回一个字符串，表示章节名称）
         * @param groupPosition 组序号
         * @return 指定的组包含的数据
         */
        public Object getGroup(int groupPosition) {
            return "Chapter " + (groupPosition + 1) + ": " + chapters[groupPosition];
        }


        /**
         * 返回组个数
         * @return
         */
        public int getGroupCount() {
            return chapters.length;
        }


        /**
         * 返回指定位置的组ID
         * @param groupPosition 指定的位置（默认下标从0开始）
         * @return 组对应的ID
         */
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }


        /**
         * 返回一个视图，用来显示指定的组数据。
         * @param groupPosition 组序号
         * @param isExpanded 组是展开的还是折叠的
         * @param convertView 可重复利用的老视图
         * @param parent 这个视图最终要依附的父视图
         * @return 用来显示指定位置的组的视图
         */
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
            TextView textView = getGenericView();
            textView.setText(getGroup(groupPosition).toString());
            textView.setPadding(100, 20, 20, 20);
            return textView;
        }


        /**
         * 组中的某个子元素是否可以被选择。
         * @param groupPosition 组序号
         * @param childPosition 组中子元素序号
         * @return
         */
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


        /**
         * 即便adapter管理的数据发生了变动，对应的组序号和子元素序号是否保持不变
         * @return 同样的对象是否拥有同样的ID
         */
        public boolean hasStableIds() {
            return true;
        }


        /**
         * 自定义的方法，通过组序号和子元素序号得到对应的Activity 的Class对象
         * @param groupPosition 组序号
         * @param childPosition 子元素序号
         * @param id 未被使用的参数
         * @return
         */
        public Class<? extends Activity> getExerciseClass(int groupPosition, int childPosition, long id) {
        	String exerciseId = "chap" + (groupPosition + 1) + "ex" + (childPosition + 1);
        	return ExerciseActivityMapper.getExerciseClass(exerciseId);
        }
	}

}
