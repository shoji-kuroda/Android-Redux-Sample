package com.github.shoji_kuroda.redux;

/**
 * Reducer
 * <p>
 * Created by kuroda02 on 2016/07/05.
 */

public interface Reducer<A extends Action, S extends State> {
    /**
     * ActionとStateから、新しいstateを作成して返すメソッド
     * 引数のStateを更新することはせず、新しいStateを作成する
     * 副作用を起こさないPureな関数でなければならず、AというStateに対して毎回必ずBというStateを返さなければならない
     *
     * @param action
     * @param state
     * @return
     */
    S call(final A action, final S state);
}
