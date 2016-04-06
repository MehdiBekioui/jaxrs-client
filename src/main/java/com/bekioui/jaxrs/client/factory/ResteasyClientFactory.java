/**
 * Copyright (C) 2016 Mehdi Bekioui (consulting@bekioui.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bekioui.jaxrs.client.factory;

import java.lang.reflect.Proxy;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

import com.bekioui.jaxrs.client.JaxrsClientFactory;
import com.bekioui.jaxrs.client.filter.AuthorizationFilter;
import com.bekioui.jaxrs.client.filter.ErrorFilter;
import com.bekioui.jaxrs.client.handler.ProxyInvocationHandler;

public final class ResteasyClientFactory implements JaxrsClientFactory {

	private final ResteasyWebTarget target;

	public ResteasyClientFactory(String uri) {
		this.target = new ResteasyClientBuilder() //
				.httpEngine(new ApacheHttpClient4Engine(HttpClientBuilder.create().setConnectionManager(new PoolingHttpClientConnectionManager()).build())) //
				.register(AuthorizationFilter.class) //
				.register(ErrorFilter.class) //
				.build() //
				.target(uri);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T create(Class<T> clazz) {
		return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[] { clazz }, new ProxyInvocationHandler(target.proxy(clazz)));
	}

}
