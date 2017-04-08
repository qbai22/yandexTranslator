package qbai22.com.yandextranslator.network.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by qbai on 08.04.2017.
 */

public abstract class RetrofitLoader<D, R> extends AsyncTaskLoader<Response<D>> {

    private final R mService;

    private Response<D> mCachedResponse;

    public RetrofitLoader(Context context, R service) {
        super(context);
        mService = service;
    }

    @Override
    public Response<D> loadInBackground() {
        try {
            final D data = call(mService);
            mCachedResponse = Response.ok(data);

        } catch (Exception ex) {
            mCachedResponse = Response.error(ex);
        }

        return mCachedResponse;
    }

    @Override
    public void onStartLoading() {

        super.onStartLoading();

        if (mCachedResponse != null) {

            deliverResult(mCachedResponse);
        }

        if (takeContentChanged() || mCachedResponse == null) {

            forceLoad();
        }
    }

    @Override
    public void onReset() {

        super.onReset();

        mCachedResponse = null;
    }

    public abstract D call(R service);
}
