/**
 * 
 */
package com.cs.baseapp.api.repository;

import java.util.List;

/**
 * @author Donald.Wang
 *
 */
public interface Repository<T> {

	public T getById(String id);

	public List<T> getAll();
}
