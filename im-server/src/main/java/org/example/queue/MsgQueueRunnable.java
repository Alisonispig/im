package org.example.queue;

import org.example.config.ImChannelContext;
import org.example.packets.Message;
import org.tio.utils.queue.FullWaitQueue;
import org.tio.utils.queue.TioFullWaitQueue;
import org.tio.utils.thread.pool.AbstractQueueRunnable;

import java.util.concurrent.Executor;

public class MsgQueueRunnable extends AbstractQueueRunnable<Message> {

    private ImChannelContext imChannelContext;

    private volatile FullWaitQueue<Message> msgQueue = null;

    public MsgQueueRunnable(ImChannelContext imChannelContext, Executor executor) {
        super(executor);
        this.imChannelContext = imChannelContext;
    }

    @Override
    public FullWaitQueue<Message> getMsgQueue() {
        if (msgQueue == null) {
            synchronized (this) {
                if (msgQueue == null) {
                    msgQueue = new TioFullWaitQueue<Message>(Integer.MAX_VALUE, true);
                }
            }
        }
        return msgQueue;
    }

    @Override
    public void runTask() {
        Message message;
        while ((message = this.getMsgQueue().poll()) != null) {

        }
    }

}
