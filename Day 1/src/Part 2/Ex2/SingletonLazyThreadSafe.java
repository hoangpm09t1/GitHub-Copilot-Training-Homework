public class SingletonLazyThreadSafe {
    private static volatile SingletonLazyThreadSafe instance;

    private SingletonLazyThreadSafe() {
        // Private constructor prevents direct instantiation.
    }

    public static SingletonLazyThreadSafe getInstance() {
        if (instance == null) {
            synchronized (SingletonLazyThreadSafe.class) {
                if (instance == null) {
                    instance = new SingletonLazyThreadSafe();
                }
            }
        }
        return instance;
    }

    public void showMessage() {
        System.out.println("Singleton instance is ready.");
    }
}
