package redis.embedded;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RedisServer extends AbstractRedisInstance {
    private static final String REDIS_READY_PATTERN = "Ready to accept connections";
    private static final int DEFAULT_REDIS_PORT = 6379;

    public RedisServer() throws IOException {
        this(DEFAULT_REDIS_PORT);
    }

    public RedisServer(int port) throws IOException {
        super(port);
        File executable = EclipseExecProvider.defaultProvider().get();
        this.args = Arrays.asList(
                executable.getAbsolutePath(),
//                "--service-install --service-name redis-server-eclipse",
                "--port", Integer.toString(port)
        );
	}

    public RedisServer(File executable, int port) {
        super(port);
        this.args = Arrays.asList(
                executable.getAbsolutePath(),
//                "--service-install --service-name redis-server-eclipse",
                "--port", Integer.toString(port)
        );
    }

    public RedisServer(IExecProvider redisExecProvider, int port) throws IOException {
        super(port);
        this.args = Arrays.asList(
                redisExecProvider.get().getAbsolutePath(),
//                "--service-install --service-name redis-server-eclipse",
                "--port", Integer.toString(port)
        );
    }

    RedisServer(List<String> args, int port) {
        super(port);
        this.args = new ArrayList<String>(args);
    }

    public static RedisServerBuilder builder() {
        return new RedisServerBuilder();
    }

    @Override
    protected String redisReadyPattern() {
        return REDIS_READY_PATTERN;
    }
}
