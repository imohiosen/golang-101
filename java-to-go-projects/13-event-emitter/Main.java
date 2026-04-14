package events;

/**
 * Demo: Event emitter with various listener patterns.
 *
 * Go structure:
 *   main.go                  — Demo usage
 *   events/listener.go       — Listener struct { ID, Callback func(interface{}), Once bool }
 *   events/emitter.go        — Emitter struct with sync.RWMutex
 *                               On/Once/Off/Emit/ListenerCount/EventNames
 *                               map[string][]Listener for event→listeners
 *                               Wildcard "*" support
 *
 * Rust structure:
 *   main.rs                  — Demo usage
 *   listener.rs              — Listener<T> with Box<dyn Fn(&T) + Send + Sync>
 *   emitter.rs               — Emitter<T> with HashMap<String, Vec<Listener<T>>>
 *                               Behind Arc<RwLock<>> for thread safety
 *                               on/once/off/emit/listener_count/event_names
 *
 * Key learning:
 *   - Go: func type as callback, closures, sync.RWMutex, slice manipulation
 *   - Rust: Box<dyn Fn> for stored callbacks, trait bounds (Send+Sync),
 *           lifetime issues with closures, Arc<RwLock<>>
 */
public class Main {

    public static void main(String[] args) {
        Emitter<String> emitter = new Emitter<>();

        // Persistent listener
        String id1 = emitter.on("user:login", data ->
                System.out.println("Login event: " + data));

        // One-shot listener
        emitter.once("user:login", data ->
                System.out.println("First login only: " + data));

        // Wildcard listener — catches all events
        emitter.on("*", data ->
                System.out.println("  [wildcard] got: " + data));

        // Another event
        emitter.on("user:logout", data ->
                System.out.println("Logout event: " + data));

        System.out.println("Events: " + emitter.eventNames());
        System.out.println("Login listeners: " + emitter.listenerCount("user:login"));

        System.out.println("\n--- Emit user:login (alice) ---");
        emitter.emit("user:login", "alice");

        System.out.println("\n--- Emit user:login (bob) ---");
        emitter.emit("user:login", "bob");
        // Note: "First login only" won't fire the second time

        System.out.println("\n--- Emit user:logout (alice) ---");
        emitter.emit("user:logout", "alice");

        System.out.println("\n--- Remove login listener and emit ---");
        emitter.off("user:login", id1);
        emitter.emit("user:login", "charlie");
        // Only wildcard fires

        System.out.println("\nLogin listeners: " + emitter.listenerCount("user:login"));
    }
}
