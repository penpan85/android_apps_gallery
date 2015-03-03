package com.u17;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * AsyncTaskLoader that handles pagination
 */
public abstract class PaginatedLoader<T> extends AsyncTaskLoader<T> {
    private final int mNumItemsPerPage;
    private int mCurrentPage;
    private final int mMaxItems;

    private T mCurrentResults;

    /**
     * Creates a new paginated loader
     * @param context the context
     * @param numItemsPerPage the number of items per page to load
     * @param maxItems the maximum number of items to load
     */
    protected PaginatedLoader(Context context, int numItemsPerPage, int maxItems) {
        super(context);

        mNumItemsPerPage = numItemsPerPage;
        mCurrentPage = 0;
        mMaxItems = maxItems;
    }

    @Override
    protected void onStartLoading() {
        if (mCurrentResults != null) {
            deliverResult(mCurrentResults);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();

        // Making sure to release the results when this loader is reset
        mCurrentResults = null;
        mCurrentPage = 0;
    }

    @Override
    public void deliverResult(T result) {
        mCurrentResults = result;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(result);
        }
    }

    @Override
    public T loadInBackground() {
        int offset = mCurrentPage * mNumItemsPerPage;
        int numItemsToLoad = Math.min(mNumItemsPerPage, mMaxItems - offset);
        return appendResults(mCurrentResults, loadInBackground(mCurrentPage, offset, numItemsToLoad));
    }

    /**
     * Subclasses need to override this method for adding a new page of results to the previously
     * retrieved ones
     * @param oldData the results previously returned
     * @param newData the new page of results.
     * @return the complete results
     */
    protected abstract T appendResults(T oldData, T newData);

    /**
     * Subclasses need to override this method for loading one page of results. This will always
     * be called in a background thread.
     * @param pageNumber the number of the page to load (starts at 0)
     * @param offset the number of items that are already loaded and should be skipped
     * @param numItemsToLoad the number of items to load
     * @return the new page of results
     */
    protected abstract T loadInBackground(int pageNumber, int offset, int numItemsToLoad);

    /**
     * Loads one more page of items. The loader's onLoadFinished() callback will be called with
     * the new data.
     * If the max number of items has already been loaded, this will not do anything.
     */
    public synchronized void loadMore() {
        if (mCurrentPage * mNumItemsPerPage >= mMaxItems) {
            // We already load enough items, don't do anything
            return;
        }
        mCurrentPage += 1;
        forceLoad();
    }
}
