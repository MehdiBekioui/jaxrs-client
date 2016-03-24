# Jaxrs Client

**Jaxrs Client** provides a factory to develop your own RESTful API client. RESTEasy is the default implementation to create resources proxies.

## Get it
> Not yet available

Add the following to your Maven configuration:

```xml
<dependency>
	<groupId>com.bekioui.jaxrs</groupId>
	<artifactId>jaxrs-client</artifactId>
	<version>1.0.0</version>
</dependency>
```

## Use it

```java
JaxrsClientFactory jaxrsClientFactory = JaxrsClientFactory.create("http://localhost:8080");
MyResource myResource = jaxrsClientFactory.create(MyResource.class);
```

## License
	
	Copyright (C) 2016 Mehdi Bekioui (consulting@bekioui.com)
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
		http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.		