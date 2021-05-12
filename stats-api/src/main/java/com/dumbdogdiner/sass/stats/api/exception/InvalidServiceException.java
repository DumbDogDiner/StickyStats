package com.dumbdogdiner.sass.stats.api.exception;

import com.dumbdogdiner.sass.stats.api.StatisticsAPIPlugin;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an exception thrown by the API when the running service could not be found.
 */
public class InvalidServiceException extends StatisticsException {
	/**
	 * The class this error was instantiated with.
	 */
	@Nullable
	@Getter
	private final Class<?> serviceClass;

	public InvalidServiceException(@Nullable Class<?> serviceClass) {
		this.serviceClass = serviceClass;
	}

	/**
	 * @return <code>true</code> if a service exists, even if it is invalid.
	 */
	public boolean exists() {
		return this.serviceClass != null;
	}

	/**
	 * @return <code>true</code> if a service exists, and is of the correct type.
	 */
	public boolean valid() {
		return this.serviceClass != null && this.serviceClass.equals(StatisticsAPIPlugin.class);
	}

	@Override
	public String getMessage() {
		return "Failed to fetch the statistics API plugin - service is invalid!";
	}
}
