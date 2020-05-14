package com.zzq.democollection.ipc;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class IpcButton extends AppCompatButton {

    private ButtonType mType;

    public IpcButton(Context context) {
        super(context);
        mType = ButtonType.NONE;
    }

    public IpcButton(Context context, AttributeSet attrs, ButtonType mType) {
        super(context, attrs);
        this.mType = mType;
    }

    private void setButtonType(ButtonType type) {
        mType =type;
    }

    private ButtonType getButtonType(){
        return mType;
    }

    public enum ButtonType {
        NONE,
        BUNDLE;
    }
}
