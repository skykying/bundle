package redis.embedded;

import java.io.File;
import java.io.IOException;

public interface IExecProvider {

	File get() throws IOException;

}
