package quickgames.extra.view;

import quickgames.android.extra.interfaces.GetSetValue;

/**
 * @deprecated As of release 0.0.4,
 * replaced by {@link #( GetSetValue )},
 * will be deleted of release 0.1
 */
public interface SetGetValue<T> {
    void setViewValue(T value);
    T getViewValue();
}
