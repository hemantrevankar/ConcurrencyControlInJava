package com.multithreading.concept;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumerProblem {
	private static final Queue<String> sharedQueue = new LinkedBlockingQueue<>(10);
	private static final String[] arr = new String[] {"A", "B", "C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	private static final long startTime = System.currentTimeMillis();
	private static final CountDownLatch latch = new CountDownLatch(2);
	public static class Consumer implements Runnable {
		@Override
		public void run() {
			while(true) {
				try {
				System.out.println("Thread [" + Thread.currentThread().getName() + "] read: " + sharedQueue.poll());
				Thread.sleep(2000);
				} catch(Exception ex) {
					System.err.println("Exception for Thread: [" + Thread.currentThread().getName() + "]" + ex.getMessage());
				}
			}
		}
	}

	public static class Producer implements Runnable {
		@Override
		public void run() {
			while (true) {
				int r = new Random().nextInt(26);
				try {
				String val = (sharedQueue.add(arr[r])) ? arr[r] : "NOT ADDED";
				System.out.println("Thread [" + Thread.currentThread().getName() + "] added: " + val);
				Thread.sleep(2000);
				} catch(Exception ex) {
					System.err.println("Exception for Thread: [" + Thread.currentThread().getName() + "]" + ex.getMessage());
				}
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread pt = new Thread(new Producer(), "Producer");
		Thread ct = new Thread(new Consumer(), "Consumer");
		Thread ct1 = new Thread(new Consumer(), "Consumer1");
		pt.start();
		ct.start();
		ct1.start();
		System.out.println("queue size: " + sharedQueue.size());
	}
}
