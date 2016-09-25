package com.hunter.dribbble.ui.user.search;

import com.hunter.dribbble.api.ApiClient;
import com.hunter.dribbble.api.ApiConstants;
import com.hunter.dribbble.api.PagerMap;
import com.hunter.dribbble.entity.ShotsEntity;

import java.util.List;
import java.util.Map;

import rx.Observable;

public class SearchModel implements SearchContract.Model {

    @Override
    public Observable<List<ShotsEntity>> searchShots(String key, int page) {
        Map<String, String> params = new PagerMap(page);
        params.put(ApiConstants.ParamKey.SEARCH_KEY, key);
        params.put(ApiConstants.ParamKey.PER_PAGE, ApiConstants.ParamValue.SEARCH_PAGE_SIZE + "");

        return ApiClient.getForJsoup().search(params);
    }
}
