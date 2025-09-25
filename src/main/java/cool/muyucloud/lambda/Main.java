package cool.muyucloud.lambda;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Main {
    /**
     * void -> o
     *
     */
    public static void foo(Supplier<Object> supplier) {
        supplier.get();
        System.out.println("void -> o");
    }

    /**
     * void -> void
     *
     */
    public static void foo(Runnable consumer) {
        consumer.run();
        System.out.println("void -> void");
    }

    /**
     * o -> o
     *
     */
    public static void bar(Function<Object, Object> function) {
        function.apply(new Object());
        System.out.println("o -> o");
    }

    /**
     * o => void
     *
     */
    public static void bar(Consumer<Object> consumer) {
        consumer.accept(new Object());
        System.out.println("o -> void");
    }

    public static void main(String[] args) {
        /* The first set both take lambda WITHOUT input param */
        foo(() -> new Test());
        foo(() -> {});

        /* The second set both take lambda WITH a param */
//        bar(o -> new Test(o)); // Java cannot tell which method you are calling
        bar(o -> {});

        /* Fix A */
        bar((Function<Object, Object>) o -> new Test(o));
        /* Fix B */
        bar(o -> {  // Now Java knows which-is-which
            new Test(o);
        });
    }

    public static class Test {
        public Test() {
        }

        public Test(Object o) {
        }
    }
}
