package ru.otus.l161.runner;

import java.io.IOException;

public class ProcessRunnerImpl implements ProcessRunner {

	private final StringBuffer out = new StringBuffer();
    private Process process;

    public void start(String[] command) throws IOException {
        process = runProcess(command);
    }

    public void stop() {
        process.destroy();
    }

    public String getOutput() {
        return out.toString();
    }

    private Process runProcess(String[] command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        return pb.start();
    }
}
