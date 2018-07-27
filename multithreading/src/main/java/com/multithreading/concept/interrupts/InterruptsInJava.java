package com.multithreading.concept.interrupts;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/*
 * Example Use Case
   1) The following are the requirements of the use case:
   2) Make a task that prints 0 through 9 on console.
   3) After printing a number the task should wait 1 sec before printing the next number.
   4) The task runs on a separate thread, other than main application thread. 
   5) After starting the task the main application should wait for 3 sec and then shutdown.
   6) On shutdown the application should request the running task to finish.
   7) Before shutting down completely the application should, at the max, wait for 1 sec for the task to finish.
   8) The task should respond to the finish request by stopping immediately.
 * 
 */

/*
 * In Java, one thread cannot stop other thread. However, it can only request other thread to stop execution.
 * The request is made in the form of an interruption.
 * Calling interrupt() on a thread object sets the interrupt flag as true.
 */

/*
 * Usage of the Executor framework is preferred over Threads as it provides separation of task execution from the thread management.
 * The Executor framework is a complete asynchronous task execution framework.
 */
public class InterruptsInJava {
	public static class TaskThread implements Runnable {
		@Override
		public void run() {
			for (int i=1; i<=10; i++) {
				System.out.println(i);
				try {
					Thread.sleep(1_000);
				} catch(InterruptedException ex) {
					System.out.println("Interrupted...stopping the task");
					break;
				}
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		demonstration3();
	}
	
	public static void demonstration1() throws InterruptedException {
		Thread task = new Thread(new TaskThread());
		task.start();
		Thread.sleep(3_000);	
		task.interrupt();
		task.join(1_000);
		System.out.println("Good bye...from main");
	}

	public static void demonstration2() throws InterruptedException {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(new TaskThread());
		Thread.sleep(3_000);
		executor.shutdownNow();
		executor.awaitTermination(1, TimeUnit.SECONDS);
	}

	public static void demonstration3() throws InterruptedException {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<?> f = executor.submit(() -> {
			printNumbers();
			printNumbers();
		});
		Thread.sleep(3_000);
		executor.shutdownNow();
		//OR
		//f.cancel(true);
		executor.awaitTermination(1, TimeUnit.SECONDS);
	}

	public static void printNumbers() {
		for (int i=1; i<=10; i++) {
			System.out.println(i);
			try {
				Thread.sleep(1_000);
			} catch(InterruptedException ex) {
				System.out.println("Interrupted...stopping the task");
				Thread.currentThread().interrupt(); // preserve interruption status
				break;
			}
		}
	}
}
