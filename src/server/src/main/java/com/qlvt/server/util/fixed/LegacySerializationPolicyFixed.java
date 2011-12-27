/*
 * Copyright (C) 2009 - 2012 PLAYENGLISH.INFO
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.qlvt.server.util.fixed;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.impl.SerializabilityUtil;
import com.google.gwt.user.server.rpc.impl.TypeNameObfuscator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class just used for split GWT to 2 projects. It will skip validateDeserialize & validateSerialize.
 */
//TODO: will remove when production @dungvn3000
public class LegacySerializationPolicyFixed extends SerializationPolicy implements
        TypeNameObfuscator {

    private static final String ELISION_ERROR = "Type name elision in RPC "
            + "payloads is only supported if the RPC whitelist file is used.";

    /**
     * Many JRE types would appear to be {@link Serializable} on the server.
     * However, clients would not see these types as being {@link Serializable}
     * due to mismatches between the GWT JRE emulation and the real JRE. As a
     * workaround, this blacklist specifies a list of problematic types which
     * should be seen as not implementing {@link Serializable} for the purpose
     * matching the client's expectations. Note that a type on this list may still
     * be serializable via a custom serializer.
     */
    private static final Class<?>[] JRE_BLACKLIST = {
            java.lang.ArrayStoreException.class, java.lang.AssertionError.class,
            java.lang.Boolean.class, java.lang.Byte.class, java.lang.Character.class,
            java.lang.Class.class, java.lang.ClassCastException.class,
            java.lang.Double.class, java.lang.Error.class, java.lang.Float.class,
            java.lang.IllegalArgumentException.class,
            java.lang.IllegalStateException.class,
            java.lang.IndexOutOfBoundsException.class, java.lang.Integer.class,
            java.lang.Long.class, java.lang.NegativeArraySizeException.class,
            java.lang.NullPointerException.class, java.lang.Number.class,
            java.lang.NumberFormatException.class, java.lang.Short.class,
            java.lang.StackTraceElement.class, java.lang.String.class,
            java.lang.StringBuffer.class,
            java.lang.StringIndexOutOfBoundsException.class,
            java.lang.UnsupportedOperationException.class, java.util.ArrayList.class,
            java.util.ConcurrentModificationException.class, java.util.Date.class,
            java.util.EmptyStackException.class, java.util.EventObject.class,
            java.util.HashMap.class, java.util.HashSet.class,
            java.util.MissingResourceException.class,
            java.util.NoSuchElementException.class, java.util.Stack.class,
            java.util.TooManyListenersException.class, java.util.Vector.class};

    private static final Set<Class<?>> JRE_BLACKSET = new HashSet<Class<?>>(
            Arrays.asList(JRE_BLACKLIST));

    private static final LegacySerializationPolicyFixed sInstance = new LegacySerializationPolicyFixed();

    public static LegacySerializationPolicyFixed getInstance() {
        return sInstance;
    }

    /**
     * Singleton.
     */
    private LegacySerializationPolicyFixed() {
    }

    /**
     * Implemented to fail with a useful error message.
     */
    public final String getClassNameForTypeId(String id)
            throws SerializationException {
        throw new SerializationException(ELISION_ERROR);
    }

    /**
     * Implemented to fail with a useful error message.
     */
    public final String getTypeIdForClass(Class<?> clazz)
            throws SerializationException {
        throw new SerializationException(ELISION_ERROR);
    }

    @Override
    public boolean shouldDeserializeFields(Class<?> clazz) {
        return isFieldSerializable(clazz);
    }

    @Override
    public boolean shouldSerializeFields(Class<?> clazz) {
        return isFieldSerializable(clazz);
    }

    @Override
    public void validateDeserialize(Class<?> clazz) throws SerializationException {
//        if (!isInstantiable(clazz)) {
//            throw new SerializationException("Type '" + clazz.getName()
//                    + "' was not assignable to '" + IsSerializable.class.getName()
//                    + "' and did not have a custom field serializer. "
//                    + "For security purposes, this type will not be deserialized.");
//        }
    }

    @Override
    public void validateSerialize(Class<?> clazz) throws SerializationException {
//        if (!isInstantiable(clazz)) {
//            throw new SerializationException("Type '" + clazz.getName()
//                    + "' was not assignable to '" + IsSerializable.class.getName()
//                    + "' and did not have a custom field serializer."
//                    + "For security purposes, this type will not be serialized.");
//        }
    }

    /**
     * Field serializable types are primitives, {@line IsSerializable},
     * {@link Serializable}, types with custom serializers, and any arrays of
     * those types.
     */
    private boolean isFieldSerializable(Class<?> clazz) {
        if (isInstantiable(clazz)) {
            return true;
        }
        if (Serializable.class.isAssignableFrom(clazz)) {
            return !JRE_BLACKSET.contains(clazz);
        }
        return false;
    }

    /**
     * Instantiable types are primitives, {@line IsSerializable}, types with
     * custom serializers, and any arrays of those types. Merely
     * {@link Serializable} types cannot be instantiated or serialized directly
     * (only as super types of legacy serializable types).
     */
    private boolean isInstantiable(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return true;
        }
        if (clazz.isArray()) {
            return isInstantiable(clazz.getComponentType());
        }
        if (IsSerializable.class.isAssignableFrom(clazz)) {
            return true;
        }
        return SerializabilityUtil.hasCustomFieldSerializer(clazz) != null;
    }
}