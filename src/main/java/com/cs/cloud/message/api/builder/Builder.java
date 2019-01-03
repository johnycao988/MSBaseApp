/**
 * 
 */
package com.cs.cloud.message.api.builder;

/**
 * @author Donald.Wang
 * 
 * @Description This interface is extended by all builder. The sub interface can overwrite the <code>T build()</code>
 * 				to build <code>T</code>.
 *
 */
public interface Builder<T> {

	public T build();

}
