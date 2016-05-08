package ch.bfh.ti.lineify.infrastructure;

import android.content.Context;

import java.net.MalformedURLException;

import ch.bfh.ti.lineify.core.IPermissionRequestor;
import ch.bfh.ti.lineify.core.IWayPointService;
import ch.bfh.ti.lineify.core.IWayPointStore;
import ch.bfh.ti.lineify.core.dependencyInjection.IDependencyContainer;
import ch.bfh.ti.lineify.infrastructure.azure.WayPointStore;
import ch.bfh.ti.lineify.infrastructure.location.WayPointService;

public class Registry {
    public static void initializeDependencies(IDependencyContainer container) {
        container.registerContainerControlled(IWayPointService.class, () -> new WayPointService(container.resolve(Context.class)));
        container.registerContainerControlled(IWayPointStore.class, () -> swallow(() -> new WayPointStore(container.resolve(Context.class))));
        container.registerPerResolve(IPermissionRequestor.class, () -> new PermissionRequestor(container.resolve(Context.class)));
    }

    public static <T> T swallow(X<T> x) {
        try {
            return x.run();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new Error(ex.getMessage());
        }
    }

    interface X<T> {
        T run() throws Exception;
    }
}