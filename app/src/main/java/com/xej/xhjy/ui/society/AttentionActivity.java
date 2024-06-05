package com.xej.xhjy.ui.society;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.ui.view.TextviewTobTabView;
import com.xej.xhjy.ui.view.TobTabView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author dazhi
 * @class MessageActivity 关注列表,俩fragment，关注的人和关注的话题，长按删除
 * @Createtime 2018/11/28 15:18
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class AttentionActivity extends BaseActivity {

    @BindView(R.id.people_tab)
    TextviewTobTabView peopleTab;
    @BindView(R.id.topic_tab)
    TextviewTobTabView topicTab;
    @BindView(R.id.vp_attent)
    ViewPager vpAttent;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<TextviewTobTabView> mTabs = new ArrayList<>();
    private AttentPeopleFragment peopleFragment;
    private AttentTopticFragment topicFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        peopleFragment = new AttentPeopleFragment();
        topicFragment = new AttentTopticFragment();
        mFragments.add(peopleFragment);
        mFragments.add(topicFragment);
        mTabs.add(peopleTab);
        mTabs.add(topicTab);
        peopleTab.setText("关注的人");
        peopleTab.setCheck(true);
        topicTab.setText("关注的话题");
        topicTab.setCheck(false);
        vpAttent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mTabs.size(); i++) {
                    if (position == i) {
                        mTabs.get(i).setCheck(true);
                    } else {
                        mTabs.get(i).setCheck(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //页面切换动画
        vpAttent.setPageTransformer(true, new AccordionTransformer());
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }
        };
        vpAttent.setAdapter(mAdapter);
    }

    @OnClick(R.id.people_tab)
    void imList() {
        peopleTab.setCheck(true);
        topicTab.setCheck(false);
        vpAttent.setCurrentItem(0);
    }

    @OnClick(R.id.topic_tab)
    void newsList() {
        peopleTab.setCheck(false);
        topicTab.setCheck(true);
        vpAttent.setCurrentItem(1);
    }
}
