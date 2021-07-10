package job;

import job.processor.Processor;
import job.reader.Reader;
import job.writer.Writer;

public class JobExecutor<K, T> implements Job {
    private final Reader<K> reader;
    private final Processor<K, T> processor;
    private final Writer<T> writer;

    public JobExecutor(Reader<K> reader,
                       Processor<K, T> processor,
                       Writer<T> writer) {
        this.reader = reader;
        this.processor = processor;
        this.writer = writer;
    }

    @Override
    public void execute() {
        try {
            K inputData = reader.read();
            T output = processor.process(inputData);
            writer.write(output);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
