package com.hunter.dribbble.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hunter.dribbble.App;
import com.hunter.dribbble.AppConstants;
import com.hunter.dribbble.R;
import com.hunter.dribbble.base.BaseActivity;
import com.hunter.dribbble.event.EventViewMode;
import com.hunter.dribbble.ui.user.login.LoginActivity;
import com.hunter.dribbble.ui.shots.list.ShotsListFragment;
import com.hunter.dribbble.ui.user.search.dialog.SearchFragment;
import com.hunter.dribbble.widget.spinner.MaterialSpinner;
import com.hunter.lib.util.SPUtils;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hunter.dribbble.utils.ViewModelUtils.VIEW_MODE_TITLE_RES;

public class MainActivity extends BaseActivity implements AccountHeader.OnAccountHeaderListener,
        Toolbar.OnMenuItemClickListener {

    private static final String TAG_SEARCH = "tag_search";

    private static final int VIEW_MODE_ICON_RES[] = {
            R.mipmap.ic_action_small_info,
            R.mipmap.ic_action_small,
            R.mipmap.ic_action_large_info,
            R.mipmap.ic_action_large
    };

    @BindView(R.id.toolbar_main)
    Toolbar         mToolbar;
    @BindView(R.id.spinner_selector_type)
    MaterialSpinner mSpinnerSelectorType;
    @BindView(R.id.spinner_selector_sort)
    MaterialSpinner mSpinnerSelectorSort;
    @BindView(R.id.spinner_selector_time)
    MaterialSpinner mSpinnerSelectorTime;

    private ShotsListFragment mShotsListFragment;
    private SearchFragment    mSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        shouldAuthorize();
        initNavDrawer();
        initFragment();
        initSpinner();
    }

    private void initNavDrawer() {
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(this);

        ProfileDrawerItem navHeader = new ProfileDrawerItem().withName("点击头像登录")
                .withIcon(ContextCompat.getDrawable(this, R.mipmap.ic_launcher));
        AccountHeader accountHeader = new AccountHeaderBuilder().withActivity(this)
                .addProfiles(navHeader)
                .withSelectionListEnabled(false)
                .withOnAccountHeaderListener(this)
                .build();
        DrawerBuilder builder = new DrawerBuilder();
        builder.withActivity(this)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(accountHeader)
                .addDrawerItems(new PrimaryDrawerItem().withName("Home")
                                        .withIcon(R.drawable.iv_home_grey_24dp)
                                        .withSelectedIcon(R.drawable.iv_home_pink_24dp),
                                new PrimaryDrawerItem().withName("Follower")
                                        .withIcon(R.drawable.iv_follower_grey_24dp)
                                        .withSelectedIcon(R.drawable.iv_follower_pink_24dp),
                                new PrimaryDrawerItem().withName("My Buckets")
                                        .withIcon(R.drawable.iv_bucket_grey_24dp)
                                        .withSelectedIcon(R.drawable.iv_bucket_pink_24dp),
                                new PrimaryDrawerItem().withName("My Likes")
                                        .withIcon(R.drawable.iv_like_grey_24dp)
                                        .withSelectedIcon(R.drawable.iv_like_pink_24dp),
                                new DividerDrawerItem(),
                                new SecondaryDrawerItem().withName("Settings")
                                        .withIcon(R.drawable.iv_settings_grey_24dp)
                                        .withSelectedIcon(R.drawable.iv_settings_pink_24dp));

        builder.build();
    }

    private void initSpinner() {
        mSpinnerSelectorType.setItems(AppConstants.SELECTOR_TYPE);
        mSpinnerSelectorType.setOnItemClickListener(mShotsListFragment);

        mSpinnerSelectorSort.setItems(AppConstants.SELECTOR_SORT);
        mSpinnerSelectorSort.setOnItemClickListener(mShotsListFragment);

        mSpinnerSelectorTime.setItems(AppConstants.SELECTOR_TIME);
        mSpinnerSelectorTime.setOnItemClickListener(mShotsListFragment);
    }

    private void shouldAuthorize() {
        if (!(boolean) SPUtils.get(this, AppConstants.SP_IS_FIRST, false)) {
            startActivityForResult(new Intent(this, LoginActivity.class), AppConstants.REQUEST_CODE_LOGIN);
            SPUtils.put(this, AppConstants.SP_IS_FIRST, true);
        }
    }

    private void initFragment() {
        mShotsListFragment = ShotsListFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container_main, mShotsListFragment).commit();

        mSearchFragment = SearchFragment.newInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == AppConstants.REQUEST_CODE_LOGIN) {
            showToast("授权成功");
        }
    }

    /**
     * 点击侧边栏头像事件
     */
    @Override
    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
        startActivityForResult(new Intent(this, LoginActivity.class), AppConstants.REQUEST_CODE_LOGIN);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_mode, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_small_info:
            case R.id.menu_small:
            case R.id.menu_large_info:
            case R.id.menu_large:
                int viewModeIndex = item.getOrder();
                invalidateOptionsMenu();
                EventBus.getDefault().post(new EventViewMode(viewModeIndex));

                String text = "浏览模式切换为 <strong>「" + getString(VIEW_MODE_TITLE_RES[viewModeIndex]) + "」</strong>";
                showToast(Html.fromHtml(text));
                break;

            case R.id.menu_search:
                mSearchFragment.show(getSupportFragmentManager(), TAG_SEARCH);
                break;
        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_view_mode).setIcon(VIEW_MODE_ICON_RES[App.getInstance().getViewMode()]);
        return true;
    }

}