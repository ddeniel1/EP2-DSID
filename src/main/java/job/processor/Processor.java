package job.processor;

public interface Processor<I, O> {
    static <A, B, C> Processor<A, C> chainProcess(Processor<A, B> p1, Processor<B, C> p2) {
        return input -> p2.process(p1.process(input));
    }

    O process(I input);
}
