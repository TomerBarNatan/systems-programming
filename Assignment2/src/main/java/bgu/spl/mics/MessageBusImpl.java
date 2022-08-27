package bgu.spl.mics;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	final private ConcurrentHashMap<MicroService, BlockingQueue<Message>> microservices_to_message_map;
	final private HashMap<Class<? extends Event>, Queue<MicroService>> events_to_microservices_map; //changed from concurrent
	final private HashMap<Class<? extends Broadcast>, Queue<MicroService>> broadcasts_to_microservices_map;
	final private HashMap<Message,Future> future_map;
	final private Object key;

	/**
	 * Constructor
	 */
	public MessageBusImpl(){
		this.microservices_to_message_map = new ConcurrentHashMap<>();
		this.events_to_microservices_map = new HashMap<>();
		this.broadcasts_to_microservices_map = new HashMap<>();
		this.future_map = new HashMap<>();
		this.key = new Object();
	}

	/**
	 * @return an instance of MessageBusImpl in order to preserve it as a singleton
	 */
	public static MessageBusImpl getInstance() {
		return MessageBusImplHolder.instance;
	}

	private static class MessageBusImplHolder {
		private static final MessageBusImpl instance = new MessageBusImpl();
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized (events_to_microservices_map) {
			if (events_to_microservices_map.get(type) == null) {
				events_to_microservices_map.put(type, new ConcurrentLinkedQueue<>());
			}
		}
		events_to_microservices_map.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (broadcasts_to_microservices_map) {
			if (broadcasts_to_microservices_map.get(type) == null) {
				broadcasts_to_microservices_map.put(type, new ConcurrentLinkedQueue<>());
			}
		}
		broadcasts_to_microservices_map.get(type).add(m);
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		Future future = future_map.get(e);
		while (future == null){
			synchronized (key){
				try {
					key.wait();
					future = future_map.get(e);
				} catch (InterruptedException error) {
					error.printStackTrace();
				}
			}
		}
		future.resolve(result);
		future_map.remove(e);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if(broadcasts_to_microservices_map.get(b.getClass()) != null){
			for(MicroService microservice: broadcasts_to_microservices_map.get(b.getClass())){
				try {
					microservices_to_message_map.get(microservice).add(b);
				} catch (NullPointerException error) {
					error.printStackTrace();
				}
			}
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
        Queue<MicroService> currentQueue = events_to_microservices_map.get(e.getClass());
		if(currentQueue == null){
			return null;
		}
        synchronized (currentQueue) {
			while (true) {
				MicroService current_microservice = currentQueue.poll();
				if (current_microservice == null) {
					return null;
				}
				try {
					microservices_to_message_map.get(current_microservice).add(e);
					events_to_microservices_map.get(e.getClass()).add(current_microservice);
					break;
				} catch (NullPointerException error) {
					error.printStackTrace();
				}
			}
		}
        Future future = new Future<T>();
        future_map.put(e,future);
        synchronized (key) {
			key.notifyAll();
		}
        return future;
	}

	@Override
	public void register(MicroService m) {
		microservices_to_message_map.put(m,new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(MicroService m) {
		BlockingQueue<Message> message_queue = microservices_to_message_map.get(m);
		while (!message_queue.isEmpty()) {
			Message message = message_queue.poll();
			if (future_map.get(message) != null) {     //to avoid casting
				future_map.get(message).resolve(null);
				future_map.remove(message);
			}
		}
		microservices_to_message_map.remove(m);
		for (Queue<MicroService> currentQueue : events_to_microservices_map.values()) {
			synchronized (currentQueue) {
				currentQueue.remove(m);
			}
		}
		for (Queue<MicroService> currentQueue : broadcasts_to_microservices_map.values()) {
			currentQueue.remove(m);
		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if(microservices_to_message_map.get(m) == null){
			return null;
		}
		return microservices_to_message_map.get(m).take();
	}
}
