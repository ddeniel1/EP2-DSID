package job.writer;

import job.processor.LeastSquares;

public class LeastSquaresWriterTemp implements Writer<LeastSquares> {
    @Override
    public void write(LeastSquares input) throws Exception {
        System.out.println(input);
    }
}
