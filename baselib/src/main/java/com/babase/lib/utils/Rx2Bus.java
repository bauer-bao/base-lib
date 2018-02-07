package com.babase.lib.utils;

import com.hwangjr.rxbus.Bus;
import com.hwangjr.rxbus.thread.ThreadEnforcer;

/**
 * github  https://github.com/AndroidKnife/RxBus
 * 教程  http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/0616/4355.html
 * <p>
 * 生成方法  @Produce   或者  post
 * 接收方法  @Subscribe
 * --------->thread = EventThread.MAIN_THREAD,
 * --------->tags = {@Tag(BusAction.EAT_TEST)}
 * --------->接收者根据参数类型而接收
 * 生成和接收方法均可指定EventThread
 * <p>
 * Tag  @Tag 可以保证只有相同的tag才能获取到信息
 *
 * @author bauer_bao on 17/6/9.
 */

public final class Rx2Bus {
    private static Bus sBus;

    public synchronized static Bus get() {
        if (sBus == null) {
            sBus = new Bus(ThreadEnforcer.ANY);
        }
        return sBus;
    }
}
