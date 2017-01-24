package com.gebros.platform.auth.model.common;

import android.annotation.SuppressLint;

import com.gebros.platform.auth.model.annotation.InnerObject;
import com.gebros.platform.auth.model.annotation.PropertyName;
import com.gebros.platform.auth.net.Response;
import com.gebros.platform.exception.GBExceptionType;
import com.gebros.platform.exception.GBRuntimeException;
import com.gebros.platform.log.GBLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface GBObject {

    public <T extends GBObject> T cast(Class<T> GBObjectClass);
    public <T extends GBObject> T getInnerObject(Class<T> GBObjectClass);
    public Map<String, Object> asMap();
    public JSONObject getInnerJSONObject();
    public GBAPIError getAPIError();

    public String getString(String propertyName);
    public int getInt(String propertyName);
    public long getLong(String propertyName);
    public Object getProperty(String propertyName);
    public void setProperty(String propertyName, Object propertyValue);
    public void removeProperty(String propertyName);

	final class Factory {

		private static final SimpleDateFormat[] dateFormats = new SimpleDateFormat[] {
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.KOREA),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA),
            new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA),
        };

		private Factory() {}

		public static GBObject create(JSONObject json) {
			return create(json, GBObject.class);
		}

		public static <T> GBObjectList<T> createList(JSONArray array, Class<T> GBObjectClass) {
            return new GBObjectListImpl<T>(array, GBObjectClass);
        }

        public static <T> GBObjectList<T> createList(Class<T> GBObjectClass) {
            return createList(new JSONArray(), GBObjectClass);
        }

		public static <T extends GBObject> T create(JSONObject json, Class<T> GBObjectClass) {
			return createGBObjectProxy(GBObjectClass, json);
		}

		private static <T extends GBObject> T createGBObjectProxy(Class<T> gbObjectClass, JSONObject state) {

			Class<?>[] interfaces = new Class[] {gbObjectClass};
			GBObjectProxy gbObjectProxy = new GBObjectProxy(state, gbObjectClass);

			@SuppressWarnings("unchecked")
			T gbObject = (T) Proxy.newProxyInstance(GBObject.class.getClassLoader(), interfaces, gbObjectProxy);

			return gbObject;
		}

		private static Map<String, Object> createGBObjectProxyForMap(JSONObject state) {

        	Class<?>[] interfaces = new Class[]{Map.class};
			GBObjectProxy GBObjectProxy = new GBObjectProxy(state, Map.class);

            @SuppressWarnings("unchecked")
			Map<String, Object> GBObject = (Map<String, Object>) Proxy
                    .newProxyInstance(GBObjectProxy.class.getClassLoader(), interfaces, GBObjectProxy);

            return GBObject;
        }

		/**
		 * Proxy Object
		 */

		private final static class GBObjectProxy extends ProxyBase<JSONObject> {

			private static final String CLEAR_METHOD = "clear";
			private static final String CONTAINSKEY_METHOD = "containsKey";
			private static final String CONTAINSVALUE_METHOD = "containsValue";
			private static final String ENTRYSET_METHOD = "entrySet";
			private static final String GET_METHOD = "get";
			private static final String ISEMPTY_METHOD = "isEmpty";
			private static final String KEYSET_METHOD = "keySet";
			private static final String PUT_METHOD = "put";
			private static final String PUTALL_METHOD = "putAll";
			private static final String REMOVE_METHOD = "remove";
			private static final String SIZE_METHOD = "size";
			private static final String VALUES_METHOD = "values";
			private static final String CAST_METHOD = "cast";
			private static final String CASTTOMAP_METHOD = "asMap";
			private static final String GET_INT_METHOD = "getInt";
			private static final String GET_LONG_METHOD = "getLong";
			private static final String GET_STRING_METHOD = "getString";
			private static final String GETPROPERTY_METHOD = "getProperty";
			private static final String SETPROPERTY_METHOD = "setProperty";
			private static final String REMOVEPROPERTY_METHOD = "removeProperty";
			private static final String GETINNERJSONOBJECT_METHOD = "getInnerJSONObject";
			private static final String GETINNERGB_OBJECT_METHOD = "getInnerObject";
			private static final String GET_ERROR_INFO = "getAPIError";

			private final Class<?> GBObjectClass;

			public GBObjectProxy(JSONObject state, Class<?> GBObjectClass) {
				super(state);
				this.GBObjectClass = GBObjectClass;
			}

			@Override
			public String toString() {

				try {

					return String.format(
							"GBObject={\nGBObjectClass:\"%s\", \nstate:%s}",
							GBObjectClass.getSimpleName(), state.toString(1));

				} catch (JSONException e) {
					return null;
				}
			}

			@Override
			public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

				Class<?> declaringClass = method.getDeclaringClass();

				if (declaringClass == Object.class) {
					return proxyObjectMethods(proxy, method, args);
				} else if (declaringClass == Map.class) {
					return proxyMapMethods(method, args);
				} else if (declaringClass == GBObject.class) {
					return proxyGBObjectMethods(proxy, method, args);
				} else if (GBObject.class.isAssignableFrom(declaringClass)) {
					return proxyGBObjectGettersAndSetters(method, args);
				}

				return throwUnexpectedMethodSignature(method);
			}

			private final Object proxyMapMethods(Method method, Object[] args) {

				String methodName = method.getName();
				if (methodName.equals(CLEAR_METHOD)) {
					JsonUtil.jsonObjectClear(this.state);
					return null;
				} else if (methodName.equals(CONTAINSKEY_METHOD)) {
					return this.state.has((String) args[0]);
				} else if (methodName.equals(CONTAINSVALUE_METHOD)) {
					return JsonUtil.jsonObjectContainsValue(this.state, args[0]);
				} else if (methodName.equals(ENTRYSET_METHOD)) {
					return JsonUtil.jsonObjectEntrySet(this.state);
				} else if (methodName.equals(GET_METHOD)) {
					return this.state.opt((String) args[0]);
				} else if (methodName.equals(ISEMPTY_METHOD)) {
					return this.state.length() == 0;
				} else if (methodName.equals(KEYSET_METHOD)) {
					return JsonUtil.jsonObjectKeySet(this.state);
				} else if (methodName.equals(PUT_METHOD)) {
					return setJSONProperty(args);
				} else if (methodName.equals(PUTALL_METHOD)) {
					Map<String, Object> map = null;
					if (args[0] instanceof Map<?, ?>) {
						@SuppressWarnings("unchecked")
						Map<String, Object> castMap = (Map<String, Object>) args[0];
						map = castMap;
					} else if (args[0] instanceof GBObject) {
						map = ((GBObject) args[0]).asMap();
					}
					JsonUtil.jsonObjectPutAll(this.state, map);
					return null;
				} else if (methodName.equals(REMOVE_METHOD)) {
					this.state.remove((String) args[0]);
					return null;
				} else if (methodName.equals(SIZE_METHOD)) {
					return this.state.length();
				} else if (methodName.equals(VALUES_METHOD)) {
					return JsonUtil.jsonObjectValues(this.state);
				}

				return throwUnexpectedMethodSignature(method);
			}

			/**
			 * @param proxy
			 * @param method
			 * @param args
			 * @return
			 * @throws JSONException
			 */

			private final Object proxyGBObjectMethods(Object proxy, Method method, Object[] args) throws JSONException {

				String methodName = method.getName();
				if (methodName.equals(GETINNERGB_OBJECT_METHOD)) {

					//Inner GBObject
					@SuppressWarnings("unchecked")
					Class<? extends GBObject> gbObjectClass = (Class<? extends GBObject>) args[0];

					if(gbObjectClass.getAnnotation(InnerObject.class) == null)
						throw new GBRuntimeException(GBExceptionType.ANNOTATIONS_INVALID);

					InnerObject objectNameOverride = gbObjectClass.getAnnotation(InnerObject.class);
					String GBObjectName = objectNameOverride.value();

					return Factory.createGBObjectProxy(gbObjectClass, this.state.optJSONObject(Response.API_RESULT_KEY).optJSONObject(GBObjectName));

				}else if (methodName.equals(CAST_METHOD)) {

					@SuppressWarnings("unchecked")
					Class<? extends GBObject> GBObjectClass = (Class<? extends GBObject>) args[0];

					// GBObject
					if (GBObjectClass != null && GBObjectClass.isAssignableFrom(this.GBObjectClass)) {
						return proxy;
					}

					// SubObject
					return Factory.createGBObjectProxy(GBObjectClass, this.state.optJSONObject(Response.API_RESULT_KEY));

				} else if (methodName.equals(GETINNERJSONOBJECT_METHOD)) {
					InvocationHandler handler = Proxy.getInvocationHandler(proxy);
					GBObjectProxy otherProxy = (GBObjectProxy) handler;
					return otherProxy.state;
				} else if (methodName.equals(CASTTOMAP_METHOD)) {
					return Factory.createGBObjectProxyForMap(this.state);
				} else if (methodName.equals(GETPROPERTY_METHOD)) {
					return state.opt((String) args[0]);
				} else if (methodName.equals(SETPROPERTY_METHOD)) {
					return setJSONProperty(args);
				} else if (methodName.equals(REMOVEPROPERTY_METHOD)) {
					this.state.remove((String) args[0]);
					return null;
				} else if (methodName.equals(GET_INT_METHOD)) {
					return state.getInt((String) args[0]);
				} else if (methodName.equals(GET_STRING_METHOD)) {
					return state.getString((String) args[0]);
				} else if (methodName.equals(GET_LONG_METHOD)) {
					return state.getLong((String) args[0]);
				} else if (methodName.equals(GET_ERROR_INFO)) {
					return Factory.createGBObjectProxy(GBAPIError.class, this.state.optJSONObject(Response.API_ERROR_KEY));
				}

				return throwUnexpectedMethodSignature(method);
			}

			private final Object proxyGBObjectGettersAndSetters(Method method, Object[] args)
					throws JSONException {

				String methodName = method.getName();

				int parameterCount = method.getParameterTypes().length;
				PropertyName propertyNameOverride = method.getAnnotation(PropertyName.class);

				String key = propertyNameOverride != null ? propertyNameOverride.value()
						: convertCamelCaseToLowercaseWithUnderscores(methodName.substring(3));

				// If it's a get or a set on a GBObject-derived class, we can
				// handle it.
				if (parameterCount == 0) {

					// Has to be a getter. ASSUMPTION: The GBObject-derived
					// class has been verified
					Object value = this.state.opt(key);
					Class<?> expectedType = method.getReturnType();

					Type genericReturnType = method.getGenericReturnType();
					ParameterizedType parameterizedReturnType = null;

					if (genericReturnType instanceof ParameterizedType) {
						parameterizedReturnType = (ParameterizedType) genericReturnType;
					}

					value = coerceValueToExpectedType(value, expectedType, parameterizedReturnType);
					return value;

				} else if (parameterCount == 1) {

					// Has to be a setter. ASSUMPTION: The GBObject-derived
					// class has been verified
					Object value = args[0];

					// If this is a wrapped object, store the underlying
					// JSONObject instead, in order to serialize
					// correctly.
					if (GBObject.class.isAssignableFrom(value.getClass())) {
						value = ((GBObject) value).getInnerJSONObject();
					} else if (GBObjectList.class.isAssignableFrom(value.getClass())) {
						value = ((GBObjectList<?>) value).getInnerJSONArray();
					} else if (Iterable.class.isAssignableFrom(value.getClass())) {

						JSONArray jsonArray = new JSONArray();
						Iterable<?> iterable = (Iterable<?>) value;
						for (Object o : iterable) {
							if (GBObject.class
									.isAssignableFrom(o.getClass())) {
								jsonArray.put(((GBObject) o)
										.getInnerJSONObject());
							} else {
								jsonArray.put(o);
							}
						}
						value = jsonArray;
					}

					this.state.putOpt(key, value);
					return null;
				}
				return throwUnexpectedMethodSignature(method);
			}

			private Object setJSONProperty(Object[] args) {

				String name = (String) args[0];
				Object property = args[1];
				Object value = getUnderlyingJSONObject(property);

				try {
					state.putOpt(name, value);
				} catch (JSONException e) {
					throw new IllegalArgumentException(e);
				}

				return null;
			}
		}

		// If expectedType is a generic type, expectedTypeAsParameterizedType must be provided in order to determine
        // generic parameter types.
        @SuppressLint("DefaultLocale")
		static <U> U coerceValueToExpectedType(Object value, Class<U> expectedType,
											   ParameterizedType expectedTypeAsParameterizedType) {

            if (value == null) {
                return null;
            }

            Class<?> valueType = value.getClass();
            if (expectedType.isAssignableFrom(valueType)) {
                @SuppressWarnings("unchecked")
                U result = (U) value;
                return result;
            }

            if (expectedType.isPrimitive()) {
                // If the result is a primitive, let the runtime succeed or fail at unboxing it.
                @SuppressWarnings("unchecked")
                U result = (U) value;
                return result;
            }

            if (GBObject.class.isAssignableFrom(expectedType)) {
                @SuppressWarnings("unchecked")
				Class<? extends GBObject> GBObjectClass = (Class<? extends GBObject>) expectedType;

                // We need a GBObject, but we don't have one.
                if (JSONObject.class.isAssignableFrom(valueType)) {
                    // We can wrap a JSONObject as a GBObject.
                    @SuppressWarnings("unchecked")
                    U result = (U) createGBObjectProxy(GBObjectClass, (JSONObject) value);
                    return result;
                } else if (GBObject.class.isAssignableFrom(valueType)) {
                    // We can cast a GBObject-derived class to another GBObject-derived class.
                    @SuppressWarnings("unchecked")
                    U result = (U) ((GBObject) value).cast(GBObjectClass);
                    return result;
                } else {
                    throw new RuntimeException("Can't create GBObject from " + valueType.getName());
                }

            } else if (Iterable.class.equals(expectedType) || Collection.class.equals(expectedType)
                    || List.class.equals(expectedType) || GBObjectList.class.equals(expectedType)) {
                if (expectedTypeAsParameterizedType == null) {
                    throw new RuntimeException("can't infer generic type of: " + expectedType.toString());
                }

                Type[] actualTypeArguments = expectedTypeAsParameterizedType.getActualTypeArguments();

                if (actualTypeArguments == null || actualTypeArguments.length != 1
                        || !(actualTypeArguments[0] instanceof Class<?>)) {
                    throw new RuntimeException(
                            "Expect collection properties to be of a type with exactly one generic parameter.");
                }
                Class<?> collectionGenericArgument = (Class<?>) actualTypeArguments[0];

                if (JSONArray.class.isAssignableFrom(valueType)) {

                    JSONArray jsonArray = (JSONArray) value;
                    @SuppressWarnings("unchecked")
                    U result = (U) createList(jsonArray, collectionGenericArgument);
                    return result;

                } else {

                	GBLog.d("JSONArray.class.isAssignableFrom(valueType): %s", "Can't create Collection from " + valueType.getName());
                    return null;
                }

            } else if (String.class.equals(expectedType)) {
                if (Double.class.isAssignableFrom(valueType) ||
                        Float.class.isAssignableFrom(valueType)) {

                    @SuppressWarnings("unchecked")
                    U result = (U) String.format("%f", value);
                    return result;
                } else if (Number.class.isAssignableFrom(valueType)) {

                    @SuppressWarnings("unchecked")
                    U result = (U) String.format("%d", value);
                    return result;
                }
            } else if (Date.class.equals(expectedType)) {
                if (String.class.isAssignableFrom(valueType)) {
                    for (SimpleDateFormat format : dateFormats) {
                        try {
                            Date date = format.parse((String) value);
                            if (date != null) {
                                @SuppressWarnings("unchecked")
                                U result = (U) date;
                                return result;
                            }
                        } catch (ParseException e) {
                            // Keep going.
                        	GBLog.e(e, "%s", e.getMessage());
                        }
                    }
                }
            }

            GBLog.d("Can't convert type" + valueType.getName() + " to " + expectedType.getName());
            return null;
        }

		static String convertCamelCaseToLowercaseWithUnderscores(String string) {
            string = string.replaceAll("([a-z])([A-Z])", "$1_$2");
            return string.toLowerCase(Locale.US);
        }

        private static Object getUnderlyingJSONObject(Object obj) {
            Class<?> objClass = obj.getClass();
            if (GBObject.class.isAssignableFrom(objClass)) {
				GBObject GBObject = (GBObject) obj;
                return GBObject.getInnerJSONObject();
            } else if (GBObjectList.class.isAssignableFrom(objClass)) {
				GBObjectList<?> GBObjectList = (GBObjectList<?>) obj;
                return GBObjectList.getInnerJSONArray();
            }
            return obj;
        }

		private abstract static class ProxyBase<STATE> implements InvocationHandler {
			// Pre-loaded Method objects for the methods in java.lang.Object
			private static final String EQUALS_METHOD = "equals";
			private static final String TOSTRING_METHOD = "toString";

			protected final STATE state;

			protected ProxyBase(STATE state) {
				this.state = state;
			}

			// Declared to return Object just to simplify implementation of
			// proxy helpers.
			protected final Object throwUnexpectedMethodSignature(Method method) {
				throw new RuntimeException(getClass().getName()
						+ " got an unexpected method signature: "
						+ method.toString());
			}

			protected final Object proxyObjectMethods(Object proxy, Method method, Object[] args) throws Throwable {

				String methodName = method.getName();
				if (methodName.equals(EQUALS_METHOD)) {

					Object other = args[0];
					if (other == null) {
						return false;
					}

					InvocationHandler handler = Proxy.getInvocationHandler(other);
					if (!(handler instanceof GBObjectProxy)) {
						return false;
					}

					GBObjectProxy otherProxy = (GBObjectProxy) handler;
					return this.state.equals(otherProxy.state);

				} else if (methodName.equals(TOSTRING_METHOD)) {
					return toString();
				}

				// For others, just defer to the implementation object.
				return method.invoke(this.state, args);
			}
		}

		private final static class GBObjectListImpl<T> extends AbstractList<T> implements GBObjectList<T> {

            private final JSONArray state;
            private final Class<?> itemType;

            public GBObjectListImpl(JSONArray state, Class<?> itemType) {

            	//Validate.notNull(state, "state");
                //Validate.notNull(itemType, "itemType");

                this.state = state;
                this.itemType = itemType;
            }

            @Override
            public String toString() {
                return String.format("GBObjectList{itemType=%s, state=%s}", itemType.getSimpleName(), state);
            }

            @Override
            public void add(int location, T object) {
                // We only support adding at the end of the list, due to JSONArray restrictions.
                if (location < 0) {
                    throw new IndexOutOfBoundsException();
                } else if (location < size()) {
                    throw new UnsupportedOperationException("Only adding items at the end of the list is supported.");
                }

                put(location, object);
            }

            @Override
            public T set(int location, T object) {
                checkIndex(location);

                T result = get(location);
                put(location, object);
                return result;
            }

            @Override
            public int hashCode() {
                return state.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (getClass() != obj.getClass()) {
                    return false;
                }
                @SuppressWarnings("unchecked")
                GBObjectListImpl<T> other = (GBObjectListImpl<T>) obj;
                return state.equals(other.state);
            }

            @SuppressWarnings("unchecked")
            @Override
            public T get(int location) {

                checkIndex(location);
                Object value = state.opt(location);
                T result = (T) coerceValueToExpectedType(value, itemType, null);

                return result;
            }

            @Override
            public int size() {
                return state.length();
            }

            @Override
            public final <U extends GBObject> GBObjectList<U> castToAsList(Class<U> gbObjectClass) {

            	if (GBObject.class.isAssignableFrom(itemType)) {

                    if (gbObjectClass.isAssignableFrom(itemType)) {
                        @SuppressWarnings("unchecked")
						GBObjectList<U> result = (GBObjectList<U>)this;
                        return result;
                    }

                    return createList(state, gbObjectClass);
                } else {
                    throw new GBRuntimeException("Can't cast GBObjectCollection of non-GBObject type " + itemType);
                }
            }

            @Override
            public final JSONArray getInnerJSONArray() {
                return state;
            }

            private void checkIndex(int index) {

                if (index < 0 || index >= state.length()) {
                    throw new IndexOutOfBoundsException();
                }
            }

            private void put(int index, T obj) {

                Object underlyingObject = getUnderlyingJSONObject(obj);
                try {
                    state.put(index, underlyingObject);
                } catch (JSONException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
	}
}
