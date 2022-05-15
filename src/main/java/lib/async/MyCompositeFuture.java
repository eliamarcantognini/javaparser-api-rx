package lib.async;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.impl.future.CompositeFutureImpl;

import java.util.List;

/**
 * Static class to use composite future that extends from {@link CompositeFuture}
 *
 * @see io.vertx.core.CompositeFuture
 */
public interface MyCompositeFuture extends CompositeFuture {

    /**
     * Method to join a list of futures passed as parameter
     *
     * @param futures list of futures to complete
     * @param <T>     type of future
     * @return a {@link CompositeFuture} with the same type of future present in list passed as parameter
     * @see CompositeFuture#join(List)
     */
    static <T> CompositeFuture join(List<Future<T>> futures) {
        return CompositeFutureImpl.join(futures.toArray(new Future[0]));
    }

    // TODO: javadoc or remove
    static <T> CompositeFuture all(List<Future<T>> futures) {
        return CompositeFutureImpl.all(futures.toArray(new Future[futures.size()]));
    }

    // TODO: javadoc or remove
    static <T1, T2> CompositeFuture all(Future<T1> f1, Future<T2> f2) {
        return CompositeFutureImpl.all(f1, f2);
    }
}
