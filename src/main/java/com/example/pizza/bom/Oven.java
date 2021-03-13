package com.example.pizza.bom;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Oven {
	private int id;
	private Pizza bakingPizza;
	private int bakingTime;
	private Condition ovenReleased;
	private Lock lock = new ReentrantLock();
	private Queue<Pizza> waitingPizzas = new LinkedList<Pizza>();
	private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Pizza getBakingPizza() {
		return bakingPizza;
	}

	public void setBakingPizza(Pizza bakingPizza) {
		this.bakingPizza = bakingPizza;
	}

	public int getBakingTime() {
		return bakingTime;
	}

	public void setBakingTime(int bakingTime) {
		this.bakingTime = bakingTime;
	}

	public Oven(int id, Pizza bakingPizza, int bakingTime) {
		super();
		this.id = id;
		this.bakingPizza = bakingPizza;
		this.bakingTime = bakingTime;
		this.ovenReleased = lock.newCondition();
	}

	public Oven() {
		super();
	}

	@Override
	public String toString() {
		return "Oven [id=" + id + ", bakingPizza=" + bakingPizza + ", bakingTime=" + bakingTime + "]";
	}
	
	public void bakePizza(Pizza pizza) throws InterruptedException {
		System.out.println("Oven: "+id + "is baking");
		executor.execute(() -> {
			try {
				waitingPizzas.add(pizza);
				lock.lock();
				
				while(this.bakingPizza != null) {
					ovenReleased.await();
				}
				
				this.bakingPizza = pizza;
				countdown(pizza);
				
				System.out.println("Pizza baked! Oven: " + id + " | Pizza: " + pizza.getId());
				
				this.bakingPizza = null;
				waitingPizzas.remove(pizza);
				ovenReleased.signal();
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		});
	}
	
	private void countdown(Pizza pizza) {
		pizza.setBakingCountdown(bakingTime);
		
		while (pizza.getBakingCountdown() > 0) {
			try {
				int timeLeft = pizza.getBakingCountdown();
				pizza.setBakingCountdown(--timeLeft);
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				//Nothing to interrupt
			}
		}
	}

	public int waitingPizzasSize() {
		return waitingPizzas.size();
	}
}
