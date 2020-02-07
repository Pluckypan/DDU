package engineer.echo.study.cmpts.audio;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * CoolCode.java
 * Info: CoolEditor常量
 */
public class CoolCode {

    public static final String TAG = "CoolEditor";

    public static final int MSG_PLAY_PROGRESS = 0x001;
    public static final int MSG_PLAY = 0x002;
    public static final int MSG_PAUSE = 0x003;
    public static final int MSG_PREPARE = 0x004;

    public static final int PLAY = 1;
    public static final int PAUSE = 2;
    public static final int SEEK = 3;
    public static final int STOP = 4;

    @IntDef({PLAY, PAUSE, SEEK, STOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Signal {

    }
}
