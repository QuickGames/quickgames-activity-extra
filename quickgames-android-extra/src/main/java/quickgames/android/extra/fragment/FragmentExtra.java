package quickgames.android.extra.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.reflect.Field;

import quickgames.android.extra.AEException;
import quickgames.android.extra.RNames;
import quickgames.android.extra.Util;
import quickgames.android.extra.annotation.FieldId;
import quickgames.android.extra.annotation.FieldName;
import quickgames.android.extra.annotation.LayoutParams;
import quickgames.android.extra.annotation.LayoutParamsName;
import quickgames.android.extra.interfaces.GetSetViewValue;
import quickgames.android.extra.interfaces.ReportError;

public class FragmentExtra extends Fragment
        implements GetSetViewValue, ReportError {

    //region FIELDS

    protected View m_fragmentView;

    //endregion

    //region OVERRIDE

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        // LayoutParams processing
        try {
            @LayoutRes int resource = mOnCreateViewLayoutParams();
            m_fragmentView = inflater.inflate(resource, container, false);

            // Init views
            mOnCreateFields();

            onCreateViewPost();

        } catch (AEException e) {
            String message = e.getMessage();
            showError(message);
        }

        return m_fragmentView;
    }

    /**
     * Called when the view previously created by {@link #onCreateView} has
     * been detached from the fragment.  The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after {@link #onStop()} and before {@link #onDestroy()}.  It is called
     * <em>regardless</em> of whether {@link #onCreateView} returned a
     * non-null view.  Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Save values
        Util.savePreferences(this);
    }

    @Override
    public void logError(Throwable e) {
        Util.logError(this, e);
    }

    //region SHOW_ERROR

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param text The text to show.  Can be formatted text.
     */
    @Override
    public void showError(@NonNull CharSequence text) {
        if (m_fragmentView != null) {
            try {
                Snackbar.make(m_fragmentView, text, Snackbar.LENGTH_SHORT).show();
                return;
            } catch (IllegalArgumentException ignored) {
            }
        }

        m_showError(text);
    }

    /**
     * Make a standard toast that just contains a text view with the text from a resource.
     *
     * @param resId The resource id of the string resource to use.  Can be formatted text.
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    @Override
    public void showError(int resId) {
        if (m_fragmentView != null) {
            try {
                Snackbar.make(m_fragmentView, resId, Snackbar.LENGTH_SHORT).show();
                return;
            } catch (IllegalArgumentException ignored) {
            }
        }

        m_showError(resId);
    }

    //endregion

    //endregion

    //region PRIVATE_PROTECTED_METHODS

    //region SHOW_ERROR

    private void m_showError(@NonNull CharSequence text) {
        Activity activity = getActivity();
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
    }

    private void m_showError(int resId) {
        Activity activity = getActivity();
        Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
    }

    //endregion

    //endregion

    //region PRIVATE_PROCESSING

    /**
     * LayoutParams processing
     *
     * @return Layout resource ID.
     */
    @LayoutRes
    private int mOnCreateViewLayoutParams() throws AEException {
        @LayoutRes int resource = 0;
        @StringRes int titleId = 0;

        Class<? extends FragmentExtra> clazz = getClass();
        LayoutParams layoutParams = clazz.getAnnotation(LayoutParams.class);
        if (layoutParams != null) {
            resource = layoutParams.layoutResID();

            // titleResId
            titleId = layoutParams.titleResId();
        }

        if (resource == 0) {
            LayoutParamsName layoutParamsName = clazz.getAnnotation(LayoutParamsName.class);
            if (layoutParamsName != null) {
                Activity activity = getActivity();

                String layoutRes = layoutParamsName.layoutRes();
                resource = RNames.layout.getId(activity, layoutRes);

                if (titleId == 0) {
                    // titleResId
                    String titleRes = layoutParamsName.titleRes();
                    if (!titleRes.isEmpty()) {
                        @StringRes int titleResId = RNames.string.getId(activity, titleRes);
                        if (titleResId != 0)
                            titleId = titleResId;
                    }
                }
            }
        }

        if (resource == 0)
            throw new AEException(RNames.string.ERR_LAYOUT_RES_ID_NOT_FOUND);

        if (titleId != 0) {
            String title = getString(titleId);
            Activity activity = getActivity();
            activity.setTitle(title);
        }

        return resource;
    }

    private void mOnCreateFields() {
        // Init views

        Activity activity = getActivity();

        Class<? extends FragmentExtra> clazz = getClass();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {

            @IdRes int fieldId = 0;

            FieldId annotationFieldId = field.getAnnotation(FieldId.class);
            if (annotationFieldId != null) {
                fieldId = annotationFieldId.value();
            }

            if (fieldId == 0) {
                FieldName annotationFieldName = field.getAnnotation(FieldName.class);
                if (annotationFieldName != null) {
                    String value = annotationFieldName.value();
                    if (!value.isEmpty()) {
                        int id = RNames.id.getId(activity, value);
                        if (id != 0)
                            fieldId = id;
                    }
                }
            }

            if (fieldId != 0) {
                try {
                    View view = m_fragmentView.findViewById(fieldId);
                    field.set(this, view);

                    // field preferences
                    Util.loadPreferenceView(this, field, view);
                } catch (IllegalAccessException e) {
                    logError(e);
                }
            }

        }
    }

    //endregion

    //region PROTECTED_METHODS

    protected void startFragment(Class fragmentClass) {
        try {
            Util.startFragment(getActivity(), fragmentClass);
        } catch (AEException e) {
            showError(e.getMessage());
        }
    }

    //endregion

    //region VIRTUAL_METHODS

    @CallSuper
    protected void onCreateViewPost() {

    }

    @Override
    public void setViewValue(View view, Object value) {

    }

    @Nullable
    @Override
    public Object getViewValue(View view) {
        return null;
    }

    //endregion

}
