/**
 * Copyright 2005-2013 Dozer Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dozer.cache;

import java.util.Collection;
import java.util.Map;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.dozer.classmap.ClassMap;
import org.dozer.util.MappingUtils;

/**
 * Internal class that is responsible for producing cache keys. Only intended
 * for internal use.
 *
 * @author tierney.matt
 * @author dmitry.buzdin
 */
public final class CacheKeyFactory {

    public static CacheKey create(Class<?> srcClass, Class<?> destClass) {
        return create(srcClass, destClass, null);
    }

    public static CacheKey create(Class<?> srcClass, Class<?> destClass, String mapId) {
        return new CacheKey(srcClass, destClass, mapId);
    }

    public static class CacheKey {

        private Class<?> srcClass;
        private Class<?> destClass;
        private String mapId;

        private CacheKey() {
        }

        private CacheKey(Class<?> srcClass, Class<?> destClass, String mapId) {
            this.srcClass = srcClass;
            this.destClass = destClass;
            this.mapId = mapId;
        }

        private static ThreadLocal<CacheKey> cache = new ThreadLocal<CacheKey>() {

            @Override
            protected CacheKey initialValue() {
                return new CacheKey();
            }
        };

        public static boolean containsIn(Map<CacheKey, ClassMap> classMappings, Class<?> srcClass, Class<?> destClass, String mapId) {
            return getFrom(classMappings, srcClass, destClass, mapId) != null;
        }

        public static boolean containsIn(Map<CacheKey, ClassMap> classMappings, Class<?> srcClass, Class<?> destClass) {
            return getFrom(classMappings, srcClass, destClass) != null;
        }

//        public static CacheKey getKey(Class<?> srcClass, Class<?> destClass) {
//            CacheKey key = cache.get();
//            if (key.destClass == destClass && key.srcClass == srcClass) {
//                return key;
//            }
//            return null;
//        }

        public static ClassMap getFrom(Map<CacheKey, ClassMap> classMappings, Class<?> srcClass, Class<?> destClass) {
            return CacheKey.getFrom(classMappings, srcClass, destClass, null);
        }

        public static Object getFrom(Cache<CacheKey, Object> cache, Class<?> srcClass, Class<?> destClass) {
            CacheKey key = CacheKey.cache.get();
            Object result;
            try {
                key.destClass = destClass;
                key.srcClass = srcClass;
                key.mapId = null;
                result = cache.get(key);
            } finally {
                key.destClass = null;
                key.srcClass = null;
            }
            return result;
        }

        public static ClassMap getFrom(Map<CacheKey, ClassMap> classMappings, Class<?> srcClass, Class<?> destClass, String mapId) {
            if (classMappings == null || srcClass == null || destClass == null) {
                return null;
            }
            CacheKey key = cache.get();
            ClassMap result;
            try {
                key.destClass = destClass;
                key.srcClass = srcClass;
                key.mapId = mapId;
                result = classMappings.get(key);
            } finally {
                key.destClass = null;
                key.srcClass = null;
                key.mapId = null;
            }
            return result;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final CacheKey cacheKey = (CacheKey) o;
            if (destClass != null ? !destClass.equals(cacheKey.destClass) : cacheKey.destClass != null) {
                return false;
            }
            if (srcClass != null ? !srcClass.equals(cacheKey.srcClass) : cacheKey.srcClass != null) {
                return false;
            }
            if (mapId != null ? !mapId.equals(cacheKey.mapId) : cacheKey.mapId != null) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result;
            result = (srcClass != null ? srcClass.hashCode() : 0);
            result = 31 * result + (destClass != null ? destClass.hashCode() : 0);
            result = 31 * result + (mapId != null ? mapId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }

}
