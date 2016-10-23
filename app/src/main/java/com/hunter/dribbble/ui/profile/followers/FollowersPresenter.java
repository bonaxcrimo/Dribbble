package com.hunter.dribbble.ui.profile.followers;

import com.hunter.dribbble.api.BaseSubscriber;
import com.hunter.dribbble.entity.FollowerEntity;

import java.util.List;

public class FollowersPresenter extends FollowersContract.Presenter {

    @Override
    void getFollowers(String id, int page) {
        subscribe(mModel.getFollowers(id, page), new BaseSubscriber<List<FollowerEntity>>(mView) {
            @Override
            protected void onSuccess(List<FollowerEntity> followerEntities) {
                mView.getFollowersOnSuccess(followerEntities);
            }
        });
    }
}
