package hotjavi.lei.com.base_module.present;

import android.app.Fragment;

import java.lang.ref.WeakReference;

/**
 * Created by tom on 2017/4/19.
 */

public class BaseObjectPresent<T> {
    private WeakReference<T> mWref;

    public BaseObjectPresent(T t){
        mWref=new WeakReference<T>(t);
    }

    public T getRefObj(){
        return mWref.get();
    }

    public boolean isAvaiable(){
        T t= mWref.get();
        if (t==null) return false;
        if (t instanceof Fragment){
            if (((Fragment) t).getActivity()==null){
                return false;
            }
        }
        return true;
    }
}
