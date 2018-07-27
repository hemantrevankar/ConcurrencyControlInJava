package com.multithreading.concept;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/*
 * WAIT()
 * wait() method is a final method defined in the Object.class
 * this method can be called only after obtaining a monitor/lock on the shared object access to which is synchronized.
 * the thread that calls wait(), will release the lock on the object and goes into a wait state.
 * this waiting thread will wait indefinitely until it is woken up by calling notify() or notifyAll() by other thread.
 * wait(timeout) will wait and timeout after the specified time and resume.
 */
/*
 * NOTIFY()
 * notify() is a final method defined in the Object.class
 * this method can be called only after obtaining a monitor/lock on the shared object access to which is synchronized.
 * the thread calls notify() will alert one among many other waiting threads to wake up
 */
/*
 * NOTIFYALL()
 * notifyAll() is a final method defined in the Object.class
 * this method can be called only after obtaining a monitor/lock on the shared object access to which is synchronized.
 * the thread calls notifyAll() will alert all other waiting threads to wake up
 */
/*
 * JOIN()
 * join() method is a final method defined inside a Thread class.
 * this method when called on a thread object by another thread 'T' will cause T to wait until the other threads completes.
 * If t is a thread object whose thread is currently executing then calling t.join() will causing the currently running thread
 * to pause while t finishes.
 * Essentially join() will put the current thread on wait until the thread on which it is called is DEAD. 
 */
/*
 * COUNTDOWNLATCH
 * countDownLatch is a multithreading programmable construct used to put a currently running thread to wait
 * while all other threads have completed execution.
 * We initialize the count down latch with the number of threads in execution.
 * The main thread will be paused until the await() method returns true.
 * await() method checks for the count if it equals zero. Each individual thread before termination should countdown the latch.
 */
public class WaitAndNotify {
	private static final Queue<String> queue = new ConcurrentLinkedQueue<>();
	private static final long time = System.currentTimeMillis();
	static CountDownLatch latch = new CountDownLatch(3);
	private static final String[] arr = new String[] {"A", "B", "C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

	public static class Consumer implements Runnable {
		public void run() {
			while(System.currentTimeMillis() < time + 10000) {
				synchronized (queue) {
					try {
						System.out.println("Thread: " + Thread.currentThread().getName() + " waiting...");
						queue.wait();
					} catch (InterruptedException e) {
					}
				}
				System.out.println("Thread: " + Thread.currentThread().getName() + " woken up...");
				if (!queue.isEmpty()) {
					String a = queue.poll();
					System.out.println("Thread: [" + Thread.currentThread().getName() + "] :: read value => " + a);
				} else {
					System.out.println("queue is empty nothing to read");
				}
			}
			//latch.countDown();
		}
	}

	public static class Producer implements Runnable {
		public void run() {
			int i=1;
			while (System.currentTimeMillis() < time + 10000) {
				int r = new Random().nextInt(26);
				queue.add(arr[r]);
				System.out.println(i++ + " Thread: [" + Thread.currentThread().getName() + "] :: added value to the queue => " + arr[r]);
				synchronized (queue) {
					System.out.println("Thread: [" + Thread.currentThread().getName() + "] :: notified waiting threads");
					queue.notify();
				}
				try {
					System.out.println("Thread: [" + Thread.currentThread().getName() + "] :: going to bed");
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			synchronized (queue) {
				queue.notifyAll();
			}
			//latch.countDown();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Thread pt = new Thread(new Producer(), "Producer");
		Thread ct1 = new Thread(new Consumer(), "Consumer1");
		Thread ct2 = new Thread(new Consumer(), "Consumer2");
		ct1.start();
		ct2.start();
		pt.start();
		//latch.await();
		ct1.join();
		ct2.join();
		pt.join();
		System.out.println("Size of queue:" + queue.size());
	}
}